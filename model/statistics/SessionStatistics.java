package model.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.*;

public class SessionStatistics {



    public void sessionEnd(String dbPass) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            statement.execute("UPDATE sessions SET end_time = CURRENT_TIMESTAMP WHERE session_id = (SELECT last_value FROM seq_session_id)");
            statement.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getSeshAvgTurnDuration(String dbPass, String playerOne) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();


            ResultSet results = statement.executeQuery("SELECT AVG(duration) FROM turns WHERE player_id=(SELECT player_id FROM players WHERE name='"
                    + playerOne.toLowerCase(Locale.ROOT) + "') AND session_id=(SELECT last_value FROM seq_session_id)");

            while (results.next()) {
                return results.getDouble(1);
            }

            statement.close();
            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 69;
    }

    public int getCardsPlayed(String dbPass,String playerOne){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM turns WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')" +
                    " AND session_id = (SELECT last_value FROM seq_session_id )");
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }


    // ignore for now
    public int getPlusFourPlayed(String dbPass, String playerOne){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM turns WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')" +
                    " AND session_id = (SELECT last_value FROM seq_session_id )" +
                    " AND card_type='WILDDRAW'");
            if (resultSet.next()){

                return resultSet.getInt(1);
            }

        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }



    public ObservableList<PieChart.Data> getFrequenciesOfTurnDurations(String dbPass, String playerOne) {

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        try {
            HashMap<Integer, Integer> frequencyByClass = new HashMap<>();
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT duration FROM turns WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')" +
                    " AND session_id = (SELECT last_value FROM seq_session_id )");

            while (resultSet.next()) {
                for (int i = 1; i <= 30; i++) {
                    if (resultSet.getDouble(1) < i && resultSet.getDouble(1) >= i - 1) {
                        if (!frequencyByClass.containsKey(i)) {
                            frequencyByClass.put(i, 1);
                        } else {
                            frequencyByClass.put(i, frequencyByClass.get(i) + 1);
                        }
                    }
                }
            }
            String index;
            for (Map.Entry<Integer, Integer> entry : frequencyByClass.entrySet()) {
                index = String.format("[%d, %d]", entry.getKey() - 1, entry.getKey());
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

    public List<Double> getTurnOutliersLastSesh(String dbPass, String playerOne) {
        try {
            double sum = 0;
            List<Double> allDurations = new ArrayList<>();
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT duration FROM turns WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE name='" + playerOne.toLowerCase(Locale.ROOT) + "')" +
                    " AND session_id = (SELECT last_value FROM seq_session_id )");
            while (resultSet.next()) {
                allDurations.add(resultSet.getDouble(1));
                sum += resultSet.getDouble(1);
            }
            // standard deviation
            double std = calculateSD(allDurations);
            double avg = sum / allDurations.size();

            List<Double> outlierNumbers = new ArrayList<>();
            for (double number : allDurations) {
                if ((Math.abs(number - avg)) > (2 * std))
                    outlierNumbers.add(number);
            }

            return outlierNumbers;


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static double calculateSD(List<Double> numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for (double num : numArray) {
            sum += num;
        }

        double mean = sum / length;

        for (double num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public void updateSeshScore(String dbPass, int score) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", dbPass);
            Statement statement = connection.createStatement();
            statement.execute("UPDATE player_sessions SET score =" + score + " WHERE session_id=(SELECT last_value FROM seq_session_id);" +
                    " UPDATE sessions set winnerscore="+score+" WHERE session_id = (SELECT last_value FROM seq_session_id);");
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
