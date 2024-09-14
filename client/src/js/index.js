import { postJSON, nameValidation } from "./util.js";

document.getElementById("auth-btn").addEventListener("click", () => {
    let playerName = document.getElementById("auth-input").value;
    let errorMessageElement = document.getElementById("error-message");

    if (nameValidation(playerName)) {
        fetch("http://localhost:8080/players", postJSON({ name: playerName }))
            .then(response => response.json())
            .then(player => {
                sessionStorage.setItem("playerName", player.name);
                sessionStorage.setItem("playerId", player.playerId);
                location = "rooms.html";
            })
            .catch(error => {
                errorMessageElement.textContent = "Ошибка сети или сервера. Попробуйте позже.";
                errorMessageElement.style.display = "block";
            });
            errorMessageElement.style.display = "none";
    } else {
        errorMessageElement.textContent = "Как минимум 2 символа: буквы, цифры, подчеркивания";
        errorMessageElement.style.display = "block";
    }
});
