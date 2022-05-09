package model.table;

import java.util.*;

public class Table {
    private final Random random = new Random();
//    private String activeColor;
    private Deck takeDeck = new Deck( false, true);
    private Deck putDeck = new Deck( true, false);
    private Deck playerOneDeck;
    private Deck playerTwoDeck;

    public void setPlayerOneDeck(Deck playerOneDeck) {
        this.playerOneDeck = playerOneDeck;
    }

    public Deck getPlayerTwoDeck() {
        return playerTwoDeck;
    }

    public void setPlayerTwoDeck(Deck playerTwoDeck) {
        this.playerTwoDeck = playerTwoDeck;
    }

    public Deck getTakeDeck() {
        return takeDeck;
    }

    public Table() {

    }

    public void distributeCards(){
        takeDeck.distributeCards(playerOneDeck,playerTwoDeck);
    }





    //DRAW, REVERSE, SKIP, WILD, WILDDRAW, NUMBER

    // when a special card has been played  (not first turn)
    // this method decides who is affected and how


    // method for when a special card is played at the start of the game




    // add nrToDraw cards to the affected deck
    public void draw(Deck toAffect, SpecialCard cardPlayed) {
//        if(cardPlayed instanceof SpecialCard nrToDraw) {
//            SpecialCard nrToDraw = (SpecialCard) cardPlayed;
            for (int i = 0; i < cardPlayed.getNrToDraw(); i++) {
                addCardToPlayer(toAffect);
            }

    }

    public void specialAbility(String playerName, SpecialCard cardPlayed, boolean firstTurn) {

        Deck toAffect;
        if (playerName.equals("bot")) {
            if(!firstTurn) {
                toAffect = getPlayerOneDeck();
            }
            else toAffect=getPlayerTwoDeck();
        } else {
            if(!firstTurn){
                toAffect = getPlayerTwoDeck();
            }
            else toAffect=getPlayerOneDeck();
        }

        draw(toAffect,cardPlayed);
//
//        switch (cardPlayed.getTypeOfCard()) {
//            case "DRAW" -> draw(toAffect, cardPlayed);
//            case "WILDDRAW" -> draw(toAffect, cardPlayed);
//        }
    }



    // add a card to the put deck
    public boolean addCardToDeck(Card card, String activeColor, Deck deck){
//        activePlayer.addCardToDeck(putDeck.getCardList().getLast(),);
        boolean playable = putDeck.isPlayable(activeColor,card);
        System.out.println(card+" card was played on " + putDeck.getCardList().getLast());
        if (playable) {

            putDeck.getCardList().add(card);
            if(deck.equals(playerOneDeck))playerOneDeck.getCardList().remove(card);
//            System.out.println("added "+card + " to putdeck");

            return true;
        } else {
//            return false;
            throw new InputMismatchException("You can't put down that card");
        }
    }
    public void addCardToPlayer(Deck affectedDeck) {
        // if the take deck is empty switch it up with the put deck
        if (takeDeck.getCardList().size() <= 1) {
            Deck aux;
            Card lastCard;
            lastCard = putDeck.getCardList().getLast();
            aux = putDeck;
            putDeck = takeDeck;
            takeDeck = aux;
            takeDeck.getCardList().remove(lastCard);
            putDeck.getCardList().add(lastCard);
            takeDeck.shuffleDeck();
        }
        // take the last card from the take deck and add it to the affected deck (that being the deck of the active player)
//        affectedDeck.getCardList().add(takeDeck.getCardList().getLast());
        affectedDeck.getCardList().add(takeDeck.getCardList().pop());
//        takeDeck.getCardList().removeLast();
        // remove that card from the take deck
    }





    public Deck getPlayerOneDeck() {
        return playerOneDeck;
    }


    // checks if the passed card is playable



    public Deck getPutDeck() {
        return putDeck;
    }



    public String firstPutCard() {
        Card newCard = takeDeck.getCardList().getLast();
        // debug card
//        Card debugCard = new SpecialCard("ALL",50,true,"WILDDRAW");
//        putDeck.getCardList().add(debugCard);
        putDeck.getCardList().add(newCard);
        takeDeck.getCardList().removeLast();
        return newCard.getColor();
//        activeColor = putDeck.getCardList().getLast().getColor();

    }

}


