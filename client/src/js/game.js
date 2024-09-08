import { imageSocket } from "./sockets.js";

document.getElementById("img").src = sessionStorage.getItem("imageOrig");

document.getElementById("confirm-btn").addEventListener("click", () => {
    const query = document.getElementById("description").value;
    fetch("http://localhost:8080/image/generate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            query: query
        })
    })
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
                        roomId: sessionStorage.getItem("roomId"),
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

imageSocket.onmessage = event => {
    let data = JSON.parse(event.data);

    if (data.roomId == sessionStorage.getItem("roomId")) {
        generationResults.push(data);
        sessionStorage.setItem("generationResults", JSON.stringify(generationResults));
    }
};
