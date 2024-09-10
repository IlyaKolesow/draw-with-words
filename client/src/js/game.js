import { imageSocket, roomSocket } from "./sockets.js";
import { getPlayerQuantity, postJSON } from "./util.js";

const roomId = sessionStorage.getItem("roomId");

document.getElementById("img-orig").src = sessionStorage.getItem("imageOrig");

document.getElementById("confirm-btn").addEventListener("click", () => {
    const query = document.getElementById("description").value;
    fetch("http://localhost:8080/image/generate",
        postJSON({ query: query })
    )
        .then(response => response.json())
        .then(data => checkStatus(data.uuid));

    waitingForResults();
});

function waitingForResults() {
}

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
        sessionStorage.removeItem("generationResults");
        document.getElementById("desc-block").remove();
        createImgBlock();
    }
};

function createImgBlock() {
    const playerName = document.createElement("p");
    const imageResult = document.createElement("img");
    const nextBtn = document.createElement("button");

    playerName.id = "player-name";
    imageResult.id = "image-result";
    nextBtn.textContent = "=>";

    nextBtn.addEventListener("click", () => {
        if (generationResults.length > 0)
            imageSocket.send("update");
        else
            gameOver();
    });

    document.getElementById("img-block").appendChild(playerName);
    document.getElementById("img-block").appendChild(imageResult);
    document.getElementById("img-block").appendChild(nextBtn);

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