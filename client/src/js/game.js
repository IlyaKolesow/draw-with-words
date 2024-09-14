import { imageSocket, roomSocket } from "./sockets.js";
import { getPlayerQuantity, postJSON, promptValidation } from "./util.js";

const roomId = sessionStorage.getItem("roomId");

document.getElementById("img-orig").src = sessionStorage.getItem("imageOrig");

document.getElementById("confirm-btn").addEventListener("click", () => {
    const query = document.getElementById("description").value;
    const errorMessageElement = document.getElementById("error-message");

    if (promptValidation(query)) {
        document.getElementById("loading").style.display = "flex";

        fetch("http://localhost:8080/image/generate",
            postJSON({ query: query })
        )
            .then(response => response.json())
            .then(data => checkStatus(data.uuid))
            .catch(error => {
                errorMessageElement.textContent = "Ошибка генерации. Попробуйте позже.";
                errorMessageElement.style.display = "block";
            })
    } else {
        if (query.length < 1)
            errorMessageElement.textContent = "Необходимо описание";
        else
            errorMessageElement.textContent = "Введены запрещенные символы";
        errorMessageElement.style.display = "block";
    }
});

function checkStatus(uuid) {
    let timerId = setInterval(() => {
        fetch("http://localhost:8080/image/status/" + uuid)
            .then(response => response.json())
            .then(data => {
                if (data.image != "PROCESSING") {
                    imageSocket.send(JSON.stringify({
                        playerName: sessionStorage.getItem("playerName"),
                        roomId: roomId,
                        image: "data:image/png;base64, " + data.image
                    }));
                    clearInterval(timerId);
                }
            });
    }, 2000);
}

let generationResults;

if (sessionStorage.getItem("generationResults"))
    generationResults = JSON.parse(sessionStorage.getItem("generationResults"));
else
    generationResults = [];

imageSocket.onmessage = async event => {
    if (event.data == "update") {
        updateResult();
        return;
    }

    let data = JSON.parse(event.data);

    if (data.roomId == roomId) {
        generationResults.push(data);
        sessionStorage.setItem("generationResults", JSON.stringify(generationResults));
    }

    const playerQuantity = await getPlayerQuantity(roomId);

    if (generationResults.length == playerQuantity) {
        document.getElementById("loading").style.display = "none";
        sessionStorage.removeItem("generationResults");
        document.getElementById("desc-block").remove();
        createImgBlock();
    }
};

function createImgBlock() {
    const playerName = document.createElement("p");
    const originalImage = document.getElementById("img-orig");  // Исходная картинка
    const generatedImage = document.createElement("img");
    const nextBtn = document.createElement("button");

    playerName.id = "player-name";
    generatedImage.id = "image-result";

    nextBtn.id = "next-btn";
    nextBtn.textContent = "Далее";

    nextBtn.addEventListener("click", () => {
        if (generationResults.length > 0)
            imageSocket.send("update");
        else
            gameOver();
    });

    const imgBlock = document.getElementById("img-block");
    imgBlock.appendChild(playerName);
    imgBlock.appendChild(generatedImage);
    imgBlock.appendChild(nextBtn);

    // Применяем классы для стилей
    imgBlock.classList.add("img-block-flex");

    updateResult();
}

function updateResult() {
    let data = generationResults.pop();

    if (data) {
        document.getElementById("player-name").textContent = data.playerName;
        document.getElementById("image-result").src = data.image;
    }
}

function gameOver() {
    fetch("http://localhost:8080/rooms/status",
        postJSON({
            roomId: roomId,
            status: "inLobby"
        }))
        .then(() => {
            roomSocket.send(JSON.stringify({
                roomId: roomId,
                action: "ending"
            }));
        });
}

roomSocket.onmessage = event => {
    if (event.data) {
        let data = JSON.parse(event.data);
        if (data.roomId == roomId && data.action == "ending") {
            sessionStorage.removeItem("roomStatus");
            sessionStorage.removeItem("imageOrig");
            location = "lobby.html";
        }
    }
}