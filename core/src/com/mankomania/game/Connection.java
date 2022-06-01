package com.mankomania.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import playerLogic.Player;

public class Connection {
    private static final String SERVER = "https://mankomania-backend.herokuapp.com/";
    private static Socket cs;

    private static boolean start = false;
    private static String lobbyID;
    private static String[] players = {"", "", "", ""};

    public static void createConnection() {
        try {
            cs = IO.socket(SERVER);
            cs.connect();

        } catch (URISyntaxException e) {
            System.out.println("Couldn't connect to server");

        }
    }

    private Connection() {

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
        cs.on("JOIN_ROOM", el);
        cs.emit("JOIN_ROOM", "");
    }

    public static void startGame(Emitter.Listener el) {
        cs.on("START_GAME", el);
    }

    public static void closeConnection() {
        cs.emit("leaveRoom", "");
        cs.disconnect();
        cs.off();
        cs.close();
    }

    /**
     * new Methods
     **/

    public static void emitStocks() {
        cs.emit("CHOSE_STOCKS", lobbyID, "{ HardSteel_PLC: 2, ShortCircuit_PLC: 0, DryOil_PLC: 0 }");
    }

    public static void roleHighestDice(Emitter.Listener el) {
        cs.once("ROLE_THE_HIGHEST_DICE", el);
    }

    public static void emitHighestDice(int dice[]) {
        cs.emit("ROLE_THE_HIGHEST_DICE", lobbyID, dice);
    }

    public static void roleHighestDiceAgain(Emitter.Listener el) {
        cs.on("ROLE_THE_HIGHEST_DICE_AGAIN", el);
    }

    public static void emitHighestDiceAgain() {
        cs.emit("ROLE_THE_HIGHEST_DICE_AGAIN", "[" +
                "  { socket: '8oq6z37GMXw4JFijAAAN', dice: 5 }," +
                "  { socket: 'Zi_ShZhd61WqfuosAAAJ', dice: 6 }" +
                "]");
    }

    public static void startRound(Emitter.Listener el) {
        cs.on("START_ROUND", el);
    }

    public static void convertJsonToPlayer(String args) {

        /**
         * Split Player-Array in multiple Player Strings
         */

        String[] players = args.split("},", 10);

        for (int i = 0; i < players.length; i++) {

            String temp = players[i];

            String newString = "";

            if (i == 0) {
                //Ersten 24 Zeichen entfernen
                //} hinten hinzufügen

                newString = temp.substring(24);
                newString += "}";
                players[i] = newString;
            } else if (i < (players.length - 1)) {
                //Ersten 23 Zeichen entfernen
                //} hinten hinzufügen

                newString = temp.substring(23);
                newString += "}";

                players[i] = newString;

            } else if (i == (players.length - 1)) {
                //Ersten 23 Zeichen entfernen
                //Hinten entfernen: }

                newString = temp.substring(23);
                newString = newString.substring(0, newString.length() - 1);
                players[i] = newString;
            }

        }

        /**
         * Parser for Stocks
         */

        ArrayList<String> stockAsString = new ArrayList<String>();


        for (int i = 0; i < players.length; i++) {
            String[] stock = players[i].split(":\\{");

            String newStock = stock[1];

            String newNewStock = "{";

            newNewStock += newStock;

            String finalStock = newNewStock.substring(0, newNewStock.length() - 1);

            stockAsString.add(finalStock);
        }


        /**
         * Parse Player-Strings into Con-Players
         */

        ArrayList<String> allPlayersAsString = new ArrayList<String>();

        for(String s : players){
            allPlayersAsString.add(s);
        }

        ArrayList<ConPlayer> cp = new ArrayList<ConPlayer>();

        for (String p : allPlayersAsString) {

            JsonObject jsonObject = JsonParser.parseString(p).getAsJsonObject();

            ConPlayer temp = new ConPlayer();

            temp.setDice_2(jsonObject.get("dice_2").getAsInt());

            temp.setDice_1(jsonObject.get("dice_1").getAsInt());

            temp.setMoney(jsonObject.get("money").getAsInt());

            temp.setYouTurn(jsonObject.get("yourTurn").getAsBoolean());

            temp.setSocket(jsonObject.get("socket").getAsString());

            temp.setPosition(jsonObject.get("position").getAsInt());

            temp.setDice_Count(jsonObject.get("dice_Count").getAsInt());

            temp.setPlayerIndex(jsonObject.get("playerIndex").getAsInt());

            cp.add(temp);

        }

        /**
         * Parse Stocks into ConPlayer Object
         */

        for (int i = 0; i < stockAsString.size(); i++) {

            JsonObject jsonStock = JsonParser.parseString(stockAsString.get(i)).getAsJsonObject();

            cp.get(i).setHARD_STEEL_PLC(jsonStock.get("HardSteel_PLC").getAsInt());
            cp.get(i).setSHORT_CIRCUIT_PLC(jsonStock.get("ShortCircuit_PLC").getAsInt());
            cp.get(i).setDRY_OIL_PLC(jsonStock.get("DryOil_PLC").getAsInt());
        }


        /**
         * Create real Player-Objects
         */

        for (ConPlayer c : cp) {

            Player p = new Player();

            p.setPlayerIndex(c.getPlayerIndex());
            p.setPlayerSocketID(c.getSocket());
            p.setMoney(c.getMoney());

            //Field
            p.setCurrentFieldPosition(MankomaniaGame.getInstance().getBoard().getFieldByIndex(c.getPosition()));

            //Stocks
            p.setShares(c.getHARD_STEEL_PLC(), c.getSHORT_CIRCUIT_PLC(), c.getDRY_OIL_PLC());


            //Problem
            //MankomaniaGame.getInstance().getBoard().addPlayer(p);

            System.out.println(p.toString());

        }
    }
}
