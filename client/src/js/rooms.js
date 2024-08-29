document.getElementById("create-btn").addEventListener("click", () => {
    const roomName = document.getElementById("create-input").value;

    fetch("http://localhost:8080/rooms", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            name: roomName
        })
    });
});

function joinTheRoom(roomId) {
    const playerId = sessionStorage.getItem("playerId");
    fetch("http://localhost:8080/rooms/join", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            player_id: playerId,
            room_id: roomId
        })
    });
}

function createRoomBlock(id, name, quantity) {
    const roomBlock = document.createElement("div");
    const roomName = document.createElement("p");
    const playerQuantity = document.createElement("p");
    const joinButton = document.createElement("button");

    roomName.textContent = name;
    playerQuantity.textContent = quantity + "/" + 5;
    joinButton.textContent = "Присоединиться";

    joinButton.addEventListener("click", () => joinTheRoom(id));

    roomBlock.appendChild(roomName);
    roomBlock.appendChild(playerQuantity);
    roomBlock.appendChild(joinButton);

    return roomBlock;
}

function getPlayerQuantity(roomId) {
    const quantity = fetch("http://localhost:8080/players/in/" + roomId)
        .then(response => response.json())
        .then(players => players.length)
        .catch(err => console.log(err));
    return quantity;
}

fetch("http://localhost:8080/rooms")
    .then(response => response.json())
    .then(rooms => {
        const roomList = document.getElementById("room-list");
        rooms.forEach(async (room) => {
            const quantity = await getPlayerQuantity(room.id);
            if (quantity < 5) {
                const roomBlock = createRoomBlock(room.id, room.name, quantity);
                roomList.appendChild(roomBlock);
            }
        });
    });