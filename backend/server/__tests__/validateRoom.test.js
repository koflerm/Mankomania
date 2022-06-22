
import * as server from '../server.js'
import MockedSocket from "socket.io-mock";
const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");


describe("Test for searchEmptyRooms function", ()=>{
    let io, serverSocket, clientSocket;
    let roomID = "TEST_ROOM"
    let rooms = {};

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


    test("validateRoom function with an empty room attribute", () => {
        const room = "Raum"
        const socket = ""
        expect(server.validateRoom(socket, room)).toBeNull();
    });

    test("validateRoom function with not empty room attribute and roomID not undefined", () => {
        setup()
        expect(server.validateRoom(serverSocket, "", rooms)).toBe(roomID);
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
            counterForDice: 0,
            counterForHorseRace: 0
        };
        rooms[room.id] = room;



    }
})
