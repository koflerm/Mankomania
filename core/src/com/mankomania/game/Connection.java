package com.mankomania.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.tools.javac.code.Attribute;

import java.net.URISyntaxException;
import java.util.ArrayList;

import fieldLogic.Field;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import playerLogic.Player;

public class Connection {
    private static final String SERVER = "https://mankomania.herokuapp.com/";
    private static Socket cs;

    private static boolean start = false;
    private static String lobbyID;
    private static String[] players = {"", "", "", ""};
    private static ArrayList<ConPlayer> cp = new ArrayList<ConPlayer>();

    private static String winners[];

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

    //Aufrufen
    public static void emitStocks(int hardSteel, int shortCircuit, int dryOil) {
        cs.emit("CHOSE_STOCKS", lobbyID, "{ HardSteel_PLC: " + hardSteel + ", ShortCircuit_PLC: " + shortCircuit + ", DryOil_PLC: " + dryOil + " }");
        System.out.println("Emit Stocks");
    }

    //Aufrufen
    public static void roleHighestDice(Emitter.Listener el) {
        cs.once("ROLE_THE_HIGHEST_DICE", el);
        System.out.println("Listen for diceRoll Event");
    }

    //Aufrufen
    public static void emitHighestDice(int dice1, int dice2) {
        cs.emit("ROLE_THE_HIGHEST_DICE", lobbyID, "[" + dice1 + ", " + dice2 + "]");
        System.out.println("Emit dices");
    }

    //Aufrufen
    //Case Two ore more Winners The server will send an event socket.on('ROLE_THE_HIGHEST_DICE_AGAIN',
    // (data, winner) data = players object, winner = winner Array. Loop the array and check if the
    // socketID equals your id, if this is true: roll the dice again and emit to the server
    public static void roleHighestDiceAgain(Emitter.Listener el) {
        cs.on("ROLE_THE_HIGHEST_DICE_AGAIN", el);
        System.out.println("Listen for dice roll again");
    }

    //Aufrufen
    public static void emitHighestDiceAgain(int dice1, int dice2) {
        cs.emit("ROLE_THE_HIGHEST_DICE_AGAIN", lobbyID, "[" + dice1 + ", " + dice2 + "]", winners.length);
        System.out.println("Emits dice roll again");
    }

    //Aufrufen
    //Case One Winner The server will send an event socket.on('START_ROUND', (data, winner)
    // data= players Object; winner = winner ID Round will start (winner index in currentPlayer speichern)
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

        for (String s : players) {
            allPlayersAsString.add(s);
        }

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

    }

    /**
     * Create real Player-Objects Initial
     */

    public static ArrayList<Player> convertConPlayersToPlayersInitial() {
        ArrayList<Player> pList = new ArrayList<Player>();

        for (ConPlayer c : cp) {

            Field f = MankomaniaGame.getInstance().getBoard().getFieldByIndex(c.getPosition());

            Player p = new Player(f, c.getPlayerIndex(), c.getSocket());

            //Stocks
            //p.setShares(c.getHARD_STEEL_PLC(), c.getSHORT_CIRCUIT_PLC(), c.getDRY_OIL_PLC());

            pList.add(p);

        }

        return pList;
    }

    /**
     * Create real Player-Objects at Update
     */

    public static ArrayList<Player> convertConPlayersToPlayersUpdate() {
        ArrayList<Player> pList = new ArrayList<Player>();

        for (ConPlayer c : cp) {

            Field f = MankomaniaGame.getInstance().getBoard().getFieldByIndex(c.getPosition());

            Player p = new Player(f, c.getPlayerIndex(), c.getSocket());

            p.setMoney(c.getMoney());

            p.setShares(c.getHARD_STEEL_PLC(), c.getSHORT_CIRCUIT_PLC(), c.getDRY_OIL_PLC());

            pList.add(p);

        }

        return pList;
    }

}
