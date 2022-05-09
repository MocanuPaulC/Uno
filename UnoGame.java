import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.statistics.GameSession;
import view.MainMenuView.*;

public class UnoGame extends Application {
    @Override
    public void start(Stage Stage){
        //game logic part
        GameSession model = new GameSession("bot");

        //java fx part

        //view of the main menu
        MainMenuView view = new MainMenuView();
        //create the presenter of MainMenu + connect the model(Game logic)
        //add an icon to the scene
        Image icon = new Image("images_Uno_cards/all_cards/Uno_card.png");
        Stage.getIcons().add(icon);
        //set title of the scene
        Stage.setTitle("Uno Game");
        //the user cannot resize the page
        Stage.setResizable(false);
        //set the scene to the stage
        Stage.setScene(new Scene(view));

//        Stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent windowEvent) {
//
//            }
//        });
//        // i need to do this, otherwise it doesn't work
        new MainMenuPresenter(model,view);

        //css stuff
        //String css = Objects.requireNonNull(this.getClass().getResource("styleSheet.css")).toExternalForm();
        //view.getStylesheets().add(css);

        //show the stage
        Stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }}
