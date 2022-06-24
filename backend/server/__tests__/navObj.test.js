const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'
import MockedSocket from 'socket.io-mock';



describe('Test navObj function', function(){
    let io, serverSocket, clientSocket
    let roomID = "TEST_ROOM"
    let players;
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
        players = {}
    });

    test('test function navObj with invalid obj parameter', ()=>{
        let temp = backend.navObj(null, clientSocket, 1)
        expect(temp).toBeNull()
    });

    test('test function navObj with invalid currentKey parameter', ()=>{
        let temp = backend.navObj(players, null, 1)
        expect(temp).toBeNull()
    });

    test('test function navObj with invalid direction', ()=>{
        let temp = backend.navObj(players, clientSocket, null)
        expect(temp).toBeNull()
    });

    test('test function navObj with invalid direction greater 1', ()=>{
        let temp = backend.navObj(null, clientSocket, 2)
        expect(temp).toBeNull()
    });
    test('test function navObj with valid parameters first player index in players object', ()=>{
        let temp = backend.navObj(players, players["pTXmcVIA2UV_r7hJAAAJ"], 1)
        expect(temp).toMatch("dCwQokTNxDT7YCC6AAAL")
    });

    test('test function navObj with valid parameters first player index in players object', ()=>{
        let temp = backend.navObj(players, players["pTXmcVIA2UV_r7hJAAAJ"], 1)
        expect(temp).not.toEqual("")
    });

    test('test function navObj with valid parameters first player index in players object', ()=>{
        let temp = backend.navObj(players, players["y94JPlkimCvbTaE1AAAP"], 1)
        expect(temp).toMatch("pTXmcVIA2UV_r7hJAAAJ")
    });




    const setup = () =>{
         players = {
            "pTXmcVIA2UV_r7hJAAAJ": {
                "socket": "pTXmcVIA2UV_r7hJAAAJ",
                "playerIndex": 1,
                "money": 1000000,
                "position": 0,
                "stocks": {
                    "HardSteel_PLC": 2,
                    "ShortCircuit_PLC": 0,
                    "DryOil_PLC": 0
                },
                "yourTurn": true,
                "dice_1": 6,
                "dice_2": 6,
                "dice_Count": 12
            },
            "dCwQokTNxDT7YCC6AAAL": {
                "socket": "dCwQokTNxDT7YCC6AAAL",
                "playerIndex": 2,
                "money": 1000000,
                "position": 1,
                "stocks": {
                    "HardSteel_PLC": 2,
                    "ShortCircuit_PLC": 0,
                    "DryOil_PLC": 0
                },
                "yourTurn": false,
                "dice_1": 6,
                "dice_2": 1,
                "dice_Count": 7
            },
            "0boePgPUzTS9U-kYAAAN": {
                "socket": "0boePgPUzTS9U-kYAAAN",
                "playerIndex": 3,
                "money": 1000000,
                "position": 2,
                "stocks": {
                    "HardSteel_PLC": 2,
                    "ShortCircuit_PLC": 0,
                    "DryOil_PLC": 0
                },
                "yourTurn": false,
                "dice_1": 6,
                "dice_2": 5,
                "dice_Count": 11
            },
            "y94JPlkimCvbTaE1AAAP": {
                "socket": "y94JPlkimCvbTaE1AAAP",
                "playerIndex": 4,
                "money": 1000000,
                "position": 10,
                "stocks": {
                    "HardSteel_PLC": 2,
                    "ShortCircuit_PLC": 0,
                    "DryOil_PLC": 0
                },
                "yourTurn": false,
                "dice_1": 1,
                "dice_2": 2,
                "dice_Count": 3
            }
        }

    }


});


