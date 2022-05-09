package view.MainMenuView;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;
import model.player.Player;
//import model.screens.Screens;
import model.statistics.GameSession;
import view.InGameView.InGamePresenter;
import view.InGameView.InGameView;
import view.instructionsView.InstructionView;
import view.instructionsView.InstructionsPresenter;
import view.leaderboardView.LeaderboardPresenter;
import view.leaderboardView.LeaderboardView;
import view.statisticsView.StatisticsPresenter;
import view.statisticsView.StatisticsView;

import java.nio.file.LinkOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class MainMenuPresenter {
    private GameSession model;
//    private Screens screenModel;
    private MainMenuView view;


    public MainMenuPresenter(GameSession model, MainMenuView view) {
        this.model = model;
        this.view = view;

        this.addEventHandlers();
        this.updateView();

    }

    public void addEventHandlers() {


        // start the game
        this.view.getStartGame().setOnAction(actionEvent -> {
//            view.getPassword().setText("Student1234");
            view.getPassword().setText(view.getPassword().getText());
            // initialise the model
            model=new GameSession("bot");
//            Card firstPlayCard;
            Player firstPlayer;
            // set the name of the human player
            model.setPlayerOne(view.getName().getText());

            model.getGameTable().getTakeDeck().shuffleDeck();
            if(view.getFirstTurn().getSelectedToggle().equals(view.getFirstTurnHuman())){

                firstPlayer=model.getPlayerOne();
                model.initialiseTurn(view.getName().getText());
            }
            else {
                model.initialiseTurn("bot");
                firstPlayer=model.getPlayerTwo();
            }
            // initialise the database
            try {
                model.initialiseTables(view.getPassword().getText());
                model.initialiseDataBase(view.getName().getText().toLowerCase(Locale.ROOT),view.getPassword().getText(),firstPlayer.getName().toLowerCase(Locale.ROOT));

                model.getGameStats().playerSessionInit(view.getName().getText().toLowerCase(Locale.ROOT),view.getPassword().getText(), firstPlayer.getName().toLowerCase(Locale.ROOT));
                model.getGameStats().playerSessionInit("bot",view.getPassword().getText(), firstPlayer.getName().toLowerCase(Locale.ROOT));
                // set the game view
                setGameView();
                //get the card from the put deck
                model.startGame();
            }
            catch (SQLException ex){
                alertBadPassWord(actionEvent);
            }

//            gamePresenter.updateView();
            //event for the button instruction (move from main menu to instruction page)
        });
        this.view.getInstructions().setOnAction(actionEvent -> {
            setInstructionsView();

        });
            //event for the button leaderboard (do not remove)
       // this.view.getLeaderboard().setOnAction(actionEvent -> {
       //     setLeaderboardView();
       // });
            //event for the button statistics (move from main menu to statistics page)
        this.view.getStatistics().setOnAction(this::setStatisticsView);


        this.view.getScene().getWindow().setOnCloseRequest(event -> {
            if(view.getScene().getRoot().getClass()==MainMenuView.class){
                System.out.println("finished");
            }
        });

    }

    private void alertBadPassWord(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Wrong Password");
        alert.setContentText("Please try again");
        alert.setTitle("Incorrect password");
        alert.getButtonTypes().clear();
        ButtonType no = new ButtonType("OK");
//        ButtonType yes = new ButtonType("YES");
        alert.getButtonTypes().addAll(no);
        alert.showAndWait();
        if (alert.getResult() == null || alert.getResult().equals(no)) {
            event.consume();
        }
    }


    private void setGameView(){
        InGameView gameView = new InGameView();
        view.getScene().setRoot(gameView);

        new InGamePresenter(model,gameView, view);
        gameView.getScene().getWindow().sizeToScene();
    }

    private void setInstructionsView(){
        InstructionView instructionView = new InstructionView();
        view.getScene().setRoot(instructionView);
        new InstructionsPresenter(instructionView,view);
        instructionView.getScene().getWindow().sizeToScene();
    }
    private void setLeaderboardView(){
        LeaderboardView leaderboardView = new LeaderboardView();
//        LeaderboardPresenter leaderboardPresenter = new LeaderboardPresenter(leaderboardView,view);
        view.getScene().setRoot(leaderboardView);
        leaderboardView.getScene().getWindow().sizeToScene();
    }

    private void setStatisticsView(ActionEvent event){
        try {

            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/UnoDB", "postgres", view.getPassword().getText());
            StatisticsView statisticsView = new StatisticsView();
            view.getScene().setRoot(statisticsView);
            new StatisticsPresenter(model,statisticsView,view, view.getPassword().getText(),view.getName().getText());
            statisticsView.getScene().getWindow().sizeToScene();
            connection.close();
        }catch (SQLException e){
            alertBadPassWord(event);
        }
    }


    public void updateView() {
        // fills the view with model data

    }


}
