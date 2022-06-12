package com.mankomania.game;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

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
    private static ArrayList<ConPlayer> cp = new ArrayList<>();

    private static String[] winners;

    private static boolean roleHighestDice = false;
    private static boolean update = false;
    private static boolean yourTurn = false;

    private static Player currentPlayer;

    public static boolean isYourTurn() {
        return yourTurn;
    }

    public static void setYourTurn(boolean yourTurn) {
        Connection.yourTurn = yourTurn;
    }

    public static boolean isRoleHighestDice() {
        return roleHighestDice;
    }

    public static void setRoleHighestDice(boolean roleHighestDice) {
        Connection.roleHighestDice = roleHighestDice;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        Connection.currentPlayer = currentPlayer;
    }

    public static String[] getWinners() {
        return winners;
    }

    public static void setWinners(String[] winners) {
        Connection.winners = winners;
    }

    public static boolean isUpdate() {
        return update;
    }

    public static void setUpdate(boolean update) {
        Connection.update = update;
    }

    public static void createConnection() {
        try {
            cs = IO.socket(SERVER);
            cs.connect();

        } catch (URISyntaxException e) {
            /**
             * Error
             */

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
        cs.once("START_GAME", el);
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

    public static void emitStocks(ConStock s) {

        String jsonInString = new Gson().toJson(s);
        try {
            JSONObject mJSONObject = new JSONObject(jsonInString);
            cs.emit("CHOSE_STOCKS", lobbyID, mJSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void roleHighestDice(Emitter.Listener el) {
        cs.once("ROLE_THE_HIGHEST_DICE", el);
    }

    public static void emitHighestDice(int dice1, int dice2) {

        int[] diceCount = new int[2];
        diceCount[0] = dice1;
        diceCount[1] = dice2;

        cs.emit("ROLE_THE_HIGHEST_DICE", lobbyID, diceCount);
    }

    public static void roleHighestDiceAgain(Emitter.Listener el) {
        cs.on("ROLE_THE_HIGHEST_DICE_AGAIN", el);
    }

    public static void emitHighestDiceAgain(int dice1, int dice2) {
        int[] diceCount = new int[2];
        diceCount[0] = dice1;
        diceCount[1] = dice2;

        cs.emit("ROLE_THE_HIGHEST_DICE_AGAIN", lobbyID, diceCount, winners.length);
    }

    public static void startRound(Emitter.Listener el) {
        cs.once("START_ROUND", el);
    }

    public static void convertJsonToPlayer(String args) {

        cp.clear();

        /**
         * Split Player-Array in multiple Player Strings
         */

        //String[] players = args.split("\\},", 10);
        String[] players = args.split("},", 10);

        for (int i = 0; i < players.length; i++) {

            String temp = players[i];

            String newString = "";

            if (i == 0) {

                newString = temp.substring(24);
                newString += "}";
                players[i] = newString;
            } else if (i < (players.length - 1)) {

                newString = temp.substring(23);
                newString += "}";

                players[i] = newString;

            } else if (i == (players.length - 1)) {

                newString = temp.substring(23);
                newString = newString.substring(0, newString.length() - 1);
                players[i] = newString;
            }

        }

        /**
         * Parser for Stocks
         */

        ArrayList<String> stockAsString = new ArrayList<>();

        for(String s : players){
            System.out.println(s);
        }


        for (int i = 0; i < players.length; i++) {

            String[] stock = players[i].split("ShortCircuit_PLC");

            System.out.println("Stock after split: " + stock[1]);

            String newStock = stock[1];

            String newNewStock = "{\"ShortCircuit_PLC";

            newNewStock += newStock;

            String finalStock = newNewStock.substring(0, newNewStock.length() - 1);

            System.out.println("Stock Final: " + finalStock);

            stockAsString.add(finalStock);
        }


        /**
         * Parse Player-Strings into Con-Players
         */

        ArrayList<String> allPlayersAsString = new ArrayList<>();

        Collections.addAll(allPlayersAsString, players);

        for (String p : allPlayersAsString) {

            JsonObject jsonObject = JsonParser.parseString(p).getAsJsonObject();

            ConPlayer temp = new ConPlayer();

            temp.setDice2(jsonObject.get("dice_2").getAsInt());

            temp.setDice1(jsonObject.get("dice_1").getAsInt());

            temp.setMoney(jsonObject.get("money").getAsInt());

            temp.setYouTurn(jsonObject.get("yourTurn").getAsBoolean());

            temp.setSocket(jsonObject.get("socket").getAsString());

            temp.setPosition(jsonObject.get("position").getAsInt());

            temp.setDiceCount(jsonObject.get("dice_Count").getAsInt());

            temp.setPlayerIndex(jsonObject.get("playerIndex").getAsInt());

            cp.add(temp);

        }

        /**
         * Parse Stocks into ConPlayer Object
         */

        for (int i = 0; i < stockAsString.size(); i++) {

            JsonObject jsonStock = JsonParser.parseString(stockAsString.get(i)).getAsJsonObject();


            cp.get(i).setHardSteelPlc(jsonStock.get("HardSteel_PLC").getAsInt());
            cp.get(i).setShortCircuitPlc(jsonStock.get("ShortCircuit_PLC").getAsInt());
            cp.get(i).setDryOilPlc(jsonStock.get("DryOil_PLC").getAsInt());
        }

    }

    /**
     * Create real Player-Objects Initial
     */

    public static ArrayList<Player> convertConPlayersToPlayersInitial() {
        ArrayList<Player> pList = new ArrayList<>();

        for (ConPlayer c : cp) {

            System.out.println(c.getPlayerIndex());

            Field f = MankomaniaGame.getInstance().getBoard().getFieldByIndex(c.getPosition());

            Player p = new Player(f, c.getPlayerIndex(), c.getSocket());

            pList.add(p);

        }

        return pList;
    }

    /**
     * Create real Player-Objects at Update
     */

    public static ArrayList<Player> convertConPlayersToPlayersUpdate() {
        ArrayList<Player> pList = new ArrayList<>();

        for (ConPlayer c : cp) {

            Field f = MankomaniaGame.getInstance().getBoard().getFieldByIndex(c.getPosition());

            Player p = new Player(f, c.getPlayerIndex(), c.getSocket());

            p.setMoney(c.getMoney());

            p.setShares(c.getHardSteelPlc(), c.getShortCircuitPlc(), c.getDryOilPlc());

            pList.add(p);

        }

        return pList;
    }

}
