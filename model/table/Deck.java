package model.table;

import java.util.*;

public class Deck {
    private boolean visibility;
    private final LinkedList<Card> cardList = new LinkedList<>();
    //colors = red, blue, green, yellow


    // method used to give 7 cards to each player
    public void distributeCards(Deck playerOneDeck, Deck playerTwoDeck) {
        this.shuffleDeck();
        int count = 0;
        for (Iterator<Card> it = this.getCardList().iterator(); it.hasNext(); ) {
            Card card = it.next();
            if (count % 2 == 0) {
                playerOneDeck.getCardList().add(card);
            } else {

                playerTwoDeck.getCardList().add(card);
            }
            it.remove();
            count++;
            if (count == 14) break;
        }


    }

    public Deck(boolean visibility, boolean isStartDeck) {
        this.visibility = visibility;
        if (isStartDeck) {
            createDeck();
        }
    }

    public int scoreAfterLoss() {
        int sum = 0;
        for (Card card : cardList) {
            sum += card.getValue();
        }
        return sum;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    // method used to create the full deck of cards
    public void createDeck() {
        int value = 0;
        Card cardToAdd;
        for (int i = 0; i < Card.Color.values().length - 1; i++) {
            // for loop to make all the number cards
            for (int j = 0; j < 10; j++) {
                cardToAdd=new Card(Card.Color.values()[i].toString(), value++, Card.TypesOfCard.values()[Card.TypesOfCard.values().length - 1].toString());
                cardList.add(cardToAdd);
                if (j >= 1) {
                    cardToAdd = new Card(Card.Color.values()[i].toString(), value - 1, Card.TypesOfCard.values()[Card.TypesOfCard.values().length - 1].toString());
                    cardList.add(cardToAdd);
                }
            }
            value = 0;
            // loop used to make all the special cards
            for (int j = 0; j < 5; j++) {

                if (j >= 3) {
                    cardToAdd=new SpecialCard(Card.Color.values()[Card.Color.values().length - 1].toString(), 50, true, Card.TypesOfCard.values()[j].toString());
                    cardList.add(cardToAdd);
                } else {
                    cardToAdd=new SpecialCard(Card.Color.values()[i].toString(), 20, true, Card.TypesOfCard.values()[j].toString());
                    cardList.add(cardToAdd);
                    cardToAdd=new SpecialCard(Card.Color.values()[i].toString(), 20, true, Card.TypesOfCard.values()[j].toString());
                    cardList.add(cardToAdd);
                }
            }
        }
    }


    public HashMap<String,Integer> getColorCounts(){
        HashMap<String,Integer> colorCounts = new HashMap<>();
        colorCounts.put("BLUE",0);
        colorCounts.put("RED",0);
        colorCounts.put("GREEN",0);
        colorCounts.put("YELLOW",0);
        for(Card card : this.getCardList()){
            if(!card.getColor().equals("ALL")) {
                colorCounts.put(card.getColor(), colorCounts.get(card.getColor()) + 1);
            }
        }
        return colorCounts;
    }

    public boolean isPlayable(String activeColor, Card playedCard) {
        if (!playedCard.getColor().equals(activeColor)) {
            if ((playedCard.getValue() < 20 && playedCard.getValue() == getCardList().getLast().getValue())
                    || playedCard.getTypeOfCard().equals(getCardList().getLast().getTypeOfCard()) && playedCard.getValue() == 20) {
//                activeColor = card.getColor();
                return true;
            } else return playedCard.getColor().equals("ALL");
        } else return true;

    }

    public void shuffleDeck() {
        Collections.shuffle(cardList);
    }

    public LinkedList<Card> getCardList() {
        return cardList;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cardList.size(); i++) {
            stringBuilder.append(i + 1).append(". ").append(cardList.get(i));
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return visibility == deck.visibility && Objects.equals(cardList, deck.cardList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visibility, cardList);
    }
}
