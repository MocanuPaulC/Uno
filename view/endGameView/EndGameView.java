package view.endGameView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EndGameView extends BorderPane {
    // private Node attributes (controls)


    private Button back;

    private Text Title;
    private Line line;
    private VBox inputs;
    private Text stat1 ;
    private Text stat2;
    private Text stat3;
    private Text stat4 ;
    private Text stat5 ;
    private PieChart chart;



    public EndGameView(){
        initialiseNodes();
        layoutNodes();
    }


    private  void initialiseNodes(){
        //create and configure controls
        back=new Button("back!");

        Title = new Text("Session Statistics");
        // design of title
        Title.setFont(Font.font("Verdana",30));
        Title.setFill(Color.BLACK);
        //create new line
        line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(400);
        line.setEndY(200);
        line.setStrokeWidth(3);
        line.setStroke(Color.BLACK);
        //statistics
        stat1 = new Text("Cards Played: ");
        stat2 = new Text("Average turn time: ");
        stat3 = new Text("Turn Duration Outliers: ");
        stat4 = new Text("Total Plus 4s Dealt: 343");
        stat5 = new Text("Player Score: 3453");
        //create a pieChart using statistics
        chart = new PieChart();
        inputs = new VBox();
        inputs.getChildren().add(Title);
        inputs.getChildren().add(line);
        inputs.getChildren().add(stat1);
        inputs.getChildren().add(stat2);
        inputs.getChildren().add(stat3);
        inputs.getChildren().add(stat4);
        inputs.getChildren().add(stat5);



    }


    private void layoutNodes(){
        // add/set … methods
        this.setCenter(chart);
        chart.setMinSize(300,300);
        chart.setLayoutY(-400);
        this.setPadding(new Insets(20));
        this.setMinSize(500,600);
        this.setBottom(back);
        this.setTop(inputs);
        inputs.setSpacing(30);
        inputs.setAlignment(Pos.BASELINE_CENTER);
        chart.setTitle("Duration of turns in seconds");
        chart.setLegendVisible(false);
    }

    public Button getBack() {
        return back;
    }

    public Text getStat3() {
        return stat3;
    }

    public Text getStat1() {
        return stat1;
    }

    public Text getStat2() {
        return stat2;
    }


    public Text getStat4() {
        return stat4;
    }


    public Text getStat5() {
        return stat5;
    }

    public PieChart getChart() {
        return chart;
    }






}
