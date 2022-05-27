const room = {
    id: '8fca7fc1-a2e4-494b-9b02-191dc94ca467',
    status: true,
    sockets: [
        'S1RfJPQVQ260WWvuAAAP',
        'yN0piTPfla13HZYWAAAN',
        'mzMJJdGhGOXDcs4gAAAL',
        'wZxGwsyNiD1KTSLRAAAJ'
    ],
    ready: 0,
    players: {
        S1RfJPQVQ260WWvuAAAP: {
            socket: 'S1RfJPQVQ260WWvuAAAP',
            playerIndex: 1,
            money: 1000000,
            position: 1,
            stocks: [Object],
            yourTurn: false,
            dice_1: 5,
            dice_2: 5,
            dice_Count: 10
        },
        yN0piTPfla13HZYWAAAN: {
            socket: 'yN0piTPfla13HZYWAAAN',
            playerIndex: 2,
            money: 1000000,
            position: 2,
            stocks: [Object],
            yourTurn: false,
            dice_1: 4,
            dice_2: 4,
            dice_Count: 8
        },
        mzMJJdGhGOXDcs4gAAAL: {
            socket: 'mzMJJdGhGOXDcs4gAAAL',
            playerIndex: 3,
            money: 1000000,
            position: 3,
            stocks: [Object],
            yourTurn: false,
            dice_1: 3,
            dice_2: 3,
            dice_Count: 6
        },
        wZxGwsyNiD1KTSLRAAAJ: {
            socket: 'wZxGwsyNiD1KTSLRAAAJ',
            playerIndex: 4,
            money: 1000000,
            position: 4,
            stocks: [Object],
            yourTurn: false,
            dice_1: 6,
            dice_2: 6,
            dice_Count: 12
        }
    },
    counterForStocks: 0,
    counterForDice: 0
}

let a =  Object.values(room.players)
let max = Math.max.apply(Math, a.map(function(o) {
    return o.dice_Count;
}))

console.log(max)