package com.mankomania.game.connections;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mankomania.game.MankomaniaGame;

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
        MankomaniaGame.getInstance().getBoard().setCurrentPlayer(currentPlayer);
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


    public static void emitStocks(ConStock s) {

        String jsonInString = new Gson().toJson(s);
        try {
            JSONObject mJSONObject = new JSONObject(jsonInString);
            cs.emit("CHOSE_STOCKS", lobbyID, mJSONObject);
        } catch (JSONException e) {
            /**
             * Error
             */
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

    /**
     * new Methods
     **/

    public static void updateDice(Emitter.Listener el) {
        cs.on("UPDATE_DICE", el);
    }

    public static void emitDices(int dice1, int dice2) {
        int[] diceCount = new int[2];
        diceCount[0] = dice1;
        diceCount[1] = dice2;

        cs.emit("ROLE_THE_DICE", lobbyID, diceCount);

        System.out.println("Emit dices");
    }

    //Keine Ahnugn wo zu Implementieren
    public static void emitPosition(Field f) {
        int position = f.getFieldIndex();
        System.out.println("Emit position: " + position);
        cs.emit("UPDATE_PLAYER_POSITION", lobbyID, position);
    }

    //Keine Ahnugn wo zu Implementieren
    public static void emitNextTurn() {
        cs.emit("NEXT_TURN", lobbyID);
        Connection.yourTurn = false;
        System.out.println("Emit NEXT_TURN");
    }

    public static void updateNextTurn(Emitter.Listener el) {
        cs.on("NEXT_TURN", el);
    }

    public static void updateMyPlayerPosition(Emitter.Listener el) {
        cs.on("UPDATE_PLAYER_POSITION", el);
    }

    /**
     * Field Actions
     */

    public static void determineFieldAction(Field f) {

        int index = f.getFieldIndex();

        if (index == 8 || index == 30 || index == 34 || index == 56) {
            raceField();

        } else if (index == 23) {
            stockField();

        } else if (index == 42) {
            auctionField();

        } else if (index == 1 || index == 11 || index == 38) {
            getMoney(5000);

        } else if (index == 2) {
            loseMoney(170000);

        } else if (index == 3 || index == 7 || index == 13 || index == 14 || index == 15 || index == 18 || index == 21 || index == 25 || index == 27 || index == 29 || index == 39 || index == 40 || index == 54 || index == 59 || index == 64 || index == 65) {
            loseMoney(50000);

        } else if (index == 4 || index == 6 || index == 12 || index == 17 || index == 24 || (index <= 52 && index >= 46) || index == 61 || index == 62 || index == 68) {
            loseMoney(100000);

        } else if (index == 5 || index == 9 || index == 22 || index == 36 || index == 55 || index == 57 || index == 67) {
            getMoney(10000);

        } else if (index == 10 || index == 16 || index == 20 || index == 32 || index == 37 || index == 44 || index == 45 || index == 60) {
            getMoney(50);

        } else if (index == 19) {
            getMoney(50000);

        } else if (index == 26) {
            loseMoney(40000);

        } else if (index == 28) {
            loseMoney(70000);

        } else if (index == 31) {
            loseMoney(150000);

        } else if (index == 33 || index == 35) {
            loseMoney(30000);

        } else if (index == 41 || index == 53 || index == 63) {
            loseMoney(20000);

        } else if (index == 43) {
            loseMoney(80000);

        } else if (index == 58) {
            loseMoney(60000);

        } else if (index == 66) {
            loseMoney(10000);

        } else {
            /**
             * Do nothing
             */
        }
    }

    //Aufrufen
    public static void loseMoney(int amount) {
        cs.emit("LOSE_MONEY", lobbyID, amount);
        System.out.println("Emit loseMoney");
    }

    public static void getMoney(int amount) {
        cs.emit("GET_MONEY", lobbyID, amount);
        System.out.println("Emit getMoney");
    }

    public static void raceField() {
        cs.emit("RACE", lobbyID);
        System.out.println("Emit RACE");
    }

    public static void stockField() {
        cs.emit("STOCK", lobbyID);
        System.out.println("Emit STOCK");
    }

    public static void auctionField() {
        cs.emit("AUCTION", lobbyID);
        System.out.println("Emit AUCTION");
    }


    public static void getMoneyUpdate(Emitter.Listener el) {
        cs.on("GET_MONEY", el);
    }

    public static void loseMoneyUpdate(Emitter.Listener el) {
        cs.on("LOSE_MONEY", el);
    }


    /**
     * Player collision
     */

    public static void collision(Emitter.Listener el) {
        cs.on("PLAYER_COLLISION", el);
    }

    //Aufrufen
    public static void collisionEmit(String[] players) {
        cs.emit("PLAYER_COLLISION", lobbyID, players);
        System.out.println("Emit collision");
    }


    /**
     * Minigames
     */

    //Aufrufen
    public static void auction(int difference) {
        cs.emit("PLAYER_COLLISION", lobbyID, difference);
        System.out.println("Emit auction money difference");
    }

    //Aufrufen
    public static void stockMiniagmeEmit(String stock, boolean black) {

        StockMinigame sm = new StockMinigame(stock, black);

        String jsonInString = new Gson().toJson(sm);
        try {
            JSONObject mJSONObject = new JSONObject(jsonInString);
            cs.emit("STOCK", lobbyID, mJSONObject);
            System.out.println("Emit stock minigame result");
        } catch (JSONException e) {
            /**
             * Error
             */
        }
    }

    public static void stockMinigameUpdate(Emitter.Listener el) {
        cs.on("STOCK", el);
    }




    /**
     * Converter methods
     */

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


        for (int i = 0; i < players.length; i++) {

            String[] stock = players[i].split("ShortCircuit_PLC");

            String newStock = stock[1];

            String newNewStock = "{\"ShortCircuit_PLC";

            newNewStock += newStock;

            String finalStock = newNewStock.substring(0, newNewStock.length() - 1);

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
