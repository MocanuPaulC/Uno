package model.player;

import model.statistics.GameSession;
import model.statistics.Turn;
import model.table.Card;
import model.table.Deck;
import model.table.SpecialCard;
import model.table.Table;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import java.util.*;

public class Player {
    private String name;

    Card cardToPlay = null;
    private final Deck playerDeck;
    Rules cardRules = new Rules();
    RulesEngine rulesEngine = new DefaultRulesEngine();
    Facts facts = new Facts();
    List<Card> playableCards = new LinkedList<>();
    private Table gameTable;

    public Player(String name, Table gameTable) {
        this.name = name;
        this.playerDeck = new Deck(true, false);
        this.gameTable=gameTable;
        if (name.equals("bot")) playerDeck.setVisibility(false);
        createRules();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deck getPlayerDeck() {
        return playerDeck;
    }

    // mothod used for the bot to put down a card
    public Card putDownCard(String activeColor) {
        Card cardPlayed=gameTable.getPutDeck().getCardList().getLast();
        playableCards.clear();
        Random random = new Random();
        Card card;
        // for loop used to get a list of all playable cards
        for (int i = 0; i < playerDeck.getCardList().size(); i++) {
            card = playerDeck.getCardList().get(i);
            if (card.getColor().equals(activeColor)) playableCards.add(card);
            else if (card.getValue() < 20 && card.getValue() == cardPlayed.getValue()) playableCards.add(card);
            else if (card.getValue() == 20 && card.getTypeOfCard().equals(cardPlayed.getTypeOfCard())) playableCards.add(card);
            else if (card.getColor().equals("ALL")) playableCards.add(card);
        }
        if (playableCards.size() < 1) {
            return null;
        } else {

            cardToPlay=null;
            facts.clear();
            facts.put("card",playableCards);
            rulesEngine.fire(cardRules,facts);
            return cardToPlay;
        }
    }



    public void createRules(){

        Rule playDrawRule = new RuleBuilder()
                .name("play draw")
                .description("play draw if human has 3 or less cards")
                .when(facts1 -> gameTable.getPlayerOneDeck().getCardList().size()<=3)
                .when(facts1 -> {
                  for(Card card: playableCards){
                      if(card.getTypeOfCard().equals("DRAW")){
                          return true;
                      }
                  }
                  return false;
                })
                .then(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("DRAW")){
                            cardToPlay=card;
                            return;
                        }
                    }
                })
                .build();

        Rule playWildDrawRule = new RuleBuilder()
                .name("play wildDraw")
                .description("play wilddraw if human has 3 or less card")
                .when(facts1 -> gameTable.getPlayerOneDeck().getCardList().size()<=3)
                .when(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("WILDDRAW")){
                            return true;
                        }
                    }
                    return false;
                })
                .then(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("WILDDRAW")){
                            cardToPlay=card;
                            return;
                        }
                    }
                })
                .build();

        Rule playNumberRule = new RuleBuilder()
                .name("play number")
                .description("play number card if human has more than 3 cards")
                .when(facts1 -> gameTable.getPlayerOneDeck().getCardList().size()>3)
                .then(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("NUMBER")){
                            cardToPlay=card;
                            return;
                        }
                    }
                })
                .build();

        Rule playWildRule = new RuleBuilder()
                .name("play wild")
                .description("play wild card if no other cards are available")
                .when(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("NUMBER")){
                            return false;
                        }
                    }
                    return true;
                })
                .when(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("WILD")){
                            return true;
                        }
                    }
                    return false;
                })
                .then(facts1 -> {
                    for(Card card: playableCards){
                        if(card.getTypeOfCard().equals("WILD")){
                            cardToPlay=card;
                            return;
                        }
                    }
                })
                .build();

        Rule playReverseOrBlockRule = new RuleBuilder()
                .name("play reverse or block card")
                .description("play reverse of block card when the bot has a number and a reverse or block card left in the deck")
                .when(facts1 -> playerDeck.getCardList().size()==2)
                .when(facts1 -> playableCards.size()>=2)
                .when(facts1 -> playableCards.get(0).getColor().equals(playableCards.get(1).getColor()))
                .when(facts1 -> {
                    if(playableCards.size()>=2) {
                        return playableCards.get(0) instanceof SpecialCard || playableCards.get(1) instanceof SpecialCard;
                    }
                    return false;
                })
                .then(facts1 -> {
                    if(playableCards.get(0) instanceof SpecialCard)
                        cardToPlay=playableCards.get(0);
                    else if(playableCards.get(1) instanceof SpecialCard)
                        cardToPlay=playableCards.get(1);

                })
                .build();

        Rule playAnything = new RuleBuilder()
                .name("play anything")
                .description("play any card if none of the rules above triggered")
                .when(facts1 -> cardToPlay==null)
                .then(facts1 -> cardToPlay=playableCards.get(new Random().nextInt(playableCards.size())))
                .build();

        cardRules.register(playDrawRule);
        cardRules.register(playWildDrawRule);
        cardRules.register(playNumberRule);
        cardRules.register(playWildRule);
        cardRules.register(playReverseOrBlockRule);
        cardRules.register(playAnything);

    }

    public String addCardToDeck(Card card, String activeColor) {
        if(gameTable.addCardToDeck(card,activeColor,this.getPlayerDeck())){
            if(card instanceof SpecialCard specialCard) this.activateSpecial(specialCard, false);
            if(this.name.equals("bot"))playerDeck.getCardList().remove(card);
        }
        return gameTable.getPutDeck().getCardList().getLast().getColor();

    }

    public void activateSpecial(SpecialCard cardPlaced, boolean firstTurn) {
//        String type = cardPlaced.getTypeOfCard();
//        turn.getTurnStatistics().saveTurn(gameTable.getPutDeck().getCardList().getLast(), this);
        gameTable.specialAbility(this.name, cardPlaced, firstTurn);
    }



    public boolean cantPlay(String activeColor) {
        for (Card card1 : getPlayerDeck().getCardList()) {
            if (gameTable.getPutDeck().isPlayable(activeColor,card1)) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String,Integer> getColorCounts(){
        return playerDeck.getColorCounts();
    }

    private boolean checkPlayable(String activeColor, GameSession sesh) {
        if (cantPlay(activeColor)) {
//            System.out.println("couldn't play tho");
            gameTable.addCardToPlayer(playerDeck);
            Turn.setOtherPlayerDontCareCard(sesh.getPlayerOne(), sesh.getPlayerTwo());
//            System.out.println(" active player change in checkPlayable() to " + turn.getActualPlayer().getName());
            return true;
        }
        return false;
    }

    public boolean botPlayCard(String activeColor, GameSession sesh) {

        if (!checkPlayable(activeColor,sesh)) {
            // get the card to play from the player method
            Card card = this.putDownCard(sesh.getActiveColor());
//            if(card==null)return false;
            addCardToDeck(card, activeColor);
            // save the turn played by the bot
            // return true if bot played successfully
            return true;
        } else {
            // return false if bot didn't play successfully
            return false;
        }

    }




}
