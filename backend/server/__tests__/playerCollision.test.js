const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'




describe('Test playerCollision function', function(){
    let io, serverSocket, clientSocket;
    let rooms = {};
    let roomID = "TEST_ROOM"
    let colArr;


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
        rooms  ={}
    });

    test('test function playerCollision with invalid collision parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in playerCollision")
        });
        backend.playerCollision(rooms[roomID], roomID, null, serverSocket)
    });

    test('test function playerCollision with invalid room parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in playerCollision")
        });
        backend.playerCollision(rooms[roomID], null, colArr, serverSocket)
    });

    test('test function playerCollision with invalid rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in playerCollision")
        });
        backend.playerCollision(null, roomID, colArr, serverSocket)
    });

    test('test function playerCollision with valid parameters', ()=>{
        clientSocket.on('STOCK', (arg) =>{
            expect(arg).toBe(serverSocket.id, colArr)
        });

        backend.playerCollision(rooms[roomID], roomID, colArr, serverSocket)
    });



    const setup = () =>{
        const room = {
            id: roomID, // generate a unique id for the new room, that way we don't need to deal with duplicates.
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

        const stocks = {
            HardSteel_PLC: 2,
            ShortCircuit_PLC: 2,
            DryOil_PLC : 2
        }

        const player ={
            socket: clientSocket,
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
            socket: "123456789",
            playerIndex: 2,
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
        rooms[room.id].players["123456789"] = player2
        colArr = Object.keys(rooms[roomID].players).filter(type => rooms[roomID].players[type].position === rooms[roomID].players[clientSocket.id].position)

    }


});


