
import * as backend from "../server";
const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");

describe("Test for joinRoom function", ()=>{
    let io, serverSocket, clientSocket;
    let roomID = "TEST_ROOM"
    let rooms = {};
    let room;

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

    test('test function joinRoom with invalid _rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in joinRoom")
        });
        backend.joinRoom(serverSocket, room, null)
    });

    test('test function joinRoom with invalid _rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toStrictEqual("Error in joinRoom")
        });
        backend.joinRoom(serverSocket, null, rooms)
    });

    test('test function joinRoom with valid  parameters lobby is not full', ()=>{
        clientSocket.on('JOIN_ROOM', (arg) =>{
            expect(arg).toStrictEqual(roomID, room.sockets)
        });
        backend.joinRoom(serverSocket, room, rooms)
    });

    test('test function joinRoom with valid  parameters lobby is not full', ()=>{
        let temp = backend.joinRoom(serverSocket, room, rooms)
        expect(temp).toBe(room.id)
    });

    test('test function joinRoom with valid  parameters lobby is not full', ()=>{
        clientSocket.on('START_GAME', (arg) =>{
            expect(arg).toStrictEqual(roomID, room.players)
        });
        backend.joinRoom(serverSocket, room, rooms)
        backend.joinRoom(serverSocket, room, rooms)
        backend.joinRoom(serverSocket, room, rooms)
        backend.joinRoom(serverSocket, room, rooms)

    });

    const setup = () =>{
         room = {
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



    }













})