document.getElementById("auth-btn").addEventListener("click", () => {

    let playerName = document.getElementById("auth-input").value;

    fetch("http://localhost:8080/players", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            name: playerName
        })
    })
    .then(response => response.json())
    .then(player => {
        sessionStorage.setItem("playerName", player.name);
        sessionStorage.setItem("playerId", player.player_id);
        location = "rooms.html";
    });
});
