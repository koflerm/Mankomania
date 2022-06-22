const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'




describe('Test validateWinner function', function(){
    let io, serverSocket, clientSocket;

    beforeAll((done) => {
        const httpServer = createServer();
        io = new Server(httpServer);
        httpServer.listen(() => {
            const port = httpServer.address().port;
            clientSocket = new Client(`http://localhost:${port}`);
            io.on("connection", (socket) => {
                serverSocket = socket;
                clientSocket.join("TEST_ROOM")

            });
            clientSocket.on("connect", done);
        });
    });

    afterAll(() => {
        io.close();
        clientSocket.close();
    });

    test('test function validateWinner with valid room parameters', ()=>{
       clientSocket.on('WINNER', (arg) =>{
          expect(arg).toBe(serverSocket.id)
       });
       backend.validateWinner("TEST_ROOM", serverSocket)
    });

    test('test function validateWinner with invalid room parameters', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in validateWinner")
        });
        backend.validateWinner(null, serverSocket)
    });

    test('test socket.on(WINNER)', (done)=>{
      serverSocket.on('WINNER', (cb) =>{
          cb("TEST_ROOM")
      });
      clientSocket.emit('WINNER', (arg)=>{
          expect(arg).toBe("TEST_ROOM");
          done()
      })
    });

    test('test socket.on(WINNER) but with wrong arg', (done)=>{
        serverSocket.on('WINNER', (cb) =>{
            cb("TEST_ROOM")
        });
        clientSocket.emit('WINNER', (arg)=>{
            expect(arg).not.toBe("TEST_ROO");
            done()
        })
    });

});
