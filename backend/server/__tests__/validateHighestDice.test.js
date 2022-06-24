
import {createServer} from "http";
import {Server} from "socket.io";
import Client from "socket.io-client";
import * as backend from "../server";
import MockedSocket from "socket.io-mock";


describe("Test for validateHighestDice function", ()=>{
    let io, serverSocket, clientSocket;
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



            });
            clientSocket.on("connect", done);


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

    test('test function validateHighestDice with invalid _rooms object', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in validateHighestDice")
        });
        backend.validateHighestDice(null, roomID)
    });

    test('test function validateHighestDice with invalid room parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in validateHighestDice")
        });
        backend.validateHighestDice(rooms[roomID], null)
    });

    test('test function validateHighestDice with valid parameters one winner', ()=>{
        clientSocket.on('START_ROUND', (arg) =>{
            expect(arg).toBe(rooms[roomID].players, mockSocket.id)
        });
        backend.validateHighestDice(rooms[roomID], roomID)
    });

    test('test function validateHighestDice with valid parameters one winner', ()=>{
        let temp = backend.validateHighestDice(rooms[roomID], roomID)
        expect(temp).toBe(mockSocket.id)
    });

    test('test function validateHighestDice with valid parameters one winner', ()=>{
        let temp = backend.validateHighestDice(rooms[roomID], roomID)
        expect(temp).not.toBe(clientSocket.id)
    });

    test('test function validateHighestDice with valid parameters two winner', ()=>{
        rooms[roomID].players[clientSocket.id].dice_1 = 5
        rooms[roomID].players[clientSocket.id].dice_2 = 5
        rooms[roomID].players[clientSocket.id].dice_Count = 10
        let winner = [
            rooms[roomID].players[clientSocket.id],
            rooms[roomID].players[mockSocket.id]
        ]

        clientSocket.on('ROLE_THE_HIGHEST_DICE_AGAIN', (arg) =>{
            expect(arg).toBe(rooms[roomID].players, winner)
        });
        backend.validateHighestDice(rooms[roomID], roomID)
    });

    test('test function validateHighestDice with valid parameters one winner', ()=>{
        rooms[roomID].players[clientSocket.id].dice_1 = 5
        rooms[roomID].players[clientSocket.id].dice_2 = 5
        rooms[roomID].players[clientSocket.id].dice_Count = 10
        let winner = [
            rooms[roomID].players[clientSocket.id],
            rooms[roomID].players[mockSocket.id]
        ]
        let temp = backend.validateHighestDice(rooms[roomID], roomID)
        expect(temp).toMatchObject(winner)
    });

    test('test function validateHighestDice with valid parameters one winner', ()=>{
        rooms[roomID].players[clientSocket.id].dice_1 = 5
        rooms[roomID].players[clientSocket.id].dice_2 = 5
        rooms[roomID].players[clientSocket.id].dice_Count = 10
        let winner = [
            rooms[roomID].players[clientSocket.id],
            rooms[roomID].players[mockSocket.id]
        ]
        let temp = backend.validateHighestDice(rooms[roomID], roomID)
        expect(temp).not.toBe({})
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
            dice_1: 4,
            dice_2: 4,
            dice_Count: 8,
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
            dice_1: 5,
            dice_2: 5,
            dice_Count: 10,
            calculateDiceCount : function (diceCount){
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 +  this.dice_2
            }
        }
        rooms[room.id].players[mockSocket.id] = player2

    }
})