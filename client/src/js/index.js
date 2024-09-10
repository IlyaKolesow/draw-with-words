import { postJSON } from "./util";

document.getElementById("auth-btn").addEventListener("click", () => {

    let playerName = document.getElementById("auth-input").value;

    fetch("http://localhost:8080/players",
        postJSON({ name: playerName })
    )
        .then(response => response.json())
        .then(player => {
            sessionStorage.setItem("playerName", player.name);
            sessionStorage.setItem("playerId", player.player_id);
            location = "rooms.html";
        });
});
