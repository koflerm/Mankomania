
import * as backend from "../server";
import SocketMock from "socket.io-mock";
const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");

describe("Test for saveDiceProcess function", ()=>{
    let io, serverSocket, clientSocket;
    let roomID = "TEST_ROOM"
    let rooms = {};
    let diceCount = [1,1]
    let mockSocket = new SocketMock();

    beforeAll((done) => {
        const httpServer = createServer();
        io = new Server(httpServer);
        httpServer.listen(() => {
            const port = httpServer.address().port;
            clientSocket = new Client(`http://localhost:${port}`);
            io.on("connection", (socket) => {
                serverSocket = socket;
            });
            clientSocket.on("connect", done);
            console.log(clientSocket)
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

    test('test function saveDiceProcess with invalid _rooms object', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in saveDiceProcess")
        });
        backend.saveDiceProcess(null, roomID, serverSocket,  diceCount, 1)
    });

    test('test function saveDiceProcess with invalid room parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in saveDiceProcess")
        });
        backend.saveDiceProcess(rooms[roomID], null, serverSocket,  diceCount, 1)
    });

    test('test function saveDiceProcess with invalid diceCount object', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in saveDiceProcess")
        });
        backend.saveDiceProcess(rooms[roomID], roomID, serverSocket, null,1)
    });



    test('test function saveDiceProcess with valid parameters only one client rolled the dice', ()=>{
        let temp = backend.saveDiceProcess(rooms[roomID], roomID, serverSocket, diceCount,null)
        expect(temp).toBe(1)
    });

    test('test function saveDiceProcess with valid parameters all clients rolled the dice', ()=>{
        backend.saveDiceProcess(rooms[roomID], roomID, serverSocket, diceCount,null)
        let temp = backend.saveDiceProcess(rooms[roomID], roomID, serverSocket, diceCount,null)
        expect(temp).toBe(0)
    });

    test('test function saveDiceProcess with valid parameters two winner', ()=>{
        backend.saveDiceProcess(rooms[roomID], roomID, serverSocket, diceCount,2)
        let temp = backend.saveDiceProcess(rooms[roomID], roomID, serverSocket, diceCount,2)
        expect(temp).toBe(0)
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
            HardSteel_PLC: 2,
            ShortCircuit_PLC: 2,
            DryOil_PLC : 2
        }

        const player ={
            socket: clientSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
            stocks: stocks,
            yourTurn: false,
            dice_1: 1,
            dice_2: 1,
            dice_Count: 0,
            calculateDiceCount : function (diceCount){
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 +  this.dice_2
            }
        }
        rooms[room.id].players[clientSocket.id] = player

        const player2 ={
            socket: mockSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
            stocks: stocks,
            yourTurn: false,
            dice_1: 1,
            dice_2: 1,
            dice_Count: 0,
            calculateDiceCount : function (diceCount){
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 +  this.dice_2
            }
        }
        rooms[room.id].players[mockSocket.id] = player2

    }













})