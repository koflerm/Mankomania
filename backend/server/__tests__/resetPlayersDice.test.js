const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'
import MockedSocket from 'socket.io-mock';


describe('Test resetPlayersDice function', function(){
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

    test('test function resetPlayersDice with invalid _rooms parameter', ()=>{
        let temp = backend.resetPlayersDice(null)
        expect(temp).toBeNull()
    });

    test('test function resetPlayersDice with valid parameter', ()=>{
        let temp = backend.resetPlayersDice(rooms[roomID])
        rooms[roomID].players.dice_Count = 0
        rooms[roomID].players.dice_1 = 0
        rooms[roomID].players.dice_2 = 0
        rooms[roomID].counterForDice = 0

        expect(temp).toStrictEqual(rooms[roomID])
    });



    const setup = () =>{
        const room = {
            id: roomID, // generate a unique id for the new room, that way we don't need to deal with duplicates.
            status: false,
            sockets: [clientSocket.id],
            ready: 0,
            players: {},
            stockCounterFunction : function (room, socket, stock){
                rooms[room].players[socket.id].stocks = stock
                this.counterForStocks++
            },
            counterForStocks : 0,
            counterForDice: 1,
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
            dice_1: 2,
            dice_2: 2,
            dice_Count: 4,
            calculateDiceCount: function (diceCount) {
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 + this.dice_2
            }
        }



    }


});


