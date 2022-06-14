const express = require('express')
const app = express();
const http = require('http');
const server = http.createServer(app);
const {Server} = require("socket.io");
const io = new Server(server, { cors: { origin: '*'} });
const PORT = process.env.PORT || 3000;
const { v4: uuidv4 } = require('uuid');
//const { instrument } = require("@socket.io/admin-ui");



/**
 * Instance Variables
 */
const rooms = {};
const START_MONEY = 1000000
const MAX_LOBBY_SIZE = 4
const moneyTransferByPlayerCollision = 10000



/**
 * Check if a private Room was send or a new room should be created or join to an room
 * @param socket A connected socket.io socket
 * @param room Name of the room
 */
function validateRoom(socket, room){
    if(room === ''){
        let roomID = searchEmptyRooms();
        if (roomID === undefined){
            joinRoom(socket, createRoom());
        }else{
            joinRoom(socket,roomID)
        }

    }else{
        //ToDo private Game
    }
}


/**
 * Will create a new Room and returns the room
 * @returns {{ready: number, id: (*|string), sockets: *[], status: boolean}}
 */
function  createRoom () {
    const room = {
        id: uuidv4(), // generate a unique id for the new room, that way we don't need to deal with duplicates.
        status: false,
        sockets: [],
        ready: 0,
        players: {},
        stockCounterFunction : function (room, socket, stock){
            rooms[room].players[socket.id].stocks = stock
            this.counterForStocks++
        },
        counterForStocks : 0,
        counterForDice: 0
    };
    rooms[room.id] = room;
    // have the socket join the room they've just created.
    console.log("Room created " + room.id)
    return room;
}



/**
 * Will search for Empty Rooms and returns a room
 * @returns {room}
 */
function searchEmptyRooms(){
    for (const id in rooms) {
        const room = rooms[id];
        if(room.sockets.length < MAX_LOBBY_SIZE && room.status === false){
            return room;
        }
    }
}
/**
 * Will connect an socket to an specified room
 * @param socket A connected socket.io socket
 * @param room An object that represents a room from the `rooms` instance variable object
 */
function joinRoom(socket, room) {
    //push socket id into socket array in room object
    room.sockets.push(socket.id);

    createPlayer(room, socket.id, stocks())
    //socket joins room
    socket.join(room.id);
    console.log(socket.id, "Joined", room.id);
    io.in(room.id).emit('JOIN_ROOM', room.id, room.sockets)

    //when lobby is full
    if(room.sockets.length === MAX_LOBBY_SIZE){
        room.status = true;
        io.in(room.id).emit('START_GAME', room.id, room.players);
        console.log(rooms[room.id])
        //create a new Room for other players
        createRoom();

    }
}

/**
 * Will create a new player object and insert it into the players object
 * @param room An object that represents a room from the `rooms` instance variable object
 * @param socket An connected socket.io socket
 * @param stocks An object that represents stocks and their default values
 */
const createPlayer = (room,socket, stocks) =>{
   const player = {
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
    //insert into players object
    rooms[room.id].players[socket] = player
}
/**
 * Will return an object with stocks and default value
 * @returns {{ShortCircuit_PLC: number, HardSteel_PLC: number, DryOil_PLC: number}}
 */
const stocks = ()=> {
    return  {
        HardSteel_PLC: 0,
        ShortCircuit_PLC: 0,
        DryOil_PLC : 0
    }
}

/**
 * Will make the socket leave any rooms that it is a part of
 * @param socket A connected socket.io socket
 * @param io io server instance
 */
function leaveRooms(socket, io){
    const roomsToDelete = [];
    // check to see if the socket is in the current room
    for(const id in rooms){
        const room = rooms[id];

        if(room.sockets.includes(socket.id)){
                socket.leave(id)
            // remove the socket from the room object
            room.sockets = room.sockets.filter((item) => item !== socket.id);
            console.log(room.sockets)
            io.in(room.id).emit('JOIN_ROOM', room.id, room.sockets);

        }
        // Prepare to delete any rooms that are now empty
        if (room.sockets.length === 0) {
            roomsToDelete.push(room);
        }
    }
    // Delete all the empty rooms that we found earlier
    for (const room of roomsToDelete) {
        delete rooms[room.id];
    }
}

/**
 * Increase the ready counter for thr room object
 * @param socket A connected socket.io socket
 * @param room room An object that represents a room from the `rooms` instance variable object
 * @param io io server instance
 */
function increaseReadyCounterForRoom(socket, room, io){
    if(rooms[room] !== undefined){
        rooms[room].ready++;
        if(rooms[room].ready === rooms[room].sockets.length && rooms[room].sockets.length >= 2){
            rooms[room].status = true;
            io.in(room).emit('START_GAME', room, rooms[room]);
            console.log("Game starts " + room)
        }
    }else{
        io.to(socket).emit('ERROR', "Couldn't find Room");
    }
}

/**
 * Will save dice1,dice2 and calculate the total dice count
 * @param room  An object that represents a room from the `rooms` instance variable object
 * @param socket A connected socket.io socket
 * @param diceCount A counter to check how many clients have rolled their dice
 * @param winnerLength The length of the winner array
 */
const saveDice = (room, socket, diceCount, winnerLength) =>{
        rooms[room].players[socket.id].calculateDiceCount(diceCount)
        rooms[room].counterForDice++

        if (winnerLength != null){//if we have one or more winner
            if (rooms[room].counterForDice === winnerLength){
                //console.log(rooms[room].players[socket.id])
                validateHighestDice(room)
            }
        }else{
            //if all clients have rolled their dice
            if (rooms[room].counterForDice === rooms[room].sockets.length){
                validateHighestDice(room)
            }
        }
}
/**
 * Will validate the highest Dice count and send the winner/s
 * @param room An object that represents a room from the `rooms` instance variable object
 */
const validateHighestDice = (room) =>{
    //convert players object into an array
    let data = Object.values(rooms[room].players)

    //returns the highest dice count of all players
    let highestDice = Math.max.apply(Math, data.map(function(o) {
        return o.dice_Count;
    }))

    //returns the winner player/s
    let winner = data.filter(x => [x.dice_Count] == highestDice)
    console.log(winner)
    //if we have one winner
    if(winner.length === 1){
        rooms[room].players[winner[0].socket].yourTurn = true;
        io.in(room).emit('START_ROUND', rooms[room].players, winner[0].socket);
        resetPlayersDice(room)
    }
    //if we have to ore more winners
    else{
        io.in(room).emit('ROLE_THE_HIGHEST_DICE_AGAIN', rooms[room].players, winner);
        resetPlayersDice(room)
    }
}
/**
 * Will reset dice_1, dice_2, dice_Count and counterForDice
 * @param room An object that represents a room from the `rooms` instance variable object
 */
const resetPlayersDice = (room) =>{
    Object.values(rooms[room].players).map(a=>a.dice_1= 0);
    Object.values(rooms[room].players).map(a=>a.dice_2 = 0);
    Object.values(rooms[room].players).map(a=>a. dice_Count = 0);
    rooms[room].counterForDice = 0
    console.log(rooms[room])
}

/**
 * Will update the player object with the new stock object
 * @param room An object that represents a room from the `rooms` instance variable object
 * @param socket An connected socket.io socket
 * @param stock An object that represents stocks and their default values
 */
const updateStock = (room, socket, stock)=>{
    //increase the stockCounter
    rooms[room].stockCounterFunction(room, socket, stock)
    //check if all clients have selected their stocks
    if (rooms[room].counterForStocks === rooms[room].sockets.length){
        console.log("Update Stock finished " + rooms[room].sockets)
        io.in(room).emit('ROLE_THE_HIGHEST_DICE', rooms[room].players);
    }
}

/**
 *
 * @param room
 * @param diceCount
 * @param socket
 */
const updateDice = (room, diceCount, socket) =>{
    //check param
    if(room === null || diceCount === null){
        io.to(socket).emit('ERROR', "Error in roleTheDice");
    }else{
        rooms[room].players[socket.id].dice_1 = diceCount[0]
        rooms[room].players[socket.id].dice_1 = diceCount[1]

        if(diceCount[0]  + diceCount[1] === 12){
            rooms[room].players[socket.id].money -= 100000
            socket.to(room).emit('UPDATE_DICE', socket.id, diceCount)
        }else{
            socket.to(room).emit('UPDATE_DICE', socket.id, diceCount)
        }

    }

}

/**
 *
 * @param room
 * @param position
 * @param socket
 */
const updatePlayerPosition = (room, position, socket) =>{
    //check param
    rooms[room].players[socket.id].position = position;
    socket.to(room).emit('UPDATE_PLAYER_POSITION', socket.id, position);
}
/**
 *
 * @param room
 * @param socket
 */
const validateNextTurn = (room, socket) =>{
    rooms[room].players[socket.id].yourTurn = false;
    let nextPlayer = navObj(rooms[room].players, socket.id, 1)
    console.log("Next Player " + nextPlayer)
    rooms[room].players[nextPlayer].yourTurn = true;
    socket.to(room).emit('NEXT_TURN', nextPlayer)
}

/**
 *
 * @param obj
 * @param currentKey
 * @param direction
 * @returns {*}
 */
const navObj = (obj, currentKey, direction) => {
    let next =  (Object.values(obj)[Object.keys(obj).indexOf(currentKey) + direction]);
    if(next !== undefined){
        return next.socket;
    }else{
        return (obj[Object.keys(obj)[0]]).socket
    }
}

/**
 *
 * @param room
 * @param amount
 * @param socket
 */
const playerLoseMoney = (room, amount, socket) =>{
    if(room === null || amount === null ){
        io.to(socket).emit('ERROR', "Error in playerLoseMoney");
    }else{
        console.log("LOSE_MONEY: Room: " + room + " Amount: " + amount + " Socket: " + socket.id);
        rooms[room].players[socket.id].money -= amount;
        socket.to(room).emit('LOSE_MONEY', socket.id, amount);
    }
}
/**
 *
 * @param room
 * @param amount
 * @param socket
 */
const playerGetMoney = (room, amount, socket) =>{
        if(room === null || amount === null ){
            io.to(socket).emit('ERROR', "Error in playerGetMoney");
        }else {
            console.log("GET_MONEY: Room: " + room + " Amount: " + amount + " Socket: " + socket.id);
            rooms[room].players[socket.id].money += amount;
            socket.to(room).emit('GET_MONEY', socket.id, amount);
        }
}
/**
 *
 * @param room
 * @param collision
 * @param socket
 */
 const playerCollision =(room, collision, socket)=> {
    if (room === null || collision === null) {
        io.to(socket).emit('ERROR', "Error in playerCollision");
    } else {
        rooms[room].players[socket.id].money -= moneyTransferByPlayerCollision * (collision.length - 1);
        for (let element of collision) {
            if (element !== socket.id)
                rooms[room].players[element].money += moneyTransferByPlayerCollision;
        }
        socket.to(room).emit('PLAYER_COLLISION', socket.id, collision);
    }
}




app.get('/', (req, res) =>{
    res.write(`<h1>Socket IO Start on Port : ${PORT}</h1>`)
    res.end();
})

server.listen(PORT, ()=>{
    console.log('Listing on*:3000')
})


io.on('connection', (socket) => {
    console.log(rooms)

    socket.on('ROLE_THE_HIGHEST_DICE', (room, diceCount) =>{
        saveDice(room, socket, diceCount)
    })

    socket.on('CHOSE_STOCKS', (room, stock) =>{
        updateStock(room, socket, stock)
    })

    socket.on('JOIN_ROOM', (room) => {
        validateRoom(socket, room, io);
    });

    socket.on('READY_FOR_GAME', (room) =>{
        increaseReadyCounterForRoom(socket, room, io);
    });

    socket.on('LEAVE_ROOM', () => {
        leaveRooms(socket,io);
    });

    socket.on('disconnect', () => {
        console.log('user disconnected');
        leaveRooms(socket,io);
    });
    socket.on('disconnecting', () => {
        console.log('user disconnected');
        leaveRooms(socket,io);
    });

    socket.on('ROLE_THE_HIGHEST_DICE_AGAIN', (room, diceCount, length) =>{
        saveDice(room, socket, diceCount, length)
    });

    socket.on('ROLE_THE_DICE', (room, diceCount)=>{
        updateDice(room, diceCount, socket)
    })

    socket.on('UPDATE_PLAYER_POSITION', (room, position) =>{
      updatePlayerPosition(room, position, socket)
    })

    socket.on('NEXT_TURN', (room) =>{
        validateNextTurn(room, socket)
    })

    socket.on('LOSE_MONEY', (room, amount) =>{
        playerLoseMoney(room, amount,socket)
    })

    socket.on('GET_MONEY', (room, amount) =>{
        playerGetMoney(room, amount,socket)
    })

    socket.on('PLAYER_COLLISION', (room, collision) =>{
        playerCollision(room, collision, socket)
    })




});


/*
instrument(io, {
    auth: false
});
*/





