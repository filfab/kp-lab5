import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
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
    Buttons.ButtonSelector selectedButton = new Buttons.ButtonSelector();
    /**
     * Array of flag properties informing whether respective popup window is open.
     */
    BooleanProperty[] openWindowFlags;
    /**
     * The primary stage (main window) of the application.
     */
    Stage primaryStage;
    /**
     * Color picker allowing to choose color.
     */
    ColorPicker selectedColor = new ColorPicker(Color.BLACK);
    /**
     * The main entry point for the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application. Sets the scene and shows the primary stage.
     *
     * @param _primaryStage the primary stage provided by the JavaFX runtime
     */
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
     * Creates the main layout content of the application.
     * It includes the central drawing canvas and the bottom control panel.
     *
     * @return the root layout node of the scene
     */
    private Region createContent() {
        BorderPane root = new BorderPane();

        root.setCenter(createCenter());
        root.setBottom(createBottom());
        return root;
    }

    /**
     * Creates the center section of the UI containing the drawing canvas.
     *
     * @return a scrollable pane containing the drawing canvas
     */
    private Region createCenter() {
        AnchorPane canvas = new Canvas(mouseX, mouseY, selectedButton, selectedColor);
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
     * Creates the bottom section of the UI containing coordinate display,
     * shape selection buttons, and informational/help buttons.
     *
     * @return a layout node containing bottom controls
     */
    private Region createBottom() {
        // Coordinate display
        HBox cords = new Cords(mouseX, mouseY);

        // Selection buttons
        HBox menu = new HBox(10);
        Button[] buttons = new Button[Buttons.values().length];
        for (Buttons type : Buttons.values()) {
            menu.getChildren().addAll(new MenuButton(type.toString(), type, selectedButton));
        }
        menu.getChildren().addAll(selectedColor);
        menu.setAlignment(Pos.CENTER);

        // Info and Help buttons
        String[] labels = new String[]{"info", "help"};
        openWindowFlags = new BooleanProperty[labels.length];
        buttons = new Button[labels.length];
        for (int i=0; i<labels.length; i++) {
            openWindowFlags[i] = new SimpleBooleanProperty(false);
            buttons[i] = new InfoButton(labels[i], primaryStage, openWindowFlags[i]);
        }
        HBox info = new HBox(10, buttons);
        info.setAlignment(Pos.CENTER);
        info.setMinWidth(80);

        // Bottom panel layout
        BorderPane bottom = new BorderPane();
        bottom.setMinHeight(40);
        bottom.setStyle("-fx-background-color: grey; -fx-padding: 0 10 0 10;");
        bottom.setLeft(cords);
        bottom.setCenter(menu);
        bottom.setRight(info);
        return bottom;
    }


    /**
     * A custom menu button that updates a shared IntegerProperty when clicked.
     */
    public static class MenuButton extends Button {
        /**
         * Constructs a new MenuButton.
         *
         * @param text            the text displayed on the button
         * @param id              the unique ID associated with this button
         * @param selectedButton  a shared property representing the selected button's ID
         */
        MenuButton(String text, Buttons shape, Buttons.ButtonSelector selectedButton) {
            super(text);

            this.setOnAction(event -> {
                selectedButton.set(shape);
            });
        }
    }


    /**
     * A button that opens a child stage displaying information loaded from a file.
     */
    public static class InfoButton extends Button {
        /**
         * Constructs an InfoButton that opens a window with content from a text file.
         *
         * @param text        the name of the file (without extension) to load / button label
         * @param primaryStage the parent stage for modal positioning
         * @param isOpen       property indicating whether the info window is already open
         */
        InfoButton(String text, Stage primaryStage, BooleanProperty isOpen) {
            super(text);

            this.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!isOpen.get()) {
                        VBox content = new VBox();
                        try {
                            File file = new File("./resources/" + text + ".txt");
                            Scanner stream = new Scanner(file);
                            while (stream.hasNextLine()) {
                                content.getChildren().add(new Label(stream.nextLine()));
                            }

                            stream.close();
                        } catch (FileNotFoundException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                        Stage infoStage = new ChildStage(content, primaryStage, isOpen);
                        infoStage.show();
                        isOpen.set(true);
                    }
                }
            });
        }
    }


    /**
     * A secondary window (child stage) for displaying informational content.
     */
    public static class ChildStage extends Stage {
        /**
         * Constructs a non-resizable child stage with given content.
         *
         * @param content  the VBox containing the content to display
         * @param owner    the parent stage
         * @param isOpen   property to reset when the window is closed
         */
        ChildStage(VBox content, Stage owner, BooleanProperty isOpen) {
            super();
            content.setAlignment(Pos.CENTER);
            this.setScene(new Scene(content, 400, 400));
            this.setResizable(false);
            this.initOwner(owner);
            this.setOnCloseRequest(event -> {
                isOpen.set(false);
            });
        }
    }


    /**
     * A custom canvas pane for drawing and previewing Buttons (circle, rectangle, polygon).
     */
    public static class Canvas extends AnchorPane {
        private javafx.scene.shape.Circle rotateCircle = null;
        private Shape shapePreview = null;
        private Shape selectedShape = null;
        private DoubleProperty pivotX = new SimpleDoubleProperty();
        private DoubleProperty pivotY = new SimpleDoubleProperty();
        private boolean isRotating = false;

        /**
         * Constructs a new Canvas that tracks mouse events and allows shape drawing.
         *
         * @param mouseX         a property holding the current mouse X coordinate
         * @param mouseY         a property holding the current mouse Y coordinate
         * @param selectedButton an IntegerProperty representing the selected shape tool
         */
        Canvas(DoubleProperty mouseX, DoubleProperty mouseY, Buttons.ButtonSelector selectedButton, ColorPicker selectedColor) {
            super();
            rotateCircle = new javafx.scene.shape.Circle();
            rotateCircle.setRadius(10);
            rotateCircle.setFill(Color.BLUE);
            rotateCircle.setStroke(Color.BLUE);
            rotateCircle.centerXProperty().bind(pivotX);
            rotateCircle.centerYProperty().bind(pivotY);
            rotateCircle.setOnMouseDragged(event -> {
                isRotating = true;
                if (selectedShape instanceof Rotatable) {
                    ((Rotatable) selectedShape).rotate(event.getX(), event.getY());
                }
             });
             rotateCircle.setOnMouseReleased(event -> {
                if (isRotating) { isRotating = false; }
             });
            this.getChildren().add(rotateCircle);

            this.setOnMouseMoved(event -> {
                mouseX.set(event.getX());
                mouseY.set(event.getY());
                    
                    if (shapePreview != null) {
                        if (selectedButton.get().shape().isInstance(shapePreview)) {
                            ((Previewable) shapePreview).preview(mouseX.get(), mouseY.get());
                        } else {
                            this.getChildren().remove(this.getChildren().size()-2);
                            shapePreview = null;
                        }
                    }
                });

            this.setOnMouseDragged(event -> {
                mouseX.set(event.getX());
                mouseY.set(event.getY());

                if (isRotating) { return; }

                switch (selectedButton.get()) {
                    case null -> {}

                    case Buttons.EDIT -> {
                        if (selectedShape != null && selectedShape instanceof Movable) {
                            ((Movable) selectedShape).move(event.getX(), event.getY());
                        }
                    }

                    default -> {
                        if (shapePreview != null) {
                            if (selectedButton.get().shape().isInstance(shapePreview)) {
                                ((Previewable) shapePreview).preview(mouseX.get(), mouseY.get());
                            } else {
                                this.getChildren().remove(this.getChildren().size()-2);
                                shapePreview = null;
                            }
                        }
                    }
                }       
            });

            this.setOnMouseExited(event -> {
                mouseX.set(-1);
                mouseY.set(-1);
            });

            this.setOnMousePressed(event -> {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        switch (selectedButton.get()) {
                            case null -> {}
                    
                            case Buttons.EDIT -> {
                                for (int i=getChildren().size()-2; i>=0; i--) {
                                    Node node = getChildren().get(i);
                                    if (node instanceof Shape shape && shape.contains(event.getX(), event.getY())) {
                                        if (selectedShape!=null) { selectedShape.setStroke(null); }
                                        selectedShape = shape;
                                        if (selectedShape instanceof Rotatable) {
                                            rotateCircle.centerXProperty().bind(((Rotatable) selectedShape).centerXProperty());
                                            rotateCircle.centerYProperty().bind(((Rotatable) selectedShape).centerYProperty());
                                        }
                                        selectedShape.setStroke(Color.RED);
                                        break;
                                    }
                                }
                            }
                            
                            default -> {
                                if (shapePreview == null) {
                                    shapePreview = Utils.createShape(selectedButton.get().shape(), event.getX(), event.getY(), selectedColor.getValue());
                                    this.getChildren().add(this.getChildren().size()-1, shapePreview);
                                } else {
                                    if (shapePreview instanceof Polygon) {
                                        if (((Polygon) shapePreview).isNearStartPoint(event.getX(), event.getY())) {
                                            ((Polygon) shapePreview).finish();
                                            shapePreview = null;
                                        } else {
                                            ((Polygon) shapePreview).getPoints().addAll(event.getX(), event.getY());
                                        }
                                    } else {
                                        shapePreview = null;
                                    }
                                }
                            }
                        }
                    }

                    case SECONDARY -> {
                        switch (selectedButton.get()) {
                            case null -> {}
                    
                            case Buttons.EDIT -> {
                                for (int i=getChildren().size()-2; i>=0; i--) {
                                    Node node = getChildren().get(i);
                                    if (node instanceof Shape shape && shape.contains(event.getX(), event.getY())) {
                                        shape.setFill(selectedColor.getValue());
                                        break;
                                    }
                                }
                            }
                            
                            default -> {
                                if (shapePreview != null) {
                                    this.getChildren().removeLast();
                                    shapePreview = null;
                                }
                            }
                        }
                    }
                
                    default -> {}
                    
                }
                
            });

            this.setOnScroll(event -> {
                if (selectedButton.get() == Buttons.EDIT) {
                    if (selectedShape != null && selectedShape instanceof Resizable) {
                        ((Resizable) selectedShape).resize(event.getDeltaY());
                    }
                }
            });
        }
    }


    /**
     * A status bar showing the current mouse coordinates in the format "X : Y".
     */
    public static class Cords extends HBox {
        /**
         * Constructs a Cords display element bound to mouse coordinates.
         *
         * @param mouseX the mouse's X coordinate property
         * @param mouseY the mouse's Y coordinate property
         */
        Cords(DoubleProperty mouseX, DoubleProperty mouseY) {
            super();

            Label mX = new Label();
            Label mY = new Label();
            mX.textProperty().bind(mouseX.asString("%.0f"));
            mY.textProperty().bind(mouseY.asString("%.0f"));
            
            this.getChildren().addAll(mX, new Label(" : "), mY);
            this.setAlignment(Pos.CENTER);
            this.setMinWidth(80);
        }
    }

}