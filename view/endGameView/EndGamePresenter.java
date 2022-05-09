package view.endGameView;

import model.statistics.GameSession;
import view.MainMenuView.MainMenuView;

public class EndGamePresenter {


    private final GameSession model;
    private final EndGameView view;
    private final MainMenuView menuView;
    private final int cardsPlayed;
    private final int plusFoursPlayed;
    private final int playerScore;

    public EndGamePresenter(GameSession model, EndGameView view, MainMenuView menuView) {
        this.model = model;
        this.view = view;
        this.menuView=menuView;
        // to remove this and add database thing
        cardsPlayed=model.getSeshStats().getCardsPlayed(model.getDbPass(), model.getPlayerOne().getName());
        playerScore=model.getPlayerTwo().getPlayerDeck().scoreAfterLoss();
        plusFoursPlayed=model.getSeshStats().getPlusFourPlayed(model.getDbPass(),model.getPlayerOne().getName());

        model.getSeshStats().updateSeshScore(model.getDbPass(), playerScore);
        addEventHandlers();
        updateView();

    }

    private void addEventHandlers(){

        //once the button 'back' is pressed the user get back in the main menu
        view.getBack().setOnAction(actionEvent -> {
            view.getScene().setRoot(menuView);
            menuView.getScene().getWindow().sizeToScene();
        });

    }

    private void updateView(){
        //show to the user the statistics of the game
        view.getStat1().setText("Cards Played: "+cardsPlayed);
        view.getStat2().setText(String.format("Avg turn duration: %.5f", model.getSeshStats().getSeshAvgTurnDuration(model.getDbPass(), model.getPlayerOne().getName())));
        view.getStat3().setText("Turn Duration Outliers: " + model.getSeshStats().getTurnOutliersLastSesh(model.getDbPass(), model.getPlayerOne().getName()).toString());
        view.getStat4().setText("Total Plus 4s Played: "+plusFoursPlayed);
        view.getStat5().setText("Score: "+playerScore);
        view.getChart().setData(model.getSeshStats().getFrequenciesOfTurnDurations(model.getDbPass(), model.getPlayerOne().getName()));
//
    }



}
