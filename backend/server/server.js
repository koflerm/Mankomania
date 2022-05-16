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



/**
 * Check if a private Room was send or a new room should be created or join to an room
 * @param socket A connected socket.io socket
 * @param room Name of the room
 * @param io server instance
 */
function validateRoom(socket, room, io){
    if(room === ''){
        let roomID = searchEmptyRooms();
        if (roomID === undefined){
            joinRoom(socket, createRoom(), io);

        }else{
            joinRoom(socket,roomID, io)
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
        players: [],
        stockCounterFunction : function (room, socket, stock){
            rooms[room].players[socket.id].stocks = stock
            this.counterForStocks++
        },
        counterForStocks : 0,
        dice: []
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
    console.log("Search")
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
 * @param io server instance
 */
function joinRoom(socket, room, io) {
    //rooms[room.id] = room;
    room.sockets.push(socket.id);
    //room.players.push(createPlayer(room, socket.id, stocks()))
    room.players[socket.id] = createPlayer(room, socket.id, stocks())
    socket.join(room.id);
    console.log(socket.id, "Joined", room.id);
    io.in(room.id).emit('join-room', room.id, room.sockets)

    if(room.sockets.length === MAX_LOBBY_SIZE){
        room.status = true;
        console.log(room.id + " is full")
        io.in(room.id).emit('startGame', room.id, room.players);
        createRoom();

    }
}
const createPlayer = (room,socket, stocks) =>{
    return {
        socket: socket,
        playerIndex: room.players.length + 1,
        money: START_MONEY,
        position: room.players.length + 1,
        stocks: stocks,
        diceCount: 0
    }
}


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
            io.in(room.id).emit('join-room', room.id, room.sockets);

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
            io.in(room).emit('startGame', room, rooms[room]);
            console.log("Game starts " + room)
        }
    }else{
        io.to(socket).emit('error', "Couldn't find Room");
    }
}
const saveDice = (room, socket, diceCount)=>{
    const dice = {
        socket: socket.id,
        dice: diceCount
    }
    rooms[room].dice.push(dice)
    if (rooms[room].dice.length === rooms[room].sockets.length){
        validateHighestDice(rooms[room].dice)
    }
}

const validateHighestDice = (data) =>{
    let highestDice =  Math.max.apply(Math, data.map(function(o) {
        return o.dice;
    }))

    let winner = data.filter(x => [x.dice] == highestDice)
    console.log(winner)
    if(winner.length === 1){
        //if we have one winner
    }else{
        //2 or more winners
        winner.forEach(o =>{
            console.log(o.socket)
        })

    }

}


const updateStock = (room, socket, stock)=>{
    rooms[room].stockCounterFunction(room, socket, stock)
    if (rooms[room].counterForStocks === rooms[room].sockets.length){
        io.in(room).emit('ROLE_THE_DICE');
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

    socket.on('choose_stocks', (room, stock) =>{
        updateStock(room, socket, stock)
    })

    socket.on('join-room', (room) => {
        validateRoom(socket, room, io);
    });

    socket.on('readyForGame', (room) =>{
        increaseReadyCounterForRoom(socket, room, io);
    });

    socket.on('leaveRoom', () => {
        leaveRooms(socket,io);
    });

    socket.on('disconnect', () => {
        console.log('user disconnected');
        leaveRooms(socket,io);

    });
});


/*
instrument(io, {
    auth: false
});
*/





