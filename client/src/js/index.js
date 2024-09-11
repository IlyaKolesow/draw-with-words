import { postJSON, inputValidation } from "./util.js";

document.getElementById("auth-btn").addEventListener("click", () => {

    let playerName = document.getElementById("auth-input").value;

    if (inputValidation(playerName)) {
        fetch("http://localhost:8080/players",
            postJSON({ name: playerName })
        )
            .then(response => response.json())
            .then(player => {
                sessionStorage.setItem("playerName", player.name);
                sessionStorage.setItem("playerId", player.playerId);
                location = "rooms.html";
            });
    } else {
        /////
        console.log("wrong");
    }
});
