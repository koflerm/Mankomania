const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'




describe('Test stockMiniGame function', function(){
    let io, serverSocket, clientSocket;
    let rooms = {};
    let roomID = "TEST_ROOM"
    let stock = {
        stockName: "ShortCircuit_PLC",
        status: true
    }

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

    test('test function stockMiniGame with invalid stock parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in stockMiniGame")
        });
        backend.stockMiniGame(rooms[roomID], roomID, null, serverSocket)
    });

    test('test function stockMiniGame with invalid room parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in stockMiniGame")
        });
        backend.stockMiniGame(rooms[roomID], null, stock, serverSocket)
    });

    test('test function stockMiniGame with invalid rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in stockMiniGame")
        });
        backend.stockMiniGame(null, roomID, stock, serverSocket)
    });

    test('test function stockMiniGame with valid parameters stock status === true', ()=>{
        setup()
        clientSocket.on('STOCK', (arg) =>{
            expect(arg).toBe(serverSocket.id, stock)
        });

        backend.stockMiniGame(rooms[roomID], roomID, stock, serverSocket)
    });

    test('test function stockMiniGame with valid parameters stock status === false', ()=>{
        setup()
        let stock2 = {
            stockName: "ShortCircuit_PLC",
            status: false
        }
        clientSocket.on('STOCK', (arg) =>{
            expect(arg).toBe(serverSocket.id, stock2)
        });

        backend.stockMiniGame(rooms[roomID], roomID, stock2, serverSocket)
    });

    test('test socket.on(STOCK)', (done)=>{
        serverSocket.on('STOCK', (cb) =>{
            cb(roomID)

        });
        clientSocket.emit('STOCK', (arg)=>{
            expect(arg).toBe(roomID);
            done()
        })
    });

    test('test socket.on(STOCK)  but with wrong arg', (done)=>{
        serverSocket.on('STOCK', (cb) =>{
            cb(roomID)

        });
        clientSocket.emit('STOCK', (arg)=>{
            expect(arg).not.toBe("TEST");
            done()
        })
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
            playerIndex: room.sockets.length,
            money: 1000000,
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
        rooms[room.id].players[clientSocket.id] = player
    }


});


