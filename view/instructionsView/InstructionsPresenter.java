package view.instructionsView;

//import model.screens.Screens;
import view.MainMenuView.MainMenuView;
import view.statisticsView.StatisticsView;

public class InstructionsPresenter {
//    private final Screens model;
    private final InstructionView view;
    private final MainMenuView menuView;

    public InstructionsPresenter( InstructionView view, MainMenuView menuView) {
//        this.model = model;
        this.view = view;
        this.menuView = menuView;

        addEventHandlers();
        updateView();
    }

    private void addEventHandlers(){

        //        view.getScene().getWindow().setOnCloseRequest(event -> System.out.println("works"));
        view.getPageTwo().setOnAction(actionEvent ->{view.setPageTwo();} );

        view.getPageOne().setOnAction(actionEvent -> {view.setPageOne();});



        this.view.getScene().getWindow().setOnCloseRequest(event -> {

                System.out.println("finishedINInstru");

        });

        view.getBack().setOnAction(actionEvent -> {
            view.getScene().setRoot(menuView);
            menuView.getScene().getWindow().sizeToScene();
        });



    }


    private void updateView(){
        // fills the view with model data


    }



}
