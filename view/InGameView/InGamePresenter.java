package view.InGameView;

import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import model.screens.Screens;
import model.statistics.GameSession;
import model.statistics.Turn;
import model.table.Card;
import view.MainMenuView.MainMenuView;

import view.endGameView.EndGamePresenter;
import view.endGameView.EndGameView;

import java.util.*;

public class InGamePresenter {
    private final GameSession model;
    private final InGameView view;

//    private Scene scene= new Scene();

    private final Random random = new Random();

    private final MainMenuView menuView;
    private final HashMap<String, Image> cardImages = new HashMap<>();
    private final HashMap<Button, Card> buttonStringHashMap = new HashMap<>();
//    private final Screens screens;


    public InGamePresenter(GameSession model, InGameView view, MainMenuView menuView) {
        this.view = view;
//        this.screens = screens;
        this.model = model;
        this.menuView = menuView;


        // mapping images to the string of the cards in order to make the buttons
        for (int i = 0; i < model.getGameTable().getTakeDeck().getCardList().size(); i++) {
            Card card = model.getGameTable().getTakeDeck().getCardList().get(i);

            String location = String.format("resources/images_Uno_cards/%s_cards/%s.png",
                    card.getColor().toLowerCase(Locale.ROOT),
                    card.toString().toLowerCase(Locale.ROOT));
//            System.out.println("location is "+location);
            cardImages.put(card.toString(), new Image(location));
            Button button = createButton(card);
            buttonStringHashMap.put(button, card);
            view.getAllButtons().add(button);
            view.getCardButtonHashMap().put(card, button);

        }

        this.addEventHandlers();

    }

    // creating a button with the provided image from the cardImagegs map
    public Button createButton(Card card) {
        Button button = new Button();
//        System.out.println(cardImages.get(card.toString()));
        button.setGraphic(new ImageView(cardImages.get(card.toString())));
        button.setBackground(null);

        return button;
    }

    public void addEventHandlers() {
        // uno call button appears only when a player is left with 1 card
        view.getUno().setOnAction(actionEvent -> {
            // it sets the other player active
            Turn.setOtherPlayerDontCareCard(model.getPlayerOne(), model.getPlayerTwo());
            this.view.getPlayerDeck().setDisable(false);

            model.checkUnoCall();
            // reset the center of the screen
            view.resetTableDeck();

            Turn.setOtherPlayerDontCareCard(model.getPlayerOne(), model.getPlayerTwo());
            updateView();
        });

        view.getScene().getWindow().setOnCloseRequest(windowEvent -> {
                model.getSeshStats().sessionEnd(model.getDbPass());
        });

        // begin the game
        view.getBeginGame().setOnAction(actionEvent -> {

            boolean firstTurnBot =menuView.getFirstTurn().getSelectedToggle().equals(menuView.getFirstTurnBot());
            // get first player from menu view
            model.setFirstPlayer(firstTurnBot);
            // remove the begin button
            view.getTableDecks().getChildren().remove(view.getBeginGame());
//            model.getGameTable().getPutDeck().getCardList().add(debugCard);

            if (model.isFirstSpecial(model.getTurn().getActualPlayer())) {
                setColorChanger(model.getGameTable().getPutDeck().getCardList().getLast().getTypeOfCard());
            }
//            Card firstPlayCard=model.getGameTable().getPutDeck().getCardList().getLast();
            // start of the first turn
            model.setStart(System.nanoTime());
            if (firstTurnBot) {
//                System.out.println("first turn bot");
                model.botTurn();
                updateView();
            }
            updateView();

        });

        view.getInstrInGame().setOnAction(actionEvent -> {

        });

        // adding event handlers for all 108 cards
        for (Button button : view.getCardButtonHashMap().values()) {
            button.setOnAction(actionEvent -> {
                try {
                    // plays the card that is mapped to the button
                    Card card = buttonStringHashMap.get(button);
//                    String cardType =
                    model.playCard(card);
//                    model.getTurn().getTurnStatistics().saveTurn();
                    setEndGameView();
                    if(!setColorChanger(card.getTypeOfCard())){
                        updateView();
                        model.botTurnCase(1);
                        setEndGameView();
                        updateView();
                    }
//                    updateView();
                } catch (InputMismatchException ex) {

                    System.out.println(ex.getMessage());
                }
            });
        }

        // button to get a card from take deck
        view.getTakeDeck().setOnAction(actionEvent -> {
            model.getGameTable().addCardToPlayer(model.getPlayerOne().getPlayerDeck());
            updateView();
            // make the bot make its turn
            model.botTurnCase(2);
            setEndGameView();
            updateView();
        });


        //debug end game
        view.getButton().setOnAction(actionEvent -> {
            model.endSession();

            EndGameView endView = new EndGameView();
            view.getScene().setRoot(endView);
            EndGamePresenter endGamePresenter = new EndGamePresenter(model, endView, menuView);
            // TO MAKE HERE
            endView.getScene().getWindow().sizeToScene();
        });

        // on color changing screen there are 4 buttons which are all here
        // they change the active color and change the table decks back to normal
        view.getRed().setOnAction(actionEvent -> {
            model.setActiveColor("RED");
            finishColorPick();

        });
        view.getGreen().setOnAction(actionEvent -> {
            model.setActiveColor("GREEN");
            finishColorPick();
        });
        view.getBlue().setOnAction(actionEvent -> {
            model.setActiveColor("BLUE");
            finishColorPick();
        });
        view.getYellow().setOnAction(actionEvent -> {
            model.setActiveColor("YELLOW");
            finishColorPick();
        });
//
//
    }

    // if one of the players has 1 card, the uno call is set in place
    public void setUnoCall() {
        if (model.getPlayerOne().getPlayerDeck().getCardList().size() == 1 || model.getPlayerTwo().getPlayerDeck().getCardList().size() == 1) {
            view.setUnoCall();
            model.setStartTimeTurn();
        }
    }


    public boolean setColorChanger(String cardType) {
        if (cardType.equals("WILD") || cardType.equals("WILDDRAW")) {
            view.setColorChanger();
            return true;
        }
        return false;


    }

    public void finishColorPick() {

        view.resetTableDeck();
        updateView();
        model.botTurnCase(1);
        setEndGameView();
        updateView();
    }


    public void setEndGameView() {
        if (model.getPlayerOne().getPlayerDeck().getCardList().size() == 0 || model.getPlayerTwo().getPlayerDeck().getCardList().size() == 0) {
            model.endSession();

            EndGameView endView = new EndGameView();
            EndGamePresenter endGamePresenter = new EndGamePresenter(model, endView, menuView);
            // TO MAKE HERE
            try{
                view.getScene().setRoot(endView);
                endView.getScene().getWindow().sizeToScene();
            }
            catch (NullPointerException ignored){
            }

        } else setUnoCall();

    }


    public void updateView() {
        // fills the view with model data

        view.getChildren().clear();
        updateEnemyDeck();
        updatePlayerDeck();
        // set the correct image of the put deck using the map
//        System.out.println(model.getGameTable().getPutDeck().getCardList().getLast().toString());
        view.getPutDeck().setImage(cardImages.get(model.getGameTable().getPutDeck().getCardList().getLast().toString()));
        view.getChildren().add(view.getTableDecks());
        view.getChildren().add(view.getEnemyDeck());
        view.getChildren().add(view.getPlayerDeck());
        if (!view.getTableDecks().getChildren().contains(view.getActiveColor()) && !view.getTableDecks().getChildren().contains(view.getUno())) {
            view.getTableDecks().getChildren().remove(view.getPutDeck());
            view.getTableDecks().getChildren().add(view.getActiveColor());
            view.getTableDecks().getChildren().add(view.getPutDeck());
        }
//        System.out.println("resources/images_Uno_cards/all_cards/"+model.getActiveColor().toLowerCase(Locale.ROOT)+"_card.png");
        if(!model.getActiveColor().equals("ALL")){
            view.getActiveColor().setImage(new Image("resources/images_Uno_cards/all_cards/" + model.getActiveColor().toLowerCase(Locale.ROOT) + "_card.png"));
        }
        else {
            view.getActiveColor().setImage(new Image("resources/images_Uno_cards/all_cards/blue_card.png"));

        }

        //debuggning if
        if(!view.getTableDecks().getChildren().contains(view.getButton()))view.getTableDecks().getChildren().add(view.getButton());
    }


    // updates how many cards the enemy player has
    public void updateEnemyDeck() {
        int nrOfEnemyCards = model.getGameTable().getPlayerTwoDeck().getCardList().size();
        view.updateEnemyDeck(nrOfEnemyCards);
    }


    // updates the buttons that the player has in his deck
    public void updatePlayerDeck() {
        List<Card> playerCardList = model.getPlayerOne().getPlayerDeck().getCardList();
        view.updatePlayerDeck(playerCardList);
    }


}
