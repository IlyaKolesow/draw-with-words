import { playerSocket, roomSocket } from "./sockets.js";
import { postJSON } from "./util.js";

const roomId = sessionStorage.getItem("roomId");

function leave() {
    const playerId = sessionStorage.getItem("playerId");
    fetch("http://localhost:8080/rooms/leave",
        postJSON({ id: playerId })
    )
        .then(() => {
            sessionStorage.removeItem("roomId");
            sessionStorage.removeItem("roomName");
            playerSocket.send("");
            roomSocket.send("");
            location = "rooms.html";
        });
}

function play() {
    fetch("http://localhost:8080/rooms/status",
        postJSON({
            roomId: roomId,
            status: "inGame"
        })
    )
        .then(response => response.json())
        .then(room => {
            roomSocket.send(JSON.stringify({
                roomId: roomId,
                action: "start",
                image: room.imageUrl
            }));
        });
}

document.getElementById("leave-btn").addEventListener("click", leave);
document.getElementById("play-btn").addEventListener("click", play);
document.getElementsByTagName("h1")[0].textContent = sessionStorage.getItem("roomName");

function fetchPlayers() {
    fetch("http://localhost:8080/players/in/" + roomId)
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
    if (event.data) {
        let data = JSON.parse(event.data);
        if (data.roomId == roomId && data.action == "start") {
            sessionStorage.setItem("imageOrig", data.image);
            location = "game.html";
        }
    }
};