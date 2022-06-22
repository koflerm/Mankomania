
import * as server from '../server.js'
const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");


describe("Test for searchEmptyRooms function", ()=>{
    let io, serverSocket, clientSocket;

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

    test("check if the object is the same", () => {
       const temp = {
           HardSteel_PLC: 0,
           ShortCircuit_PLC: 0,
           DryOil_PLC : 0
       }
        expect(server.stocks()).toEqual(temp);
    });

    test("check if the object is not the same", () => {
        const temp = {
            HardSteel_PLC: 1,
            ShortCircuit_PLC: 0,
            DryOil_PLC : 0
        }
        expect(server.stocks()).not.toEqual(temp);
    });


})
