package model.statistics;

import model.player.Player;
import model.table.Card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class TurnStatistics {

    public void save(Card card, Player activePlayer, Long start, Player playerOne, int turnNr, String dbPass) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            String playerToUse;
            // end time to calculate the duration of the turn
            long end = System.nanoTime();
            long duration = end - start;
            // end is in nano seconds and we need to divide it by 1 billion to get the value in seconds
            double elapsedDouble = (double) duration / 1_000_000_000;

            if (activePlayer.equals(playerOne)) {

                playerToUse = playerOne.getName().toLowerCase(Locale.ROOT);


                String toExecute = String.format("INSERT INTO turns values (%d,(SELECT last_value FROM seq_session_id)," +
                                "(SELECT player_id FROM players WHERE name ='%s'),%d,%f,CURRENT_TIMESTAMP," +
                                " '%s','%s',%d)", turnNr, playerToUse,card.getValue(),elapsedDouble,
                        card.getColor(), card.getTypeOfCard(), card.getValue());
                statement.execute(toExecute);


            } else {
                String toExecute = String.format("INSERT INTO turns values (%d,(SELECT last_value FROM seq_session_id),1,%d,%f,CURRENT_TIMESTAMP," +
                                " '%s','%s',%d)", turnNr, card.getValue(), elapsedDouble,
                        card.getColor(), card.getTypeOfCard(), card.getValue());

                statement.execute(toExecute);
            }
            // reset the start time of the turn because a turn starts when one ends
//            start = System.nanoTime();
//            turn.setTurnNr(turn.getTurnNr() + 1);


            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
