import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class App extends Application {
    DoubleProperty mouseX = new SimpleDoubleProperty();
    DoubleProperty mouseY = new SimpleDoubleProperty();
    IntegerProperty selectedButton = new SimpleIntegerProperty(-1);
    IntegerProperty selectedShape = new SimpleIntegerProperty(-1);
    BooleanProperty isInfoStageOpen = new SimpleBooleanProperty(false);
    BooleanProperty isHelpStageOpen = new SimpleBooleanProperty(false);
    Stage primaryStage;
    Border test;


    /**
     * test
     */
    @Override
    public void start(Stage _primaryStage) {
        test = new Border(new BorderStroke(Color.ALICEBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL));
        primaryStage = _primaryStage;
        Scene scene = new Scene(createContent(), 1920, 1080);

        primaryStage.setTitle("lab5");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(scene);
        primaryStage.show();
        mouseX.setValue(100.12);
    }
    
    /**
     * Main function
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the content of the main scene
     * @return
     */
    private Region createContent() {
        BorderPane root = new BorderPane();

        root.setCenter(createCenter());
        root.setBottom(createBottom());
        return root;
    }

    /**
     * Returns the content of center pane of main scene
     * @return
     */
    private Region createCenter() {
        AnchorPane canvas = new AnchorPane();
        canvas.setMaxSize(1920, 1080);
        canvas.setMinSize(1920, 1080);
        canvas.setPrefSize(1920, 1080);
        canvas.setStyle("-fx-background-color: white");

        canvas.setOnMouseMoved(event -> {
            mouseX.set(event.getX());
            mouseY.set(event.getY());
        });

        canvas.setOnMouseDragged(event -> {
            mouseX.set(event.getX());
            mouseY.set(event.getY());
        });

        canvas.setOnMouseExited(event -> {
            mouseX.set(-1);
            mouseY.set(-1);            
        });

        canvas.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    switch (selectedButton.get()) {
                        case 0:
                            canvas.getChildren().add(new Circle(mouseX.get(), mouseY.get(), 5));
                            break;
                        case 1:
                            canvas.getChildren().add(new Rectangle(mouseX.get(), mouseY.get(), 10 ,10));
                            break;
                        default:
                            break;
                    }
                break;
            
                default: break;
            }
            
        });

        ScrollPane center = new ScrollPane(canvas);
        center.setFitToHeight(true);
        center.setFitToWidth(true);
        center.setStyle("-fx-padding: 10px 0 0 10px;");
        return center;
    }


    /**
     * Returns the content of bottom pane of main scene
     * @return
     */
    private Region createBottom() {
        Label mX = new Label();
        Label mY = new Label();
        mX.textProperty().bindBidirectional(mouseX, new NumberStringConverter());
        mY.textProperty().bindBidirectional(mouseY, new NumberStringConverter());
        HBox cords = new HBox(mX, new Label(" : "), mY);
        cords.setAlignment(Pos.CENTER);
        cords.setMinWidth(50);

        HBox menu = new HBox(
            10,
            createMenuButton("circle", 0),
            createMenuButton("rect", 1),
            createMenuButton("poly", 2),
            createMenuButton("edit", 3)
            );
        menu.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(10, createHelpButton(), createInfoButton());
        buttons.setAlignment(Pos.CENTER);
        
        BorderPane bottom = new BorderPane();
        bottom.setMinHeight(40);
        bottom.setStyle("-fx-background-color: grey; -fx-padding: 0 10 0 10;");
        
        bottom.setLeft(cords);
        bottom.setCenter(menu);
        bottom.setRight(buttons);
        return bottom;
    }


    private Button createHelpButton() {
        Button button = new Button("HELP");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isHelpStageOpen.get()) {
                    Stage helpStage = createChildStage(primaryStage, isHelpStageOpen);
                    helpStage.setScene(new Scene(new HBox(4, new Label("help window"))));
                    helpStage.show();
                    isHelpStageOpen.set(true);
                }
            }
        });
        return button;
    }

    private Button createInfoButton() {
        Button button = new Button("INFO");
        String text = "info window";
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isInfoStageOpen.get()) {
                    Stage infoStage = createChildStage(primaryStage, isInfoStageOpen);
                    infoStage.setScene(new Scene(new HBox(4, new Label(text))));
                    infoStage.show();
                    isInfoStageOpen.set(true);
                }
            }
        });
        return button;
    }


    private Stage createChildStage(Stage owner, BooleanProperty flagPtr) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setOnHiding(e -> flagPtr.set(false));

        return stage;
    }


    private Button createMenuButton(String text, int id) {
        Button button = new Button(text);
        button.setOnAction(event -> {
           selectedButton.set(id); 
        });
        button.setBorder(selectedButton.isEqualTo(id).get() ? test : Border.EMPTY);

        return button;
    }

}