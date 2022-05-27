package com.mankomania.game;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Connection {
    private static final String SERVER = "https://mankomania-backend.herokuapp.com/";
    private static Socket cs;

    private static boolean start = false;
    private static String lobbyID;
    private static String[] players = {"", "", "", ""};

    public static void createConnection(){
        try {
            cs = IO.socket(SERVER);
            cs.connect();

        } catch (URISyntaxException e) {
            System.out.println("Couldn't connect to server");

        }
    }

    private Connection(){
        
    }

    public static Socket getCs() {
        return cs;
    }

    public static void setCs(Socket cs) {
        Connection.cs = cs;
    }

    public static boolean getStart() {
        return start;
    }

    public static void setStart(boolean start) {
        Connection.start = start;
    }

    public static String getLobbyID() {
        return lobbyID;
    }

    public static void setLobbyID(String lobbyID) {
        Connection.lobbyID = lobbyID;
    }

    public static String[] getPlayers() {
        return players;
    }

    public static void setPlayers(String[] players) {
        Connection.players = players;
    }





    public static void joinRoom(Emitter.Listener el) {
        cs.on("join-room", el);
        cs.emit("join-room", "");
    }

    public static void readyForGame(Emitter.Listener el) {
        cs.on("startGame", el);
    }

    public static void closeConnection() {
        cs.emit("leaveRoom", "");
        cs.disconnect();
        cs.off();
        cs.close();
    }

    /**
     * new Methods
     * **/

    public static void emitStocks() {
        cs.emit("CHOSE_STOCKS", "{" +
                "HardSteel_PLC: 2," +
                "hortCircuit_PLC: 0," +
                "DryOil_PLC : 0" +
                "}");
    }

    public static void roleHighestDice (Emitter.Listener el) {
        cs.once("ROLE_THE_HIGHEST_DICE", el);
    }

    public static void emitHighestDice(int dice) {
        cs.emit("ROLE_THE_HIGHEST_DICE", lobbyID, dice);
    }

    public static void roleHighestDiceAgain(Emitter.Listener el) {
        cs.on("ROLE_THE_HIGHEST_DICE_AGAIN", el);
    }
  /* Bekommen:
  [
{ socket: '8oq6z37GMXw4JFijAAAN', dice: 5 },
{ socket: 'Zi_ShZhd61WqfuosAAAJ', dice: 6 }
  ]
   */

    public static void emitHighestDiceAgain() {
        cs.emit("ROLE_THE_HIGHEST_DICE_AGAIN", "[" +
                "  { socket: '8oq6z37GMXw4JFijAAAN', dice: 5 }," +
                "  { socket: 'Zi_ShZhd61WqfuosAAAJ', dice: 6 }" +
                "]");
    }

    public static void startRound(Emitter.Listener el) {
        cs.on("START_ROUND", el);
    }
  /* Bekommen: Gewinner
  {const dice = {
          socket: socket.id,
          dice: diceCount
          }
   }
   */

}
