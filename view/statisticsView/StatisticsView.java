package view.statisticsView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class StatisticsView extends BorderPane {
    // private Node attributes (controls)
    private Button back;
    private Text Title;
    private Line line;
    private VBox inputs;
    private Text stat1 ;
    private Text stat2 ;
    private Text stat3 ;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private BarChart<String,Number> bc;
    private XYChart.Series dataSeries;

    private PieChart colors;
    private PieChart types;

    private Button pageOne;
    private Button pageTwo;

    private VBox buttons;

    private VBox charts;


    private  void initialiseNodes(){
        //create and configure controls
        back=new Button("Main Menu");
        pageOne=new Button("Previous page");
        pageTwo=new Button("Next page");

        buttons=new VBox();
        //create a Chart with the data from all the previous game played by the user
        xAxis=new CategoryAxis();
        yAxis=new NumberAxis();
        bc=new BarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Seconds");
        yAxis.setLabel("Number of sessions");
        dataSeries = new XYChart.Series();
        Title = new Text("Statistics");
        //design part of title
        Title.setFont(Font.font("Verdana",30));
        Title.setFill(Color.BLACK);
        //create a new line
        line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(400);
        line.setEndY(200);
        line.setStrokeWidth(3);
        line.setStroke(Color.BLACK);
        //create new stat
        stat1 = new Text("Total games played: 242");
        stat2 = new Text("Score Last Session: %d \n Avg session score: %d");
        stat3 = new Text("Total Cards played: 34532");
        inputs = new VBox();
        inputs.getChildren().add(Title);
        inputs.getChildren().add(line);
        inputs.getChildren().add(stat1);
        inputs.getChildren().add(stat2);
        inputs.getChildren().add(stat3);

        colors=new PieChart();
        colors.setLegendVisible(false);
        colors.setTitle("Frequencies of colors played");
        types=new PieChart();
        types.setLegendVisible(false);
        types.setTitle("Frequencies of types played");

        charts=new VBox();
        charts.getChildren().add(colors);
        charts.getChildren().add(types);
        buttons.getChildren().add(pageTwo);
        buttons.getChildren().add(back);




    }

    private void layoutNodes(){
        // add/set … methods
        back.setMinWidth(100);
        pageOne.setMinWidth(100);
        pageTwo.setMinWidth(100);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(15);
        bc.setLegendVisible(false);
        this.setPadding(new Insets(20));
        this.setMinSize(500,600);
        this.setBottom(buttons);
        this.setTop(inputs);
        this.setCenter(bc);
//        colors.setLabelsVisible(false);

        inputs.setSpacing(30);
        inputs.setAlignment(Pos.BASELINE_CENTER);




    }

    public StatisticsView(){
        initialiseNodes();
        layoutNodes();
    }

    Text getStat1() {
        return stat1;
    }

    Text getStat2() {
        return stat2;
    }

    Text getStat3() {
        return stat3;
    }

    BarChart<String, Number> getBc() {
        return bc;
    }

    XYChart.Series getDataSeries() {
        return dataSeries;
    }


    public PieChart getColors() {
        return colors;
    }

    public PieChart getTypes() {
        return types;
    }

    public Button getPageOne() {
        return pageOne;
    }

    public Button getPageTwo() {
        return pageTwo;
    }

    public void setPageTwo(){
        this.getChildren().clear();
        this.setTop(colors);
        this.setCenter(types);
        buttons.getChildren().clear();
        buttons.getChildren().add(pageOne);
        buttons.getChildren().add(back);
        this.setBottom(buttons);
        buttons.setAlignment(Pos.CENTER);

    }

    public void setPageOne(){
        this.getChildren().clear();
//        this.setTop();
        this.setBottom(buttons);
        this.setTop(inputs);
        this.setCenter(bc);
        buttons.getChildren().clear();
        buttons.getChildren().add(pageTwo);
        buttons.getChildren().add(back);
        buttons.setAlignment(Pos.CENTER);

    }
    // package-private Getters
    // for controls used by Presenter
    Button getBack() {
        return back;
    }
}
