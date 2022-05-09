package view.MainMenuView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class MainMenuView extends BorderPane {
    // private Node attributes (controls)
    private ImageView image;
    private Button startGame;
    private Button instructions;
   // private Button leaderboard;
    private Button statistics;
    private Text textTitle;
    private Text prompt_player;
    private Text player_cpu;
    private Text password_prompt;
    private Line line;
    private TextField name;
    private PasswordField password;
    private VBox buttonList;
    private VBox inputs;
    private RadioButton firstTurnHuman;
    private RadioButton firstTurnBot;
    ToggleGroup firstTurn;



    public MainMenuView() {
        initialiseNodes();
        layoutNodes();
    }


    public void initialiseNodes() {
        //create and configure controls
        image = new ImageView(new Image("images_Uno_cards/all_cards/all_50_wild.png"));
        startGame = new Button("Start game!");
        instructions = new Button("Instructions");
      //leaderboard = new Button("Leaderboard");
        statistics = new Button("Statistics");
        textTitle = new Text("Welcome in Uno");
        password = new PasswordField();
        //design part of title
        textTitle.setFont(Font.font("Verdana",50));
        textTitle.setFill(Color.RED);
        //create a new line
        line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(600);
        line.setEndY(200);
        line.setStrokeWidth(3);
        line.setStroke(Color.RED);
        //prompt for the player name + text field
        prompt_player = new Text("Please, enter your name :");
        prompt_player.setFont(Font.font("Verdana",8));
        prompt_player.setFill(Color.GREY);
        name = new TextField("Sebastien");
        //prompt for the player password to postgres + text field
        password_prompt = new Text("Please, enter your password (postgres) :");
        password_prompt.setFont(Font.font("Verdana",8));
        password_prompt.setFill(Color.GREY);
        //prompt for the player, allow the player to choose who should start the game (CPU or human)
        player_cpu = new Text("Who should start ? :");
        player_cpu.setFont(Font.font("Verdana",8));
        player_cpu.setFill(Color.GREY);
        firstTurn = new ToggleGroup();
        this.firstTurnHuman = new RadioButton("You");
        firstTurnHuman.setToggleGroup(firstTurn);
        this.firstTurnBot = new RadioButton("Computer");
        firstTurnBot.setToggleGroup(firstTurn);
        firstTurnHuman.setSelected(true);
        buttonList=new VBox();
        inputs = new VBox();
        inputs.getChildren().add(textTitle);
        inputs.getChildren().add(line);
        inputs.getChildren().add(image);
        inputs.getChildren().add(prompt_player);
        inputs.getChildren().add(name);
        inputs.getChildren().add(password_prompt);
        inputs.getChildren().add(password);
        inputs.getChildren().add(player_cpu);
        inputs.getChildren().add(firstTurnHuman);
        inputs.getChildren().add(firstTurnBot);
        this.setTop(inputs);
        buttonList.getChildren().add(startGame);
        buttonList.getChildren().add(instructions);
        //button for leaderboard(do not remove)
      //buttonList.getChildren().add(leaderboard);
        buttonList.getChildren().add(statistics);

    }


    public void layoutNodes() {
        // add/set … methods
        this.setPadding(new Insets(20));
        inputs.setPadding(new Insets(20));
        inputs.setSpacing(10);
        buttonList.setSpacing(10);
        this.setCenter(buttonList);
        buttonList.setAlignment(Pos.BASELINE_CENTER);

        for(Node i : buttonList.getChildren()){
            ((Button) i).setMinWidth(100);}
        this.setMinSize(500,600);
    }


    // package-private Getters
    // for controls used by Presenter
    public ToggleGroup getFirstTurn() {
        return firstTurn;
    }

    public TextField getName() {return name;}

    public Button getStartGame() {return startGame;}

    public Button getInstructions() {return instructions;}
    //do not remove getter for leaderboard
   // public Button getLeaderboard() {return leaderboard;}

    public RadioButton getFirstTurnHuman() {return firstTurnHuman;}

    public RadioButton getFirstTurnBot() {return firstTurnBot;}

    public Button getStatistics() {return statistics;}

    public PasswordField getPassword() {return password;}
}
