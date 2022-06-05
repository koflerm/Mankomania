let players = {
    "pTXmcVIA2UV_r7hJAAAJ": {
    "socket": "pTXmcVIA2UV_r7hJAAAJ",
        "playerIndex": 1,
        "money": 1000000,
        "position": 0,
        "stocks": {
        "HardSteel_PLC": 2,
            "ShortCircuit_PLC": 0,
            "DryOil_PLC": 0
    },
    "yourTurn": true,
        "dice_1": 6,
        "dice_2": 6,
        "dice_Count": 12
},
    "dCwQokTNxDT7YCC6AAAL": {
    "socket": "dCwQokTNxDT7YCC6AAAL",
        "playerIndex": 2,
        "money": 1000000,
        "position": 1,
        "stocks": {
        "HardSteel_PLC": 2,
            "ShortCircuit_PLC": 0,
            "DryOil_PLC": 0
    },
    "yourTurn": false,
        "dice_1": 6,
        "dice_2": 1,
        "dice_Count": 7
},
    "0boePgPUzTS9U-kYAAAN": {
    "socket": "0boePgPUzTS9U-kYAAAN",
        "playerIndex": 3,
        "money": 1000000,
        "position": 2,
        "stocks": {
        "HardSteel_PLC": 2,
            "ShortCircuit_PLC": 0,
            "DryOil_PLC": 0
    },
    "yourTurn": false,
        "dice_1": 6,
        "dice_2": 5,
        "dice_Count": 11
},
    "y94JPlkimCvbTaE1AAAP": {
    "socket": "y94JPlkimCvbTaE1AAAP",
        "playerIndex": 4,
        "money": 1000000,
        "position": 3,
        "stocks": {
        "HardSteel_PLC": 2,
            "ShortCircuit_PLC": 0,
            "DryOil_PLC": 0
    },
    "yourTurn": false,
        "dice_1": 1,
        "dice_2": 2,
        "dice_Count": 3
}
}

let sockets = [];
for (const key in players) {
    sockets.push(key)
}

let player1 = players[sockets[0]];
let player2 = players[sockets[1]];
let player3 = players[sockets[2]];
let player4 = players[sockets[3]];








