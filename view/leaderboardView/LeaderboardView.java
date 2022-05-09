package view.leaderboardView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LeaderboardView extends BorderPane {
    // private Node attributes (controls)
    private Button back;
    private Text Title;
    private Line line;
    private VBox inputs;
    private VBox inputs_player;
    private Text player1 ;
    private Text player2 ;
    private Text player3 ;
    private Text player4 ;
    private Text player5 ;



    public LeaderboardView(){
        initialiseNodes();
        layoutNodes();
    }

    private  void initialiseNodes(){
        //create and configure controls
        back=new Button("back");

        Title = new Text("Leaderboard");
        //design part of title
        Title.setFont(Font.font("Verdana",30));
        Title.setFill(Color.BLACK);
        //create a line
        line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(400);
        line.setEndY(200);
        line.setStrokeWidth(3);
        line.setStroke(Color.BLACK);
        inputs_player = new VBox();
        //test for leaderboard
        player1 = new Text("First place:   Seb");
        player2 = new Text("Second place:   John");
        player3 = new Text("Third place:   Marc");
        player4 = new Text("Fourth place:   Carlos");
        player5 = new Text("Fifth place:   Paul");
        inputs = new VBox();
        inputs.getChildren().add(Title);
        inputs.getChildren().add(line);

        inputs_player.getChildren().add(player1);
        inputs_player.getChildren().add(player2);
        inputs_player.getChildren().add(player3);
        inputs_player.getChildren().add(player4);
        inputs_player.getChildren().add(player5);







    }

    private void layoutNodes(){
        // add/set … methods
        this.setPadding(new Insets(20));
        this.setMinSize(500,600);
        this.setBottom(back);
        this.setTop(inputs);
        this.setCenter(inputs_player);

        inputs_player.setSpacing(40);
        inputs_player.setAlignment(Pos.BASELINE_CENTER);
        inputs.setSpacing(20);
        inputs.setAlignment(Pos.BASELINE_CENTER);




    }

    // package-private Getters
    // for controls used by Presenter
    public Button getBack() {
        return back;
    }
}
