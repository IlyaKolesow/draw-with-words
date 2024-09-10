import { roomSocket, playerSocket } from "./sockets.js";
import { getPlayerQuantity, postJSON } from "./util.js";

function joinTheRoom(room) {
    const playerId = sessionStorage.getItem("playerId");
    fetch("http://localhost:8080/rooms/join",
        postJSON({
            player_id: playerId,
            room_id: room.id
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

    fetch("http://localhost:8080/rooms",
        postJSON({ name: roomName })
    )
        .then(response => response.json())
        .then(room => joinTheRoom(room));
});

function createRoomBlock(room, quantity) {
    const roomBlock = document.createElement("div");
    const roomName = document.createElement("p");
    const playerQuantity = document.createElement("p");
    const joinButton = document.createElement("button");

    roomName.textContent = room.name;
    playerQuantity.textContent = quantity + "/" + 5;
    joinButton.textContent = "Присоединиться";

    joinButton.addEventListener("click", () => joinTheRoom(room));

    roomBlock.appendChild(roomName);
    roomBlock.appendChild(playerQuantity);
    roomBlock.appendChild(joinButton);

    return roomBlock;
}

function fetchRooms() {
    fetch("http://localhost:8080/rooms")
        .then(response => response.json())
        .then(rooms => {
            const roomList = document.getElementById("room-list");
            roomList.innerHTML = "";
            rooms.forEach(async (room) => {
                const quantity = await getPlayerQuantity(room.id);
                if (quantity < 5 && room.status != "inGame") {
                    const roomBlock = createRoomBlock(room, quantity);
                    roomList.appendChild(roomBlock);
                }
            });
        });
}

roomSocket.onopen = event => {
    fetchRooms();
};

roomSocket.onmessage = event => {
    fetchRooms();
};