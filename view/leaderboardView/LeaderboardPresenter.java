package view.leaderboardView;

import javafx.stage.Window;
//import model.screens.Screens;
import view.MainMenuView.MainMenuView;
import view.instructionsView.InstructionView;

public class LeaderboardPresenter {

//    private final Screens model;
    private final LeaderboardView view;
    private final MainMenuView menuView;

    public LeaderboardPresenter( LeaderboardView view, MainMenuView menuView) {
//        this.model = model;
        this.view = view;
        this.menuView = menuView;

        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){
        // Add event handlers (inner classes or
        // lambdas) to view controls.
        //once the button 'back' is pressed the user get back in the main menu
        view.getBack().setOnAction(actionEvent -> {
            view.getScene().setRoot(menuView);
            menuView.getScene().getWindow().sizeToScene();
        });
    }

    private void updateView(){
        // fills the view with model data

    }



}
