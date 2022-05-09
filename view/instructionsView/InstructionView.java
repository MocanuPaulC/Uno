package view.instructionsView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InstructionView extends BorderPane {
    // private Node attributes (controls)
    private Button back;
    private Text Title;
    private Line line;
    private VBox inputs;
    private Text Title2;
    private Line line2;
    private VBox inputs2;
    private VBox buttons;
    private Text instruction_text;
    private Text instruction_text2;

    private Button pageOne;

    private Button pageTwo;

    private Image special_card;



    public InstructionView() {
        initialiseNodes();
        layoutNodes();
    }



    public void initialiseNodes() {
        // create and configure controls

        back=new Button("Main Menu");
        pageOne=new Button("Previous page");
        pageTwo=new Button("Next page");

        //first page

        Title = new Text("Instruction");
        //design part of title
        Title.setFont(Font.font("Verdana",30));
        Title.setFill(Color.BLACK);
        //instruction text
        instruction_text = new Text("""
                Setup: The game is for 2 players (human and computer) Every player starts with seven cards. The rest of the
                  cards are placed in a Draw Pile face down. Next to the pile a space should be designated for a Discard Pile.
                  The top card should be placed in the Discard Pile, and the game begins!
                
                Game Play: you start the game in a clockwise direction. Every player views his/her cards and tries to match the
                  card in the Discard Pile.
                
                You have to match either by the number, color, or the symbol/Action. For instance, if the Discard Pile has a red
                  card that is an 8 you have to place either a red card or a card with an 8 on it. You can also play a Wild card
                  (which can alter current color in play).
                
                If the player has no matches or they choose not to play any of their cards even though they might have a match,
                  they must draw a card from the Draw pile. If that card can be played, play it. Otherwise, keep the card, and the
                  game moves on to the next person in turn. You can also play a Wild card, or a Wild Draw Four card on your
                  turn.
                
                                
                Note: If the first card turned up from the Draw Pile (to form the Discard Pile) is an Action card, the Action, a Wild
                  or a Wild Draw Four, you pick another card until you have a number card.\\n\s
                                
                Note: If the first card turned up from the Draw Pile (to form the Discard Pile) is an Action card, the Action from
                  that card applies and must be carried out by the first player (as stated, it is usually the player to the dealer’s
                  left). The exceptions are if a Wild or Wild Draw Four card is turned up.
                
                If it is a Wild card, the first player to start, can choose whatever color to begin play. If the first card is a Wild
                  Draw Four card – Return it to the Draw Pile, shuffle the deck, and turn over a new card. At any time during the
                  game, if the Draw Pile becomes depleted and no one has yet won the round, take the Discard Pile, shuffle it,
                  and turn it over to regenerate a new Draw Pile.
                
                                
                Take note that you can only put down one card at a time; you cannot stack two or more cards together on the
                  same turn. For example, you cannot put down a Draw Two on top of another Draw Two, or Wild Draw Four
                  during the same turn, or put down two Wild Draw Four cards together.
                
                The game continues until a player has one card left. The moment a player has just one card left in his deck,
                  they must click on the button “UNO!”. If they do not click on the button before the time runs out, that player must
                  draw four new cards as a penalty. Assuming that the player is unable to play/discard their last card and needs to
                  draw, but after drawing, is then able to play/discard that penultimate card, the player has to repeat the action of
                  clicking “Uno”. The bottom line is – Clicking “Uno” needs to be repeated every time you are left with one card.
               
                 Once a player has no cards remaining, the game is over.\n
                 Note, you cannot finish the game with a wild card(wild & wild draw 4).
                
                                
                     
                """);
        //design part of instruction text
        instruction_text.setFont(Font.font("Verdana",10));
        instruction_text.setFill(Color.GREEN);
        //create a new line
        line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(400);
        line.setEndY(200);
        line.setStrokeWidth(3);
        line.setStroke(Color.BLACK);
        inputs = new VBox();

        //second page

        Title2 = new Text("Instruction page 2");
        //design part of title
        Title2.setFont(Font.font("Verdana",30));
        Title2.setFill(Color.BLACK);
        //instruction text
        instruction_text2 = new Text("""
                          
                Action Cards: Besides the number cards, there are several other cards that help mix up the game. These are\n  called Action or Symbol cards.\n
                          
                Reverse – If going clockwise, switch to counterclockwise or vice versa. It can only be played on a card that\n  matches by color, or on another Reverse card.\n
                Skip – When a player places this card, the next player has to skip their turn. It can only be played on a card that\n  matches by color, or on another Skip card.\n
                Draw Two – When a person places this card, the next player will have to pick up two cards and forfeit his/her\n  turn. It can only be played on a card that matches by color, or on another Draw Two.\n
                Wild – This card represents all four colors, and can be placed on any card. The player has to state which color it\n  will represent for the next player. It can be played regardless of whether another card is available.\n
                Wild Draw Four – This acts just like the wild card except that the next player also has to draw four cards as well\n  as forfeit his/her turn.\n
                          
                          
                     
                """);
        //design part of instruction text
        instruction_text2.setFont(Font.font("Verdana",10));
        instruction_text2.setFill(Color.GREEN);
        //create a new line
        line2 = new Line();
        line2.setStartX(200);
        line2.setStartY(200);
        line2.setEndX(460);
        line2.setEndY(200);
        line2.setStrokeWidth(3);
        line2.setStroke(Color.BLACK);

        ImageView special_card = new ImageView(new Image("images_Uno_cards/For_instruction/Instruction_5_special_card.png"));
        special_card.setFitWidth(570);
        special_card.setFitHeight(150);
        inputs2 = new VBox();


        buttons = new VBox();
        inputs.getChildren().add(Title);
        inputs.getChildren().add(line);
        inputs.getChildren().add(instruction_text);
        inputs2.getChildren().add(Title2);
        inputs2.getChildren().add(line2);
        inputs2.getChildren().add(instruction_text2);
        inputs2.getChildren().add(special_card);
        buttons.getChildren().add(pageTwo);
        buttons.getChildren().add(back);




    }

    public void layoutNodes() {
        // add/set … methods
        this.setPadding(new Insets(20));
        this.setMinSize(500,600);

        buttons.setAlignment(Pos.CENTER);
        inputs.setAlignment(Pos.TOP_CENTER);
        this.setTop(inputs);
        this.setBottom(buttons);
        buttons.setSpacing(5);
        inputs.setSpacing(15);





    }
    public Button getPageOne() {
        return pageOne;
    }

    public Button getPageTwo() {
        return pageTwo;
    }

    public void setPageTwo(){
        this.getChildren().clear();
        inputs2.setAlignment(Pos.TOP_CENTER);
        this.setTop(inputs2);
        buttons.getChildren().clear();
        buttons.getChildren().add(pageOne);
        buttons.getChildren().add(back);
        this.setBottom(buttons);
        buttons.setAlignment(Pos.CENTER);
        inputs2.setSpacing(15);

    }

    public void setPageOne(){
        this.getChildren().clear();
        this.setBottom(buttons);
        this.setTop(inputs);
        buttons.getChildren().clear();
        buttons.getChildren().add(pageTwo);
        buttons.getChildren().add(back);
        buttons.setAlignment(Pos.CENTER);

    }



    // package-private Getters
    // for controls used by Presenter
    public Button getBack() {return back;}

}


