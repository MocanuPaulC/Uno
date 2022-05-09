package view.InGameView;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.table.Card;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InGameView extends BorderPane {
    // private Node attributes (controls)
    private Button uno;

    private Button instrInGame;
    private HBox enemyDeck;
    private HBox tableDecks;
    private LinkedList<Button> allButtons;
    private HashMap<Card, Button> cardButtonHashMap;
    private LinkedList<ImageView> enemyCardList;
    private Button takeDeck;
    private ImageView putDeck;
    private ImageView activeColor;
    private HBox playerDeck;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem instr;
    private MenuItem goMainMenu;
    private Button beginGame;
    //    private Button debug;
    private HBox prevPlayeDeck;
    Button button = new Button("debug");

    // private Node attributes (controls)
    private HBox topColor;
    private HBox botColor;
    VBox topBox;
    private VBox colorBtns;
    private Button red;
    private Button blue;
    private Button yellow;
    private Button green;

    public HashMap<Card, Button> getCardButtonHashMap() {
        return cardButtonHashMap;
    }

    public HBox getTableDecks() {
        return tableDecks;
    }

    public HBox getPlayerDeck() {
        return playerDeck;
    }

    public LinkedList<Button> getAllButtons() {
        return allButtons;
    }


    public Button getUno() {
        return uno;
    }

    public void initialiseNodes() {
        topBox=new VBox();
        instr = new MenuItem("Instructions");
        goMainMenu = new MenuItem("Main Menu");
        menu = new Menu("Menu",null, instr,goMainMenu);
        menuBar = new MenuBar(menu);
        uno = new Button();
        uno.setGraphic(new ImageView(new Image("images_Uno_cards/all_cards/Uno_big_button.png")));
        uno.setBackground(null);
        takeDeck = new Button();
        takeDeck.setGraphic(new ImageView(new Image("images_Uno_cards/all_cards/Uno_card.png")));
        takeDeck.setBackground(null);
        putDeck = new ImageView(new Image("images_Uno_cards/all_cards/Uno_card.png"));
        enemyCardList = new LinkedList<>();
        enemyDeck = new HBox();
        playerDeck = new HBox();
        prevPlayeDeck = new HBox();
        tableDecks = new HBox();
        beginGame = new Button("Start!");
        activeColor = new ImageView();
        colorBtns = new VBox();
        instrInGame = new Button("instructions");
        tableDecks.getChildren().add(takeDeck);
        tableDecks.getChildren().add(beginGame);
        tableDecks.getChildren().add(activeColor);
        tableDecks.getChildren().add(putDeck);
        cardButtonHashMap = new HashMap<>();
        allButtons = new LinkedList<>();
        topColor = new HBox();
        botColor = new HBox();
        red = new Button();
        red.setGraphic(new ImageView(new Image("images_Uno_cards/all_cards/red_card.png")));
        red.setBackground(null);
        blue = new Button();
        blue.setGraphic(new ImageView(new Image("images_Uno_cards/all_cards/blue_card.png")));
        blue.setBackground(null);
        yellow = new Button();
        yellow.setGraphic(new ImageView(new Image("images_Uno_cards/all_cards/yellow_card.png")));
        yellow.setBackground(null);
        green = new Button();
        green.setGraphic(new ImageView(new Image("images_Uno_cards/all_cards/green_card.png")));
        green.setBackground(null);


//


    }

    public void updatePlayerDeck(List<Card> playerCardList) {

        playerDeck.getChildren().clear();
        for (Card card : playerCardList) {
            if (!playerDeck.getChildren().contains(cardButtonHashMap.get(card)) && cardButtonHashMap.get(card) != null) {

//                    System.out.println("");
                playerDeck.getChildren().add(cardButtonHashMap.get(card));

            }
        }
    }


    public void setColorChanger() {
        playerDeck.setDisable(true);
        tableDecks.getChildren().clear();
        layoutColor();
        colorBtns.setAlignment(Pos.CENTER);
        tableDecks.getChildren().add(colorBtns);
    }

    public void updateEnemyDeck(int nrOfEnemyCards) {
        enemyDeck.getChildren().clear();
        for (int i = 0; i < nrOfEnemyCards; i++) {
//            System.out.println("added "+i+" cards");
            enemyDeck.getChildren().add(new ImageView(new Image("images_Uno_cards/all_cards/Uno_card.png")));
        }
    }

    public void resetTableDeck() {

        playerDeck.setDisable(false);
        tableDecks.getChildren().clear();
        tableDecks.getChildren().add(takeDeck);
        tableDecks.getChildren().add(putDeck);
    }

    public void setUnoCall() {
        playerDeck.setDisable(true);
        tableDecks.getChildren().clear();
        tableDecks.getChildren().add(uno);
    }

    Button getButton() {
        return button;
    }


    public ImageView getActiveColor() {
        return activeColor;
    }

    public void layoutColor() {
        colorBtns.getChildren().clear();
        topColor.getChildren().clear();
        botColor.getChildren().clear();
        colorBtns.getChildren().add(topColor);
        colorBtns.getChildren().add(botColor);
        topColor.getChildren().add(red);
        topColor.getChildren().add(blue);
        botColor.getChildren().add(yellow);
        botColor.getChildren().add(green);
        colorBtns.setSpacing(30);

    }

    public Button getInstrInGame() {
        return instrInGame;
    }

    public VBox getColorBtns() {
        return colorBtns;
    }

    public Button getRed() {
        return red;
    }

    public Button getBlue() {
        return blue;
    }

    public Button getYellow() {
        return yellow;
    }

    public Button getGreen() {
        return green;
    }

    public void layoutNodes() {
        this.setLeft(instrInGame);
        instrInGame.setAlignment(Pos.TOP_CENTER);
        enemyDeck.setAlignment(Pos.TOP_CENTER);
        enemyDeck.setMinSize(50, 50);
        playerDeck.setAlignment(Pos.BOTTOM_CENTER);
        playerDeck.setMinSize(50, 50);
        tableDecks.setAlignment(Pos.CENTER);
        this.setMinSize(1200, 800);


//        topBox.getChildren().add(menuBar);
//        topBox.getChildren().add(enemyDeck);
//        this.setTop(topBox);
        this.setTop(enemyDeck);
        this.setCenter(tableDecks);
        this.setBottom(playerDeck);
    }


    public HBox getEnemyDeck() {
        return enemyDeck;
    }


    public Button getTakeDeck() {
        return takeDeck;
    }

    public ImageView getPutDeck() {
        return putDeck;
    }

    public Button getBeginGame() {
        return beginGame;
    }

    public InGameView() {
        initialiseNodes();
        layoutNodes();
    }


}

