import java.io.File;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class App extends Application {
    DoubleProperty width = new SimpleDoubleProperty();
    DoubleProperty height = new SimpleDoubleProperty();
    DoubleProperty mouseX = new SimpleDoubleProperty();
    DoubleProperty mouseY = new SimpleDoubleProperty();
    Stage primaryStage;
    Stage infoStage;
    Stage helpStage;


    @Override
    public void start(Stage primaryStag) {
        primaryStage = primaryStag;
        width.bind(primaryStage.widthProperty());
        height.bind(primaryStage.heightProperty());
        Scene scene = new Scene(createContent(), 1920, 1080);

        primaryStage.setTitle("lab5");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(scene);
        primaryStage.show();
        mouseX.setValue(100.12);
    }
    

    public static void main(String[] args) {
        launch(args);
    }


    private Region createContent() {
        BorderPane root = new BorderPane();

        root.setCenter(createCenter());
        root.setRight(createRight());
        root.setBottom(createBottom());
        return root;
    }


    private Region createCenter() {
        StackPane canvas = new StackPane(new Label("ccccc"));
        canvas.setMaxSize(1920, 1080);
        canvas.setMinSize(1920, 1080);
        canvas.setPrefSize(1920, 1080);
        canvas.setStyle("-fx-background-color: white");

        ScrollPane center = new ScrollPane(canvas);
        center.setFitToHeight(true);
        center.setFitToWidth(true);
        center.setStyle("-fx-border-color: grey; -fx-border-width: 10 0 0 10;");
        return center;
    }


    private Region createRight() {
        VBox right = new VBox(new Label("rrrr"));
        right.setMinWidth(100);
        right.setMaxWidth(300);
        right.prefWidthProperty().bind(width.multiply(0.2));
        right.setStyle("-fx-border-color: grey; -fx-border-width: 10 10 0 10;");

        return right;
    }


    private Region createBottom() {
        Label mX = new Label();
        Label comma = new Label(" : ");
        Label mY = new Label();
        mX.textProperty().bindBidirectional(mouseX, new NumberStringConverter());
        mY.textProperty().bindBidirectional(mouseY, new NumberStringConverter());
        HBox cords = new HBox(mX, comma, mY);

        BorderPane bottom = new BorderPane();
        bottom.setLeft(cords);
        BorderPane.setAlignment(cords, Pos.CENTER);

        

        HBox buttons = new HBox(10, createHelpButton(), createInfoButton());
        bottom.setRight(buttons);
        BorderPane.setAlignment(buttons, Pos.CENTER);

        bottom.setMinHeight(40);
        bottom.setStyle("-fx-background-color: grey; -fx-padding: 0 0 0 10;");

        return bottom;
    }


    private Button createHelpButton() {
        File imgFile = new File("img/fanf.jpeg");
        ImageView imgV = new ImageView(imgFile.toURI().toString());

        Button button = new Button("INFO", imgV);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (helpStage == null) {
                    helpStage = createChildStage(primaryStage);
                    helpStage.setScene(new Scene(new HBox(4, new Label("help window"))));
                    helpStage.show();
                }
            }
        });
        return button;
    }

    private Button createInfoButton() {
        Button button = new Button("INFO");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (infoStage == null) {
                    infoStage = createChildStage(primaryStage);
                    infoStage.setScene(new Scene(new HBox(4, new Label("info window"))));
                    infoStage.show();
                }
            }
        });
        return button;
    }


    private Stage createChildStage(Stage owner) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setOnHiding(e -> infoStage = null);

        return stage;
    }

}