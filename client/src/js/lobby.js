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
            location = "rooms.html";
        });
}

function play() {
    fetch("http://localhost:8080/rooms/" + sessionStorage.getItem("roomId") + "/play")
        .then(() => {
            sessionStorage.setItem("roomStatus", "inGame");
            location = "game.html";
        });
}

function checkRoomStatus() {
    fetch("http://localhost:8080/rooms/" + sessionStorage.getItem("roomId") + "/status")
        .then(response => response.json())
        .then(response => {
            if (response.status == "inGame") {
                sessionStorage.setItem("roomStatus", "inGame");
                location = "game.html";
            }
        });
}

document.getElementById("leave-btn").addEventListener("click", leave);
document.getElementById("play-btn").addEventListener("click", play);
document.getElementsByTagName("h1")[0].textContent = sessionStorage.getItem("roomName");

fetch("http://localhost:8080/players/in/" + sessionStorage.getItem("roomId"))
    .then(response => response.json())
    .then(players => {
        const playerList = document.getElementById("player-list");
        players.forEach(player => {
            const playerName = document.createElement("p");
            playerName.textContent = player.name;
            playerList.appendChild(playerName);
        });
    });

setInterval(checkRoomStatus, 1000);