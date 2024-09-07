import { playerSocket, roomSocket } from "./sockets.js";

function leave() {
    const playerId = sessionStorage.getItem("playerId");
    fetch("http://localhost:8080/rooms/leave", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            id: playerId
        })
    })
        .then(() => {
            sessionStorage.removeItem("roomId");
            sessionStorage.removeItem("roomName");
            playerSocket.send("");
            location = "rooms.html";
        });
}

function play() {
    fetch("http://localhost:8080/rooms/" + sessionStorage.getItem("roomId") + "/play")
        .then(() => {
            roomSocket.send("start");
            location = "game.html";
        });
}

document.getElementById("leave-btn").addEventListener("click", leave);
document.getElementById("play-btn").addEventListener("click", play);
document.getElementsByTagName("h1")[0].textContent = sessionStorage.getItem("roomName");

function fetchPlayers() {
    fetch("http://localhost:8080/players/in/" + sessionStorage.getItem("roomId"))
        .then(response => response.json())
        .then(players => {
            const playerList = document.getElementById("player-list");
            playerList.innerHTML = "";
            players.forEach(player => {
                const playerName = document.createElement("p");
                playerName.textContent = player.name;
                playerList.appendChild(playerName);
            });
        });
}

playerSocket.onopen = event => {
    fetchPlayers();
};

playerSocket.onmessage = event => {
    fetchPlayers();
};

roomSocket.onmessage = event => {
    if (event.data == "start")
        location = "game.html";
};