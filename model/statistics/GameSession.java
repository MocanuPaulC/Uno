package model.statistics;




import model.player.Player;
import model.player.PlayerSession;
import model.table.Card;
import model.table.SpecialCard;
import model.table.Table;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;


public class GameSession {


    Rules colorRules = new Rules();
    RulesEngine rulesEngine = new DefaultRulesEngine();
    Facts facts = new Facts();
    Random random = new Random();
    private final SessionStatistics seshStats = new SessionStatistics();
    private Player playerOne;
    private final Player playerTwo;
    private final Table gameTable = new Table();
    private Turn turn;
    private String dbPass;
    private String activeColor;
    //    private Player activePlayer;

    private PlayerSession botPlayerSession;
    private PlayerSession humanPlayerSession;
    private long startTimeSesh;
    private long endTimeSesh;

    private long startTimeTurn, endTimeTurn;

    private final GameStatistics gameStats = new GameStatistics();

    public long getStart() {
        return startTimeSesh;
    }

    public GameStatistics getGameStats() {
        return gameStats;
    }

    public void setStart(long start) {
        this.startTimeSesh = start;
    }

    public long getEnd() {
        return endTimeSesh;
    }

    public void setEnd(long end) {
        this.endTimeSesh = end;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setStartTimeTurn() {
        this.startTimeTurn = System.nanoTime();
    }

    public void setEndTimeTurn() {
        this.endTimeTurn = System.nanoTime();
    }

    public void checkUnoCall(){
        // it disables the player's deck
        // a random time that the bot took to call uno
        setEndTimeTurn();
        int randomTime = random.nextInt(2)+1;
        Player activePlayer = turn.getActualPlayer();
        double elapsedTimeInSecond = (double) (endTimeTurn - startTimeTurn) / 1_000_000_000;
        // if the player was slower than the bot
        if (elapsedTimeInSecond > randomTime) {
            // and the active player is the human
            if (activePlayer.equals(playerOne)) {
                // the human gets a card added to his deck
                gameTable.addCardToPlayer(playerOne.getPlayerDeck());
            }

        } else {
            if (activePlayer.equals(playerTwo)) {
                gameTable.addCardToPlayer(playerTwo.getPlayerDeck());
            }
        }
    }

    public GameSession(String playerTwo) {
        this.playerTwo = new Player(playerTwo, gameTable);
        this.gameTable.setPlayerTwoDeck(this.playerTwo.getPlayerDeck());
        this.botPlayerSession=new PlayerSession(this.playerTwo,this);
        createRules();
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = new Player(playerOne, gameTable);
        this.gameTable.setPlayerOneDeck(this.playerOne.getPlayerDeck());
        this.humanPlayerSession = new PlayerSession(this.playerOne,this);
    }

    // initialise the first turn
    public void initialiseTurn(String firstTurn) {
        System.out.println("we get here");
// add dbpass to turn class and to the turn statistics
        // then remove the need for passage
        // and do the same for the other statistics classes
        turn = new Turn(firstTurn.equals(playerOne.getName()) ? playerOne : playerTwo);
        gameTable.getTakeDeck().shuffleDeck();
        activeColor = gameTable.firstPutCard();


    }

    public void setFirstPlayer(boolean humanPlayerFirst){
        //if true bot
        // else human
        if(humanPlayerFirst){
            botPlayerSession.setFirst(true);
            humanPlayerSession.setFirst(false);
        }
        else {
            botPlayerSession.setFirst(false);
            humanPlayerSession.setFirst(true);
        }
    }


    public void endSession() {
        seshStats.sessionEnd(dbPass);
    }

    public boolean isFirstSpecial(Player firstTurn) {
        String type = gameTable.getPutDeck().getCardList().getLast().getTypeOfCard();
        if (gameTable.getPutDeck().getCardList().getLast() instanceof SpecialCard specialCard) {
            firstTurn.activateSpecial(specialCard, turn.getTurnNr() == 1);
        }
        if (firstTurn.equals(playerTwo) && (type.equals("WILD") || type.equals("WILDDRAW"))) {
//            activeColor=Card.Color.values()[random.nextInt(4)].toString();
            setActiveColorBot();
            return false;
        } else return true;
    }


    public PlayerSession getBotPlayerSession() {
        return botPlayerSession;
    }

    public PlayerSession getHumanPlayerSession() {
        return humanPlayerSession;
    }

    public void startGame() {
        gameTable.distributeCards();
    }

    public void playCard(Card card) {
//        turn.addCardToDeck(card);
        activeColor = playerOne.addCardToDeck(card, activeColor);
//        activeColor= gameTable.getPutDeck().getCardList().getLast().getColor();
//        System.out.println("active color is " + activeColor);
        turn.save(card, playerOne, startTimeSesh, playerOne, dbPass);
        startTimeSesh = System.nanoTime();
//        gameTable.addCardToDeck(card);
    }

    public void botTurn() {
        while (playerTwo.equals(turn.getActualPlayer())) {
            // if the bot played, change active player with consideration about the type of card
            if (playerTwo.botPlayCard(activeColor, this)) {
                if (!gameTable.getPutDeck().getCardList().getLast().getColor().equals("ALL")) {
                    activeColor = gameTable.getPutDeck().getCardList().getLast().getColor();
//                    setActiveColorBot();
                } else {
//                    activeColor=Card.Color.values()[random.nextInt(4)].toString();
                    setActiveColorBot();
                }
                System.out.println("active color is " + activeColor);
                turn.save(gameTable.getPutDeck().getCardList().getLast(), playerTwo, startTimeSesh, playerOne, dbPass);
                startTimeSesh = System.nanoTime();
                Turn.setOtherPlayer(playerOne, playerTwo, gameTable.getPutDeck().getCardList().getLast());
            }
            if ((playerTwo.getPlayerDeck().getCardList().size() == 0 || playerTwo.getPlayerDeck().getCardList().size() == 1)
                    && (gameTable.getPutDeck().getCardList().getLast().getTypeOfCard().equals("REVERSE") || gameTable.getPutDeck().getCardList().getLast().getTypeOfCard().equals("SKIP"))) {
                Turn.setOtherPlayerDontCareCard(playerOne, playerTwo);
            }
        }
    }

    public void createRules() {
//        MVELRuleFactory ruleFactory = new MVELRuleFactory()
        Rule colorBlue = new RuleBuilder()
                .name("BLUE RULE")
                .description("if nr of blue is max then set blue active color blue")
                .when(facts1 -> colorCount.get("BLUE")>colorCount.get(activeColor))
                .then(facts1 -> activeColor="BLUE")
                .build();

        Rule colorRed = new RuleBuilder()
                .name("RED RULE")
                .description("if nr of red is max then set red active color red")
                .when(facts1 -> colorCount.get("RED")>colorCount.get(activeColor))
                .then(facts1 -> activeColor="RED")
                .build();
        Rule colorYellow = new RuleBuilder()
                .name("YELLOW RULE")
                .description("if nr of YELLOW is max then set YELLOW active color YELLOW")
                .when(facts1 -> colorCount.get("YELLOW")>colorCount.get(activeColor))
                .then(facts1 -> activeColor="YELLOW")
                .build();
        Rule colorGreen = new RuleBuilder()
                .name("GREEN RULE")
                .description("if nr of GREEN is max then set GREEN active color GREEN")
                .when(facts1 -> colorCount.get("GREEN")>colorCount.get(activeColor))
                .then(facts1 -> activeColor="GREEN")
                .build();



        colorRules.register(colorBlue);
        colorRules.register(colorRed);
        colorRules.register(colorYellow);
        colorRules.register(colorGreen);

//        RulesEngine

    }

    HashMap<String, Integer> colorCount;
    public void setActiveColorBot() {
         colorCount = playerTwo.getColorCounts();
        // BLUE - 1
        facts.put("color", colorCount);
        rulesEngine.fire(colorRules, facts);

    }

    public Turn getTurn() {
        return turn;
    }

    public Table getGameTable() {
        return gameTable;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(String activeColor) {
        this.activeColor = activeColor;
    }

    public SessionStatistics getSeshStats() {
        return seshStats;
    }


    public void initialiseTables(String password) throws SQLException {
        gameStats.initialiseTables(password);
    }

    public void initialiseDataBase(String playerName, String password, String firstTurnPlayer) throws SQLException {
        dbPass = password;
        gameStats.databaseInitialisation(playerName, dbPass, firstTurnPlayer);
    }


    // two types of bot turns
    public void botTurnCase(int i) {
        switch (i) {
            case 1 -> Turn.setOtherPlayer(playerOne, playerTwo, gameTable.getPutDeck().getCardList().getLast());
            case 2 -> Turn.setOtherPlayerDontCareCard(playerOne, playerTwo);
        }
        botTurn();
    }


    // checks if the bot can play


    // cant Play returns true if the bot has no playable cards and false if he has at least one


    // Initialise the database
    // to be moved to another class


    // method used to initialise a new session entry in the database
    // to be moved to another class


    // method used to complete the session data
    // to be moved to another class


}
