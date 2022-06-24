const express = require('express')
const app = express();
const http = require('http');
const server = http.createServer(app);
const {Server} = require("socket.io");
const io = new Server(server, { cors: { origin: '*'} });
const PORT = process.env.PORT || 3000;
const { v4: uuidv4 } = require('uuid');
const {instrument} = require("@socket.io/admin-ui");
//sonar-scanner.bat -D"sonar.organization=koflerm" -D"sonar.projectKey=Mankomania_backend" -D"sonar.sources=." -D"sonar.host.url=https://sonarcloud.io"
//https://sonarcloud.io/summary/new_code?id=Mankomania_backend


/**
 * Instance Variables
 */
const rooms = {};
const START_MONEY = 1000000
const MAX_LOBBY_SIZE = 4
const moneyTransferByPlayerCollision = 10000
const moneyMinigameStock = 20000



/**
 * Check if a private Room was send or a new room should be created or join to an room
 * @param socket A connected socket.io socket
 * @param room A string that represents the roomID
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 */
function validateRoom(socket, room, _rooms){
    if(room === ''){
        let roomID = searchEmptyRooms(_rooms);
        if (roomID === undefined){
            joinRoom(socket, createRoom(uuidv4()),  _rooms);
        }else{
            return joinRoom(socket,roomID, _rooms)
        }
    }else{
        return null;
    }
}

/**
 * Will search for Empty Rooms and returns a roomID if an empty room is available, if not return undefined
 *  * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @returns {room} A string that represents the roomID
 */
function searchEmptyRooms(_rooms){
    for (const id in _rooms) {
        const room = _rooms[id];
        if(room.sockets.length < MAX_LOBBY_SIZE && room.status === false){
            return room;
        }
    }
}

/**
 * Will create a new Room and returns the room
 * @param uuid
 * @returns {{counterForStocks: number, stockCounterFunction: function(*, *, *): void, ready: number, players: {}, counterForHorseRace: number, counterForDice: number, id: *, sockets: [], status: boolean}}
 */
 function  createRoom (uuid) {
    const room = {
        id: uuid, // generate a unique id for the new room, that way we don't need to deal with duplicates.
        status: false,
        sockets: [],
        ready: 0,
        players: {},
        stockCounterFunction : function (room, socket, stock){
            rooms[room].players[socket.id].stocks = stock
            this.counterForStocks++
        },
        counterForStocks : 0,
        counterForDice: 0,
        counterForHorseRace: 0
    };
    rooms[room.id] = room;
    return room;
}



/**
 * Will connect an socket to an specified room
 * @param socket A connected socket.io socket
 * @param room An object that represents a room from the `rooms` instance variable object
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 */
 function joinRoom(socket, room,  _rooms) {
     if(room === null || _rooms === null){
         io.to(socket).emit('ERROR', "Error in joinRoom");

     }else{
         //push socket id into socket array in room object
         room.sockets.push(socket.id);
         _rooms[room.id].players[socket.id] = createPlayer(room, socket.id, stocks())

         //socket joins room
         socket.join(room.id);
         console.log(socket.id, "Joined", room.id);
         io.in(room.id).emit('JOIN_ROOM', room.id, room.sockets)

         //when lobby is full
         if(room.sockets.length === MAX_LOBBY_SIZE){
             room.status = true;
             console.log(rooms[room.id])
             createRoom(uuidv4());
             io.in(room.id).emit('START_GAME', room.id, room.players);
         }
         return room.id
     }
}



/**
 * Will create a new player object and insert it into the players object
 * @param room An object that represents a room from the `rooms` instance variable object
 * @param socket An connected socket.io socket
 * @param stocks An object that represents stocks and their default values
 */
  const createPlayer = (room,socket, stocks) =>{
   return  {
        socket: socket,
        playerIndex: room.sockets.length,
        money: START_MONEY,
        position: room.sockets.length - 1,
        stocks: stocks,
        yourTurn: false,
        dice_1: 0,
        dice_2: 0,
        dice_Count: 0,
       calculateDiceCount : function (diceCount){
           this.dice_1 = diceCount[0]
           this.dice_2 = diceCount[1]
           this.dice_Count = this.dice_1 +  this.dice_2
       }
    }

}


/**
 * Will return an object with stocks and default value
 * @returns {{ShortCircuit_PLC: number, HardSteel_PLC: number, DryOil_PLC: number}}
 */
 function stocks (){
    return  {
        HardSteel_PLC: 0,
        ShortCircuit_PLC: 0,
        DryOil_PLC : 0
    }
}



/**
 * Will make the socket leave any rooms that it is a part of
 * @param socket A connected socket.io socket
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 */
 function leaveRooms(socket, _rooms){
     if(_rooms === null){
         io.to(socket).emit('ERROR', "Error in leaveRooms");
     }else{
         const roomsToDelete = [];
         // check to see if the socket is in the current room
         for(const id in _rooms){
             const room = _rooms[id];

             if(room.sockets.includes(socket.id)){
                 socket.leave(id)
                 // remove the socket from the room object
                 room.sockets = room.sockets.filter((item) => item !== socket.id);
                 delete room.players[socket.id]

                 console.log(room)
                 io.in(room.id).emit('JOIN_ROOM', room.id, room.sockets);

             }
             // Prepare to delete any rooms that are now empty
             if (room.sockets.length === 0) {
                 roomsToDelete.push(room);
             }
         }
         // Delete all the empty rooms that found earlier
         for (const room of roomsToDelete) {
             delete _rooms[room.id];
         }
     }
}


/**
 * Increase the ready counter for thr room object
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param socket A connected socket.io socket
 * @param room A string that represents the roomID
 */
 function increaseReadyCounterForRoom(_rooms, socket, room){
     if(_rooms === null || room === null){
         io.to(socket).emit('ERROR', "Error in increaseReadyCounterForRoom");
     }else{
         _rooms.ready++;

         if(_rooms.ready === _rooms.sockets.length && _rooms.sockets.length >= 2){
             _rooms.status = true;
             io.in(room).emit('START_GAME', room, _rooms);
             console.log("Game starts " + room)
             return _rooms.status
         }else{
             return _rooms.ready
         }
     }

}



/**
 * Will save dice1,dice2 and calculate the total dice count
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room  A string that represents the roomID
 * @param socket A connected socket.io socket
 * @param diceCount A counter to check how many clients have rolled their dice
 * @param winnerLength The length of the winner array
 */
const saveDice = (_rooms, room, socket, diceCount, winnerLength) =>{
    if(_rooms === null || room === null || diceCount === null){
        io.to(socket).emit('ERROR', "Error in saveDice");
    }else{
        if(typeof diceCount === 'string' || diceCount instanceof String){
            let diceCountArray = diceCount.split(',').map(Number)
            console.log(diceCountArray[0] + diceCountArray[1])
            return saveDiceProcess(_rooms,room, socket, diceCountArray, winnerLength)

        }else{
            return saveDiceProcess(_rooms, room, socket, diceCount, winnerLength)
        }

    }

}

/**
 * Check if if all clients have rolled their dices and check if we have winners to role again
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param socket socket A connected socket.io socket
 * @param diceCount A counter to check how many clients have rolled their dice
 * @param winnerLength The length of the winner array
 */
const saveDiceProcess = (_rooms, room, socket, diceCount, winnerLength) =>{
    if(_rooms === null || room === null || diceCount === null){
        io.to(socket).emit('ERROR', "Error in saveDiceProcess");
    }else{
        console.log(_rooms)
        _rooms.players[socket.id].calculateDiceCount(diceCount)
        _rooms.counterForDice++

        if (winnerLength != null){//if we have one or more winner
            if (_rooms.counterForDice === winnerLength){
                validateHighestDice(_rooms, room)
                return _rooms.counterForDice
            }
        }else{
            //if all clients have rolled their dice
            if (_rooms.counterForDice === _rooms.sockets.length){
                validateHighestDice(_rooms, room)
                return _rooms.counterForDice
            }else{
                return _rooms.counterForDice
            }
        }
    }

}

/**
 * Will validate the highest Dice count and send the winner/s
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 */
 const validateHighestDice = (_rooms,room) =>{
     if (_rooms === null || room === null){
         io.in(room).emit('ERROR', "Error in validateHighestDice");
     }else{
         //convert players object into an array
         let data = Object.values(_rooms.players)

         //returns the highest dice count of all players
         let highestDice = Math.max.apply(Math, data.map(function(o) {
             return o.dice_Count;
         }))

         //returns the winner player/s
         let winner = data.filter(x => [x.dice_Count] == highestDice)
         console.log(winner)
         //if we have one winner
         if(winner.length === 1){
             _rooms.players[winner[0].socket].yourTurn = true;
             io.in(room).emit('START_ROUND', _rooms.players, winner[0].socket);
             resetPlayersDice(_rooms)
             return winner[0].socket;
         }
         //if we have to ore more winners
         else{
             io.in(room).emit('ROLE_THE_HIGHEST_DICE_AGAIN', _rooms.players, winner);
             resetPlayersDice(_rooms)
             return winner
         }
     }

}


/**
 * Will reset dice_1, dice_2, dice_Count and counterForDice
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 */
 const resetPlayersDice = (_rooms) =>{
     if(_rooms === null){
         return null;
     }else{
         Object.keys(_rooms.players).forEach((key =>{
             _rooms.players[key].dice_1 = 0
             _rooms.players[key].dice_2 = 0
             _rooms.players[key].dice_Count = 0
         }))
         _rooms.counterForDice = 0
         return _rooms
     }
}



/**
 * Will update the player object with the new stock object
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room An object that represents a room from the `rooms` instance variable object
 * @param socket An connected socket.io socket
 * @param stock An object that represents stocks and their default values
 */
 const updateStock = (_rooms, room, socket, stock)=>{
    if(room === null || stock === null || _rooms === null){
        io.to(socket).emit('ERROR', "Error in updateStock");
     }else{
        _rooms.stockCounterFunction(room, socket, stock)
        if (_rooms.counterForStocks === _rooms.sockets.length){
            console.log("Update Stock finished " + _rooms.sockets)
            io.in(room).emit('ROLE_THE_HIGHEST_DICE', _rooms.players);
        }
    }

}


/**
 * Update dices and emit it to the other players in room
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param diceCount A counter to check how many clients have rolled their dice
 * @param socket  A connected socket.io socket
 */
 const updateDice = (_rooms, room, diceCount, socket) =>{
    if(room === null || diceCount === null || _rooms === null){
        io.to(socket).emit('ERROR', "Error in updateDice");
    }else{
        _rooms.players[socket.id].dice_1 = diceCount[0]
        _rooms.players[socket.id].dice_1 = diceCount[1]

        if(diceCount[0]  + diceCount[1] === 12){
            _rooms.players[socket.id].money -= 100000
            socket.to(room).emit('UPDATE_DICE', socket.id, diceCount)
        }else{
            socket.to(room).emit('UPDATE_DICE', socket.id, diceCount)
        }
    }
}



/**
 * Update player position and emit it to the other players in room
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room  A string that represents the roomID
 * @param position position the current player
 * @param socket socket A connected socket.io socket
 */
 const updatePlayerPosition = (_rooms,room, position, socket) =>{
     if(_rooms === null || room === null || position === null){
         io.to(socket).emit('ERROR', "Error in updatePlayerPosition");
     }else{
         _rooms.players[socket.id].position = position;
         socket.to(room).emit('UPDATE_PLAYER_POSITION', socket.id, position);
     }
}

/**
 * validate the next player in the game and emit it to the other players in room
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param socket socket A connected socket.io socket
 */
 const validateNextTurn = (_rooms, room, socket) =>{
    if (_rooms === null || room === null){
        io.to(socket).emit('ERROR', "Error in validateNextTurn");
    }else{
        _rooms.players[socket.id].yourTurn = false;
        let nextPlayer = navObj(_rooms.players, socket, 1)
        console.log("Next Player " + nextPlayer)
        _rooms.players[nextPlayer].yourTurn = true;
        socket.to(room).emit('NEXT_TURN', nextPlayer)
    }

}


/**
 * Return the next socketID in the players object
 * @param obj  An object that represents a room from the `rooms` instance variable object
 * @param currentKey currentKey = socket.id
 * @param direction move one key forward in the object
 * @returns {*}
 */
 const navObj = (obj, currentKey, direction) => {
     if (obj === null || direction === null || currentKey === null || direction > 1){
         return null;
     }else{
         let next =  (Object.values(obj)[Object.keys(obj).indexOf(currentKey.socket) + direction]);
         if(next !== undefined){
             return next.socket;
         }else{
             return (obj[Object.keys(obj)[0]]).socket
         }
     }
}



/**
 * Reduce money from player and emit it to the other players in room
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param amount amount of money
 * @param socket socket A connected socket.io socket
 */
 const playerLoseMoney = (_rooms, room, amount, socket) =>{
    if(room === null || amount === null || _rooms === null){
        io.to(socket).emit('ERROR', "Error in playerLoseMoney");
    }else{
        console.log("LOSE_MONEY: Room: " + room + " Amount: " + amount + " Socket: " + socket.id);
        socket.to(room).emit('LOSE_MONEY', socket.id, amount);
        _rooms.players[socket.id].money -= amount;
        return  _rooms.players[socket.id].money
    }
}

/**
 * Add money to player and emit it to the other players in room
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param amount amount of money
 * @param socket socket A connected socket.io socket
 */
 const playerGetMoney = (_rooms, room, amount, socket) =>{
        if(room === null || amount === null || _rooms === null){
            io.to(socket).emit('ERROR', "Error in playerGetMoney");
        }else {
            console.log("GET_MONEY: Room: " + room + " Amount: " + amount + " Socket: " + socket.id);
            socket.to(room).emit('GET_MONEY', socket.id, amount);
            return _rooms.players[socket.id].money += amount;
        }
}


/**
 * Check if we have an player collision and emit it to the other players in room
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param collision collision object
 * @param socket socket A connected socket.io socket
 */
  const playerCollision =(_rooms, room, collision, socket)=> {
    if (room === null || collision === null || _rooms === null) {
        io.to(socket).emit('ERROR', "Error in playerCollision");
    } else {
        _rooms.players[socket.id].money -= moneyTransferByPlayerCollision * (collision.length - 1);
        for (let element of collision) {
            if (element !== socket.id)
                _rooms.players[element].money += moneyTransferByPlayerCollision;
        }
        socket.to(room).emit('PLAYER_COLLISION', socket.id, collision);
    }
}

/**
 * Function for stockMiniGame
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param stock stock object for minigame
 * @param socket socket A connected socket.io socket
 */
 const stockMiniGame = (_rooms, room, stock, socket)=>{
     if(room === null || stock === null || _rooms === null){
         io.to(socket).emit('ERROR', "Error in stockMiniGame");
     }else{
         let players = _rooms.players
         Object.keys(players).forEach((key =>{
             if(players[key].stocks[stock.stockName] > 0){
                if (stock.status === true){
                    _rooms.players[key].money +=  players[key].stocks[stock.stockName] * moneyMinigameStock
                }else{
                    _rooms.players[key].money -=  players[key].stocks[stock.stockName] * moneyMinigameStock
                }
             }
         }))
         socket.to(room).emit('STOCK', socket.id, stock)
     }
}

/**
 * Function for auctionMiniGame
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param auctionObject auctionObject for minigame
 * @param socket A connected socket.io socket
 */
 const  auctionMiniGame = (_rooms, room, auctionObject, socket)=>{
     if(room === null || auctionObject === null || _rooms === null){
         io.to(socket).emit('ERROR', "Error in auctionMiniGame");
     }else{
         _rooms.players[socket.id].money = auctionObject.moneyToSet
         socket.to(room).emit('AUCTION', socket.id, auctionObject)
     }
}


/**
 * initialize horseGame
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param socket A connected socket.io socket
 * @returns {*} counterForHorseRace
 */
 const raceMiniGame = (_rooms, room, socket) =>{
    if(room === null || _rooms === null){
        io.to(socket).emit('ERROR', "Error in raceMiniGame");
    }else{
        _rooms.counterForHorseRace++;
        socket.to(room).emit('RACE', socket.id)
        return _rooms.counterForHorseRace
    }
}


/**
 * Check if the counterForHorseRace is ready for the minigame to start
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param socket A connected socket.io socket
 * @returns {*} counterForHorseRace
 */
 const validateRaceMiniGame = (_rooms,room, socket) =>{
    if(room === null || _rooms === null){
        io.to(socket).emit('ERROR', "Error in validateRaceMiniGame");
    }else{
        _rooms.counterForHorseRace++;
        if(_rooms.counterForHorseRace === _rooms.sockets.length){
            io.in(room).emit('HORSE_START')
            return _rooms.counterForHorseRace
        }else{
            return _rooms.counterForHorseRace
        }
    }
}


/**
 * Validate the winner of the horseGame
 * @param _rooms An object that represents a room from the `rooms` instance variable object
 * @param room A string that represents the roomID
 * @param horseObject auctionObject auctionObject for minigame
 * @param socket A connected socket.io socket
 */
 const validateWinnerRaceMiniGame =(_rooms, room, horseObject, socket)=>{
    if(room === null || horseObject === null || _rooms === null){
        io.to(socket).emit('ERROR', "Error in validateWinnerRaceMiniGame");
    }else{
        if(horseObject.winner === true){
            let players = _rooms.players
            Object.keys(players).forEach((key =>{
                if(players[key].playerIndex === horseObject.horseIndex){
                    players[key].money += 50000
                }else{
                    players[key].money -= 50000
                }
            }))
            socket.to(room).emit('RACE_MOVE', horseObject)
        }else{
            socket.to(room).emit('RACE_MOVE', horseObject)
        }
    }
}


/**
 * Validate the winner
 * @param room A string that represents the roomID
 * @param socket A connected socket.io socket
 */
 const validateWinner =(room, socket)=>{
    if(room === null){
        io.to(socket).emit('ERROR', "Error in validateWinner");
    }else{
        socket.to(room).emit('WINNER', socket.id)
    }
}


app.get('/', (_req, res) =>{
    res.write(`<h1>Socket IO Start on Port : ${PORT}</h1>`)
    res.end();
})

server.listen(PORT, ()=>{
    console.log('Listing on*:3000')
})

io.on('connection', (socket) => {
    console.log(rooms)

    socket.on('JOIN_ROOM', (room) => {
        validateRoom(socket, room, rooms);
    });

    socket.on('ROLE_THE_HIGHEST_DICE', (room, diceCount) =>{
        saveDice(rooms[room], room, socket, diceCount)
    })

    socket.on('CHOSE_STOCKS', (room, stock) =>{
        updateStock(rooms[room], room, socket, stock)
    })

    socket.on('READY_FOR_GAME', (room) =>{
        increaseReadyCounterForRoom(rooms[room],socket, room);
    });

    socket.on('LEAVE_ROOM', () => {
        leaveRooms(socket, rooms);
    });

    socket.on('disconnect', () => {
        leaveRooms(socket, rooms);
    });
    socket.on('disconnecting', () => {
        leaveRooms(socket, rooms);
    });

    socket.on('ROLE_THE_HIGHEST_DICE_AGAIN', (room, diceCount, length) =>{
        saveDice(room, socket, diceCount, length)
    });

    socket.on('ROLE_THE_DICE', (room, diceCount)=>{
        updateDice(rooms[room], room, diceCount, socket)
    })

    socket.on('UPDATE_PLAYER_POSITION', (room, position) =>{
      updatePlayerPosition(rooms[room],room, position, socket)
    })

    socket.on('NEXT_TURN', (room) =>{
        validateNextTurn(rooms[room], room, socket)
    })

    socket.on('LOSE_MONEY', (room, amount) =>{
        playerLoseMoney(rooms[room], room, amount,socket)
    })

    socket.on('GET_MONEY', (room, amount) =>{
        playerGetMoney(rooms[room], room, amount,socket)
    })

    socket.on('PLAYER_COLLISION', (room, collision) =>{
        playerCollision(rooms[room], room, collision, socket)
    })

    socket.on('STOCK', (room, stock) =>{
        stockMiniGame(rooms[room], room, stock, socket)
    })

    socket.on('AUCTION', (room, auctionObject) =>{
        auctionMiniGame(rooms[room], room, auctionObject, socket)
    })

    socket.on('RACE', (room)=>{
        raceMiniGame(rooms[room],room, socket)
    })

    socket.on('RACE_READY', (room)=>{
        validateRaceMiniGame(rooms[room],room)
    })

    socket.on('RACE_MOVE', (room, horseObject) =>{
        validateWinnerRaceMiniGame(rooms[room],room, horseObject, socket)
    })

    socket.on('WINNER', (room)=>{
          validateWinner(room, socket)
    })

});

instrument(io, {
    auth: false
});

module.exports ={
    validateRoom,
    searchEmptyRooms,
    createRoom,
    joinRoom,
    createPlayer,
    stocks,
    leaveRooms,
    increaseReadyCounterForRoom,
    saveDice,
    validateHighestDice,
    resetPlayersDice,
    updateStock,
    updateDice,
    updatePlayerPosition,
    validateNextTurn,
    navObj,
    playerLoseMoney,
    playerGetMoney,
    playerCollision,
    stockMiniGame,
    auctionMiniGame,
    raceMiniGame,
    validateRaceMiniGame,
    validateWinnerRaceMiniGame,
    validateWinner,
    saveDiceProcess


}



