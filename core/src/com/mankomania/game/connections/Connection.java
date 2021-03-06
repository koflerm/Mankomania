package com.mankomania.game.connections;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mankomania.game.MankomaniaGame;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import fieldlogic.Field;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import playerlogic.Player;
import shareLogic.Share;

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

    public static void setCurrentPlayer(Player currentPlayer) {
        Connection.currentPlayer = currentPlayer;
        MankomaniaGame.getInstance().getBoard().setCurrentPlayer(currentPlayer);
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

    public static boolean getStart() {
        return start;
    }

    public static void setStart(boolean start) {
        Connection.start = start;
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

        cs.emit("ROLE_THE_HIGHEST_DICE", lobbyID, dice1 + "," + dice2);
    }

    public static void startRound(Emitter.Listener el) {
        cs.once("START_ROUND", el);
    }

    public static void updateDice(Emitter.Listener el) {
        cs.on("UPDATE_DICE", el);
    }

    public static void emitDices(int dice1, int dice2) {

        cs.emit("ROLE_THE_DICE", lobbyID, dice1 + "," + dice2);

    }

    public static void emitPosition(Field f) {
        int position = f.getFieldIndex();
        cs.emit("UPDATE_PLAYER_POSITION", lobbyID, position);
    }

    public static void emitNextTurn() {
        cs.emit("NEXT_TURN", lobbyID);
        Connection.yourTurn = false;
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

    public static void determineFieldAction(Field f, Player p) {

        int index = f.getFieldIndex();

       if (index == 10 || index == 16 || index == 20 || index == 32 || index == 37 || index == 45 || index == 60) {
           getMoney(50);
           p.addMoney(50);

       }else if (index == 44){
           getMoney(250);
           p.addMoney(250);

       }else if (index == 0 || index == 11) {
           getMoney(5000);
           p.addMoney(5000);

       }else if (index == 5 || index == 9 || index == 22 || index == 36 || index == 57) {
           getMoney(10000);
           p.addMoney(10000);

       }else if (index == 19) {
           getMoney(50000);
           p.addMoney(50000);

       }else if (index == 55){
           getMoney(70000);
           p.loseMoney(70000);

       }else if (index == 41){
           getMoney(200000);
           p.addMoney(200000);

       }else if (index == 38) {
           loseMoney(5000);
           p.loseMoney(5000);

       }else if (index == 66 || index == 67){
           loseMoney(10000);
           p.loseMoney(10000);

       }else if (index == 33 || index == 35){
           loseMoney(30000);
           p.loseMoney(30000);

       }else if (index == 26 || index == 54) {
           loseMoney(40000);
           p.loseMoney(40000);

       }else if (index == 2 || index == 4 || index == 7 || index == 13 || index == 14 || index == 15 || index == 18 || index == 21 || index == 25 || index == 27 || index == 29 || index == 39 || index == 64 || index == 65) {
           loseMoney(50000);
           p.loseMoney(50000);

       }else if (index == 58 || index == 59){
           loseMoney(60000);
           p.loseMoney(60000);

       }else if (index == 28 || index == 40) {
           loseMoney(70000);
           p.loseMoney(70000);

       }else if (index == 43){
           loseMoney(80000);
           p.loseMoney(80000);

       }else if (index == 3 || index == 6 || index == 12 || index == 17 || index == 24 || index == 46 || index == 47 || index == 48 || index == 49 || index == 50 || index == 51 || index == 52 || index == 61 || index == 62 || index == 68) {
           loseMoney(100000);
           p.loseMoney(100000);

       }else if (index == 31){
           loseMoney(150000);
           p.loseMoney(150000);

       }else if (index == 1) {
           loseMoney(170000);
           p.loseMoney(170000);

       }else if (index == 53 || index == 63){
           loseMoney(200000);
           p.loseMoney(200000);

        } else {
            /**
             * Do nothing
             */
        }
    }


    public static void loseMoney(int amount) {
        cs.emit("LOSE_MONEY", lobbyID, amount);
    }

    public static void getMoney(int amount) {
        cs.emit("GET_MONEY", lobbyID, amount);
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

    public static void collisionEmit(String[] players) {
        cs.emit("PLAYER_COLLISION", lobbyID, players);
    }


    /**
     * Minigames
     */


    public static void auctionEmit(float itemprice, float multiplicator) {

        float moneyFromBank = itemprice * multiplicator;
        int difference = (int) moneyFromBank - (int) itemprice;

        MankomaniaGame.getInstance().getBoard().getCurrentPlayer().addMoney(difference);

        int money = MankomaniaGame.getInstance().getBoard().getCurrentPlayer().getMoney();

        ConAuction ca = new ConAuction(itemprice, multiplicator, moneyFromBank, difference, money);

        String jsonInString = new Gson().toJson(ca);
        try {
            JSONObject caJSONObject = new JSONObject(jsonInString);
            cs.emit("AUCTION", lobbyID, caJSONObject);
        } catch (JSONException e) {
            /**
             * Error
             */
        }
    }


    public static void stockMinigameEmit(String stock, boolean black) {

        Player current = MankomaniaGame.getInstance().getBoard().getCurrentPlayer();

        Share s;

        if (stock.equals("HardSteel")) {
            s = Share.HARD_STEEL_PLC;
        } else if (stock.equals("Shortcircuit")) {
            s = Share.SHORT_CIRCUIT_PLC;
        } else {
            s = Share.DRY_OIL_PLC;
        }


        int amountOfStock = current.getAmountOfShare(s);
        if (amountOfStock > 0) {
            if (black) {
                current.addMoney(10000 * amountOfStock);
            } else {
                current.loseMoney(20000 * amountOfStock);
            }
        }


        StockMinigame sm = new StockMinigame(stock, black);

        String jsonInString = new Gson().toJson(sm);
        try {
            JSONObject mJSONObject = new JSONObject(jsonInString);
            cs.emit("STOCK", lobbyID, mJSONObject);
        } catch (JSONException e) {
            /**
             * Error
             */
        }
    }

    public static void auctionMinigameUpdate(Emitter.Listener el) {
        cs.on("AUCTION", el);
    }

    /**
     * Money check <= 0
     */

    public static void emitWinner() {
        cs.emit("WINNER", lobbyID);
    }

    public static void winnerUpdate(Emitter.Listener el) {
        cs.once("WINNER", el);
    }


    /**
     * Converter methods
     */

    public static void convertJsonToPlayer(String args) {

        cp.clear();

        /**
         * Split Player-Array in multiple Player Strings
         */
        
        String[] tempPlayers;

        if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
            tempPlayers = args.split("dice_Count\":[0-9]+\\},");
        }else{
            tempPlayers = args.split("},", 10);
        }

        for (int i = 0; i < tempPlayers.length; i++) {

            String temp = tempPlayers[i];

            String newString = "";

            if (i == 0) {

                newString = temp.substring(24);

                if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
                    newString += "dice_Count\":0}";

                }else{
                    newString += "}";
                }

                tempPlayers[i] = newString;

            } else if (i < (tempPlayers.length - 1)) {

                newString = temp.substring(23);

                if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
                    newString += "dice_Count\":0}";
                }else{
                    newString += "}";
                }

                tempPlayers[i] = newString;

            } else if (i == (tempPlayers.length - 1)) {

                newString = temp.substring(23);
                newString = newString.substring(0, newString.length() - 1);
                tempPlayers[i] = newString;
            }

        }

        /**
         * Parser for Stocks
         */

        ArrayList<String> stockAsString = new ArrayList<>();


        for (int i = 0; i < tempPlayers.length; i++) {

            String[] stock = tempPlayers[i].split("stocks\":");

            String newStock = stock[1];

            String finalStock = newStock.substring(0, newStock.length() - 1);

            if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
                String[] android = finalStock.split(",\"yourTurn");
                finalStock = android[0];
            }


            stockAsString.add(finalStock);
        }


        /**
         * Parse Player-Strings into Con-Players
         */

        ArrayList<String> allPlayersAsString = new ArrayList<>();

        Collections.addAll(allPlayersAsString, tempPlayers);

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
