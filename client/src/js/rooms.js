import { roomSocket, playerSocket } from "./sockets.js";
import { getPlayerQuantity, nameValidation, postJSON } from "./util.js";

function joinTheRoom(room) {
    const playerId = sessionStorage.getItem("playerId");
    fetch("http://localhost:8080/rooms/join",
        postJSON({
            playerId: playerId,
            roomId: room.id
        })
    )
        .then(() => {
            sessionStorage.setItem("roomId", room.id);
            sessionStorage.setItem("roomName", room.name);
            roomSocket.send("");
            playerSocket.send("");
            location = "lobby.html";
        });
}

document.getElementById("create-btn").addEventListener("click", () => {
    const roomName = document.getElementById("create-input").value;
    const errorMessageElement = document.getElementById("error-message");

    if (nameValidation(roomName)) {
        fetch("http://localhost:8080/rooms",
            postJSON({ name: roomName })
        )
            .then(response => response.json())
            .then(room => joinTheRoom(room))
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

function createRoomBlock(room, quantity) {
    const roomBlock = document.createElement("div");
    const container = document.createElement("div");
    const roomName = document.createElement("p");
    const playerQuantity = document.createElement("p");
    const joinButton = document.createElement("button");

    roomBlock.className = "room-block";
    container.className = "quantity-and-btn";
    roomName.textContent = room.name;
    playerQuantity.textContent = quantity + "/" + 5;
    joinButton.textContent = "Присоединиться";

    joinButton.addEventListener("click", () => joinTheRoom(room));

    roomBlock.appendChild(roomName);
    container.appendChild(playerQuantity);
    container.appendChild(joinButton);
    roomBlock.appendChild(container);

    return roomBlock;
}

function fetchRooms() {
    fetch("http://localhost:8080/rooms")
        .then(response => response.json())
        .then(rooms => {
            const roomList = document.getElementById("room-list");
            roomList.innerHTML = "";
            if (rooms.length == 0) {
                const emptyMessage = document.createElement("p");
                emptyMessage.textContent = "Список пуст";
                emptyMessage.style.color = "#666";
                emptyMessage.style.fontSize = "2em";
                roomList.appendChild(emptyMessage);
            } else {
                rooms.forEach(async (room) => {
                    const quantity = await getPlayerQuantity(room.id);
                    if (quantity < 5 && room.status != "inGame") {
                        const roomBlock = createRoomBlock(room, quantity);
                        roomList.appendChild(roomBlock);
                    }
                });
            }
        });
}

roomSocket.onopen = event => {
    fetchRooms();
};

roomSocket.onmessage = event => {
    fetchRooms();
};