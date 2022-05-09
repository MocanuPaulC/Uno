package model.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.*;

public class GameStatistics {

    public void initialiseTables(String dbPass) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS players
                    (
                        player_id numeric(8)  NOT NULL PRIMARY KEY,
                        name      VARCHAR(40) NOT NULL
                            constraint ch_name_length check ( LENGTH(name) <= 40 )
                    );



                    CREATE TABLE IF NOT EXISTS sessions
                    (
                        session_id  numeric(4) NOT NULL PRIMARY KEY,
                        start_time  TIMESTAMP  NOT NULL,
                        end_time    TIMESTAMP,
                        winnerScore numeric(8)
                    );

                    CREATE TABLE IF NOT EXISTS player_sessions
                    (
                        player_id  numeric(8) NOT NULL
                            constraint fk_playerSession_player references players (player_id),
                        session_id numeric(4) NOT NULL
                            constraint fk_playerSession_session references sessions (session_id),
                        score      numeric(8),
                        isFirst    boolean,
                        constraint pk_playerSessions PRIMARY KEY (session_id,player_id)
                    );

                    CREATE TABLE IF NOT EXISTS turns
                    (
                        turn_id    numeric(8) NOT NULL,
                        session_id numeric(8) NOT NULL,
                        player_id  numeric(8) NOT NULL,
                        score      numeric(8),
                        duration   numeric(12, 8),
                        playStamp  timestamp  NOT NULL,
                        card_color VARCHAR(8),
                        card_type  VARCHAR(10),
                        card_value numeric(2)
                            constraint ch_card_value check ( card_value <= 50),
                        constraint pk_turns PRIMARY KEY (turn_id, session_id,player_id),
                        constraint fk_turns_playerSessions FOREIGN KEY (session_id,player_id) references player_sessions(session_id,player_id)
                    );



                    CREATE SEQUENCE IF NOT EXISTS seq_player_id START WITH 2 INCREMENT BY 1;
                    CREATE SEQUENCE IF NOT EXISTS seq_session_id START WITH 1 INCREMENT BY 1;



                    INSERT INTO players values (1, 'bot') ON CONFLICT DO NOTHING;""");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    public void databaseInitialisation(String playerName, String dbPass, String firstTurn) throws SQLException {
        try {
//            dbPass = password;
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();


            // if the player doesn't exist in the database, a new entry is created for the new player
            if (!checkPlayerExists(connection, playerName)) {
                statement.execute("INSERT INTO players values (nextval('seq_player_id'),'" + playerName + "')");
            }
            // the session entry is created
            sessionInit(connection, playerName, firstTurn);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void playerSessionInit(String playerName,String dbPass, String firstTurn){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO player_sessions(player_id,session_id,isfirst) values "+
                    "((SELECT player_id FROM players WHERE name ='" + playerName + "'), (SELECT last_value FROM seq_session_id), false);" +
                    "" +
                    "UPDATE player_sessions SET isfirst=true WHERE player_id=(SELECT player_id FROM players WHERE name ='" + firstTurn + "') " +
                    "AND session_id=(SELECT last_value FROM seq_session_id);");

        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public void sessionInit(Connection connection, String playerName, String firstTurn) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO sessions (session_id, start_time) values " +
                    "(nextval('seq_session_id'),CURRENT_TIMESTAMP)");

            statement.close();
        } catch (SQLException ex) {
            System.out.println("bad here");
            ex.printStackTrace();
        }
    }

    public boolean checkPlayerExists(Connection connection, String playerName) {
        try {
            Statement statement = connection.createStatement();
            //The resultSet object will either be empty or contain exactly one row

            ResultSet resultSet = statement.executeQuery("SELECT player_id FROM players WHERE name ='" + playerName + "'");
            //It enters the while loop if it has a value which for us means that a saved game exists for that player
            //and as such we return true and the method stops
            if(resultSet.next()){
//                System.out.println("wowshithere");
                resultSet.close();
                statement.close();
                return true;
            }

            //It doesn't enter the while loop if it's empty, meaning that there is no saved game and returning a false boolean.
            resultSet.close();
            statement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public int getNrOfGamesPlayed(String dbPass, String playerOne) {
        try {
            int count = 0;
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM player_sessions  WHERE player_id=(SELECT player_id FROM players " + "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')");
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return count;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getNrOfCardsPlayed(String dbPass, String playerOne) {
        try {
            int count = 0;
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM turns  WHERE player_id=(SELECT player_id FROM players " + "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')");
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return count;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getAvgGameScore(String dbPass, String name) {
        try {
            int score = 0;
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT AVG(score) FROM player_sessions  WHERE player_id=(SELECT player_id FROM players WHERE name='" +name.toLowerCase(Locale.ROOT) + "')");
            while (resultSet.next()) {
                score = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return score;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getLastSeshScore(String dbPass) {
        try {
            int score = 0;
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT score FROM player_sessions WHERE session_id= (SELECT last_value FROM seq_session_id)");
            while (resultSet.next()) {
                score = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return score;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public ObservableList<PieChart.Data> getFreqOfTypesPlayedAllGames(String dbPass, String playerOne) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        try {
            HashMap<String, Integer> freqByType = new HashMap<>();
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT card_type FROM turns WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')");
            String type;
            while (resultSet.next()) {
                type = resultSet.getString(1);
                if (!freqByType.containsKey(type)) {
                    freqByType.put(type, 1);
                } else {
                    freqByType.put(type, freqByType.get(type) + 1);
                }


            }
            String index;
            for (Map.Entry<String, Integer> entry : freqByType.entrySet()) {
                index = entry.getKey();
                pieData.add(new PieChart.Data(index, entry.getValue()));
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        return pieData;
    }

    public ObservableList<PieChart.Data> getFreqOfColorsPlayedAllGames(String dbPass, String playerOne) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
//        ArrayList<String> pieColors = new ArrayList<>();
        try {
            HashMap<String, Integer> freqByColor = new HashMap<>();
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT card_color FROM turns WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')");

            String color;
            while (resultSet.next()) {
                color = resultSet.getString(1);
                if (!freqByColor.containsKey(color)) {
                    freqByColor.put(color, 1);
                } else {
                    freqByColor.put(color, freqByColor.get(color) + 1);
                }


            }
            String index;
            for (Map.Entry<String, Integer> entry : freqByColor.entrySet()) {
                index = entry.getKey();
                pieData.add(new PieChart.Data(index, entry.getValue()));
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        return pieData;
    }

    public HashMap<String, Integer> getSeshDurations(String dbPass, String playerOne) {
        LinkedHashMap<String, Integer> seshDuration = new LinkedHashMap<>();
//        int i = 1;
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT AGE(end_time,start_time), ses.session_id FROM" + " sessions as ses " +
                    " JOIN player_sessions ps on ses.session_id = ps.session_id " +
                    "WHERE ps.player_id=(SELECT player_id FROM players WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')");

            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1));
                if (resultSet.getString(1) != null) {
                    int secDuration = Integer.parseInt(resultSet.getString(1).substring(3, 5)) * 60 + Integer.parseInt(resultSet.getString(1).substring(6, 8));
//                System.out.println(secDuration);
                    for (int i = 0; i < 1800; i += 50) {
                        if (!seshDuration.containsKey(String.format("[%d-%d]", i, i + 50))) {
                            seshDuration.put(String.format("[%d-%d]", i, i + 50), 0);
                        }
                        if (secDuration > i && secDuration <= i + 50) {
                            seshDuration.put(String.format("[%d-%d]", i, i + 50), seshDuration.get(String.format("[%d-%d]", i, i + 50)) + 1);
                            break;
                        }
//                        System.out.println(seshDuration);

                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
//        System.out.println(seshDuration);
        return seshDuration;
    }
}


