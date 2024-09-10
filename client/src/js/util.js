export function postJSON(body) {
    const obj = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(body)
    };
    return obj;
}

export function getPlayerQuantity(roomId) {
    const quantity = fetch("http://localhost:8080/players/in/" + roomId)
        .then(response => response.json())
        .then(players => players.length);
    return quantity;
}
