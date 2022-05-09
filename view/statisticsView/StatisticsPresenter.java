package view.statisticsView;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import model.statistics.GameSession;
import view.MainMenuView.MainMenuView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsPresenter {

    private final GameSession model;
    private final StatisticsView view;
    private final MainMenuView menuView;
    private final String dbPass;
    private final String playerOne;

    public StatisticsPresenter(GameSession model, StatisticsView view, MainMenuView menuView,String dbPass, String playerOne) {
        this.model = model;
        this.view = view;
        this.menuView = menuView;
        this.dbPass=dbPass;
        this.playerOne=playerOne;
        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){

//        view.getScene().getWindow().setOnCloseRequest(event -> System.out.println("works"));
        view.getPageTwo().setOnAction(actionEvent ->{view.setPageTwo();} );

        view.getPageOne().setOnAction(actionEvent -> {view.setPageOne();});



        this.view.getScene().getWindow().setOnCloseRequest(event -> {
//            if(view.getScene().getRoot().getClass()==StatisticsView.class){
                System.out.println("finishedINStats");
//            }
        });


        view.getBack().setOnAction(actionEvent -> {
            view.getScene().setRoot(menuView);
            menuView.getScene().getWindow().sizeToScene();
        });
    }


    private void updateView(){
        //show to the user the statistics of all games that he played
        view.getColors().setData(model.getGameStats().getFreqOfColorsPlayedAllGames(dbPass,playerOne.toLowerCase(Locale.ROOT)));
//        int i = 0;

        for (PieChart.Data data : view.getColors().getData()) {
            data.getNode().setStyle("-fx-pie-color: " + (data.getName().equals("ALL")?"BLACK":data.getName()) + ";");
//            i++;
        }

        view.getTypes().setData(model.getGameStats().getFreqOfTypesPlayedAllGames(dbPass,playerOne.toLowerCase(Locale.ROOT)));
        view.getStat1().setText("Total games played: "+ model.getGameStats().getNrOfGamesPlayed(dbPass,playerOne.toLowerCase(Locale.ROOT)));
        view.getStat2().setText("Score Last Session: "+ model.getGameStats().getLastSeshScore(dbPass)+
                "\nAvg session score: "+model.getGameStats().getAvgGameScore(dbPass, playerOne));
        view.getStat3().setText("Total cards played: "+ model.getGameStats().getNrOfCardsPlayed(dbPass,playerOne.toLowerCase(Locale.ROOT)));
        HashMap<String,Integer> seshDur = model.getGameStats().getSeshDurations(dbPass,playerOne.toLowerCase(Locale.ROOT));

        for(Map.Entry<String,Integer> entry :  seshDur.entrySet()){
            if(entry.getValue()!=0) {
                view.getDataSeries().getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        view.getBc().getData().add(view.getDataSeries());


    }


}
