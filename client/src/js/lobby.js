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

document.getElementById("leave-btn").addEventListener("click", leave);

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

document.getElementsByTagName("h1")[0].textContent = sessionStorage.getItem("roomName");