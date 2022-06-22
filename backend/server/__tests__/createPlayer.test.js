import * as server from '../server.js'
import {createServer} from "http";
import {Server} from "socket.io";
import Client from "socket.io-client";


describe("Test for createPlayer function", ()=>{
    let io, serverSocket, clientSocket;
    let player, room, stocks;


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

    test("connecting to server", (done) => {
        clientSocket.on("hello", (arg) => {
            expect(arg).toBe("world");
            done();
        });
        serverSocket.emit("hello", "world");
    });


    test("Test to see if the function return the right player object", () => {
        setup()
        expect(server.createPlayer(room, clientSocket, stocks)).toMatchObject(player);
    });

    test("Test to see if the function return not the right player object (playerIndex changed)" , () => {
        setup()
        expect(server.createPlayer(room, clientSocket, stocks)).not.toMatchObject(player);
    });


    const setup = () =>{
        const rooms = {}
        const id = "11bf5b37-e0b8-42e0-8dcf-dc8c4aefc000"
         room = {
            id: id,
            status: false,
            sockets: [clientSocket.id],
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

        const START_MONEY = 1000000

         stocks = {
            HardSteel_PLC: 0,
            ShortCircuit_PLC: 0,
            DryOil_PLC : 0
        }
         player =   {
            socket: clientSocket,
            playerIndex: room.sockets.length-1,
            money: START_MONEY,
            position: room.sockets.length - 1,
            stocks: stocks,
            yourTurn: false,
            dice_1: 0,
            dice_2: 0,
            dice_Count: 0,
        }
    }
})