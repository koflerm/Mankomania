const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'
import MockedSocket from 'socket.io-mock';


describe('Test increaseReadyCounterForRoom function', function(){
    let io, serverSocket, clientSocket
    let roomID = "TEST_ROOM"
    let rooms = {};
    let mockSocket = new MockedSocket();




    beforeAll((done) => {
        const httpServer = createServer();
        io = new Server(httpServer);
        httpServer.listen(() => {
            const port = httpServer.address().port;
            clientSocket = new Client(`http://localhost:${port}`);
            io.on("connection", (socket) => {
                serverSocket = socket;
                clientSocket.join(roomID)
            });
            clientSocket.on("connect", done);
            mockSocket.on("connect", done);

        });

    });


    afterAll(() => {
        io.close();
        clientSocket.close();
    });

    beforeEach(() => {
        setup()
    });
    afterEach(() => {
        rooms = {}
    });

    test('test function increaseReadyCounterForRoom with invalid _rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in increaseReadyCounterForRoom")
        });
        backend.increaseReadyCounterForRoom(null, serverSocket, roomID)
    });

    test('test function increaseReadyCounterForRoom with invalid rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in increaseReadyCounterForRoom")
        });
        backend.increaseReadyCounterForRoom(rooms[roomID], serverSocket, null)
    });


    test('test function increaseReadyCounterForRoom with valid parameters one player ready', ()=>{
        let temp = backend.increaseReadyCounterForRoom(rooms[roomID],serverSocket, roomID)
        expect(temp).toBe(1)
    });

    test('test function increaseReadyCounterForRoom with valid parameters one player ready', ()=>{
        let temp = backend.increaseReadyCounterForRoom(rooms[roomID],serverSocket, roomID)
        expect(temp).not.toBe(0)
    });

    test('test function increaseReadyCounterForRoom with valid parameters one player ready', ()=>{
        backend.increaseReadyCounterForRoom(rooms[roomID],serverSocket, roomID)
        let temp = backend.increaseReadyCounterForRoom(rooms[roomID],serverSocket, roomID)
        expect(temp).toBe(true)
    });

    test('test function increaseReadyCounterForRoom with valid parameters two player ready', ()=>{
        backend.increaseReadyCounterForRoom(rooms[roomID],serverSocket, roomID)
        let temp = backend.increaseReadyCounterForRoom(rooms[roomID],serverSocket, roomID)
        expect(temp).not.toBe(false)
    });

    test('test function increaseReadyCounterForRoom with valid rooms parameter', ()=>{
        clientSocket.on('START_GAME', (arg) =>{
            expect(arg).toBe(roomID,rooms[roomID]  )
        });
        backend.increaseReadyCounterForRoom(rooms[roomID], serverSocket, roomID)
        backend.increaseReadyCounterForRoom(rooms[roomID], serverSocket, roomID)
    });


    const setup = () =>{
        const room = {
            id: roomID, // generate a unique id for the new room, that way we don't need to deal with duplicates.
            status: false,
            sockets: [clientSocket.id, mockSocket.id],
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

        const stocks = {
            HardSteel_PLC: 0,
            ShortCircuit_PLC: 0,
            DryOil_PLC : 0
        }



        rooms[room.id].players[clientSocket.id] = {
            socket: clientSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
            stocks: stocks,
            yourTurn: false,
            dice_1: 0,
            dice_2: 0,
            dice_Count: 0,
            calculateDiceCount: function (diceCount) {
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 + this.dice_2
            }
        }

        rooms[room.id].players[mockSocket.id] = {
            socket: mockSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
            stocks: stocks,
            yourTurn: false,
            dice_1: 0,
            dice_2: 0,
            dice_Count: 0,
            calculateDiceCount: function (diceCount) {
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 + this.dice_2
            }
        }


    }


});


