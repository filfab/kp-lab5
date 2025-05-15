import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class App extends Application {
    /**
     * Property containing the x mouse position on the canvas.
     * If the cursor is outside of the canvas, the value is -1.
     */
    DoubleProperty mouseX = new SimpleDoubleProperty();
    /**
     * Property containing the y mouse position on the canvas.
     * If the cursor is outside of the canvas, the value is -1.
     */
    DoubleProperty mouseY = new SimpleDoubleProperty();
    /**
     * Property containing id of the selested menu button.
     * If no button is selected, the value is -1.
     */
    IntegerProperty selectedButton = new SimpleIntegerProperty(-1);
    /**
     * Array of flag properties informing whether respective popup window is open.
     */
    BooleanProperty[] openWindowFlags;
    /**
     * The primary stage.
     */
    Stage primaryStage;

    /**
     * The main method.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage _primaryStage) {
        primaryStage = _primaryStage;
        Scene scene = new Scene(createContent(), 1920, 1080);

        primaryStage.setTitle("lab5");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Returns the contents of the main scene.
     * @return  the contents of the main scene
     */
    private Region createContent() {
        BorderPane root = new BorderPane();

        root.setCenter(createCenter());
        root.setBottom(createBottom());
        return root;
    }

    /**
     * Returns the contents of the center section of the main anchor pane.
     * @return  the contents of the center section of the main anchor pane
     */
    private Region createCenter() {
        AnchorPane canvas = new Utils.Canvas(mouseX, mouseY, selectedButton);
        canvas.setMaxSize(1920, 1080);
        canvas.setMinSize(1920, 1080);
        canvas.setPrefSize(1920, 1080);
        canvas.setStyle("-fx-background-color: white");

        ScrollPane center = new ScrollPane(canvas);
        center.setFitToHeight(true);
        center.setFitToWidth(true);
        center.setStyle("-fx-padding: 10px 0 0 10px;");
        return center;
    }

    /**
     * Returns the contents of the bottom section of the main anchor pane.
     * @return  the contents of the bottom section of the main anchor pane
     */
    private Region createBottom() {
        HBox cords = new Utils.Cords(mouseX, mouseY);

        String[] labels = new String[]{"circle", "rect", "poly" ,"edit"};
        Button[] buttons = new Button[labels.length];
        for (int i=0; i<labels.length; i++) {
            buttons[i] = new Utils.MenuButton(labels[i], i, selectedButton);
        }
        HBox menu = new HBox(10, buttons);
        menu.setAlignment(Pos.CENTER);


        labels = new String[]{"info", "help"};
        openWindowFlags = new BooleanProperty[labels.length];
        buttons = new Button[labels.length];
        for (int i=0; i<labels.length; i++) {
            openWindowFlags[i] = new SimpleBooleanProperty(false);
            buttons[i] = new Utils.InfoButton(labels[i], primaryStage, openWindowFlags[i]);
        }
        HBox info = new HBox(10, buttons);
        info.setAlignment(Pos.CENTER);
        info.setMinWidth(80);

        
        BorderPane bottom = new BorderPane();
        bottom.setMinHeight(40);
        bottom.setStyle("-fx-background-color: grey; -fx-padding: 0 10 0 10;");
        bottom.setLeft(cords);
        bottom.setCenter(menu);
        bottom.setRight(info);
        return bottom;
    }

}