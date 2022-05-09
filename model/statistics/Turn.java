package model.statistics;

import model.player.Player;
import model.table.Card;

public class Turn {
    private static Player actualPlayer;
    private int turnNr;
    private TurnStatistics turnStatistics = new TurnStatistics();

    public Turn(Player actualPlayer) {
        turnNr = 1;
        Turn.actualPlayer = actualPlayer;
    }

    public static void setActualPlayer(Player actualPlayer) {
        Turn.actualPlayer = actualPlayer;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public TurnStatistics getTurnStatistics() {
        return turnStatistics;
    }

    public void setTurnStatistics(TurnStatistics turnStatistics) {
        this.turnStatistics = turnStatistics;
    }

    public int getTurnNr() {
        return turnNr;
    }

    public void setTurnNr(int turnNr) {
        this.turnNr = turnNr;
    }

    // change the active player taking into account the last card played
    public static void setOtherPlayer(Player playerOne, Player playerTwo,Card card) {
        System.out.println("old active player is " + actualPlayer.getName());
        // if the last card played was a reverse or a skip it doesn't change the player
        // which means the player has multiple turns in a row
        if (!(card.getTypeOfCard().equals("REVERSE") || card.getTypeOfCard().equals("SKIP"))) {
            setOtherPlayerDontCareCard(playerOne, playerTwo);
        }
        System.out.println("new active player is " + actualPlayer.getName());

    }

    public void save(Card card, Player actualPlayer, Long start, Player playerOne, String dbPass){
        turnStatistics.save(card,actualPlayer,start,playerOne,turnNr,dbPass);
        turnNr++;
    }



    // change the active player without caring about the last card played
    public static void setOtherPlayerDontCareCard(Player playerOne, Player playerTwo) {
        if (actualPlayer.equals(playerOne)) Turn.setActivePlayer(playerTwo);
        else Turn.setActivePlayer(playerOne);
    }


    public static void setActivePlayer(Player nextActivePlayer) {
        Turn.actualPlayer = nextActivePlayer;
    }

}
