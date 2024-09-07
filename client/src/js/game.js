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
                    imageSocket.send("data:image/png;base64, " + data.image);
                    clearInterval(timerId);
                }
            });
    }, 2000);
}

const imageResults = [];

imageSocket.onmessage = event => {
    imageResults.push(event.data);
};
