
import * as backend from "../server";
const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");

describe("Test for validateWinnerRaceMiniGame function", ()=>{
    let io, serverSocket, clientSocket;
    let roomID = "TEST_ROOM"
    let rooms = {};
    let horseObject = {
        horseIndex: 1,
        movedSteps: 2,
        winner: false
    }


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

    test('test function validateWinnerRaceMiniGame with invalid room parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in validateWinnerRaceMiniGame")
        });
        backend.validateWinnerRaceMiniGame(rooms[roomID],null, horseObject, serverSocket)
    });

    test('test function validateWinnerRaceMiniGame with invalid rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in validateWinnerRaceMiniGame")
        });
        backend.validateWinnerRaceMiniGame(null,roomID, horseObject, serverSocket)
    });

    test('test function validateWinnerRaceMiniGame with invalid horseObject parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in validateWinnerRaceMiniGame")
        });
        backend.validateWinnerRaceMiniGame(rooms[roomID],roomID, null, serverSocket)
    });



    test('test function validateWinnerRaceMiniGame with valid parameters winner = false', ()=>{
        clientSocket.on('RACE_MOVE', (arg) =>{
            expect(arg).toMatchObject(horseObject)
        });
        backend.validateWinnerRaceMiniGame(rooms[roomID],roomID, horseObject, serverSocket)
    });

    test('test function validateWinnerRaceMiniGame with valid parameters winner = false', ()=>{
        horseObject.winner = true
        clientSocket.on('RACE_MOVE', (arg) =>{
            expect(arg).toMatchObject(horseObject)
        });
        backend.validateWinnerRaceMiniGame(rooms[roomID],roomID, horseObject, serverSocket)
    });

    test('test function validateWinnerRaceMiniGame with valid parameters winner = false', ()=>{
        horseObject.winner = true
        horseObject.horseIndex = 4
        clientSocket.on('RACE_MOVE', (arg) =>{
            expect(arg).toMatchObject(horseObject)
        });
        backend.validateWinnerRaceMiniGame(rooms[roomID],roomID, horseObject, serverSocket)
    });









    const setup = () =>{
        const room = {
            id: roomID, // generate a unique id for the new room, that way we don't need to deal with duplicates.
            status: false,
            sockets: [clientSocket.id, serverSocket.id],
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
            dice_1: 0,
            dice_2: 0,
            dice_Count: 0,
            calculateDiceCount : function (diceCount){
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 +  this.dice_2
            }
        }
        rooms[room.id].players[clientSocket.id] = player

        const player2 ={
            socket: serverSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
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
        rooms[room.id].players[serverSocket.id] = player2

    }

})