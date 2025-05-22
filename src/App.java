import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Javafx application
 */
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
     * The primary stage (main window) of the application.
     */
    Stage primaryStage;
    /**
     * The canvas (drawing board) of the application.
     */
    Canvas canvas;
    /**
     * Flag containing information about currenty celected button.
     */
    Buttons.ButtonSelector selectedButton = new Buttons.ButtonSelector();
    /**
     * Pointer flag informing whether info popup window is open.
     */
    BooleanProperty infoWindowFlag = new SimpleBooleanProperty(false);
    /**
     * Pointer flag informing whether help popup window is open.
     */
    BooleanProperty helpWindowFlag = new SimpleBooleanProperty(false);
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
        canvas = new Canvas(mouseX, mouseY, selectedButton, selectedColor);

        StackPane wrapper = new StackPane(canvas);
        wrapper.setAlignment(Pos.CENTER);

        ScrollPane center = new ScrollPane(wrapper);
        center.setFitToHeight(true);
        center.setFitToWidth(true);
        return center;
    }

    /**
     * Creates the bottom section of the UI containing coordinate display,
     * mode selection buttons, save/load buttons, and information/help buttons.
     *
     * @return a layout node containing bottom controls
     */
    private Region createBottom() {
        // Coordinate display
        HBox cords = new Cords(mouseX, mouseY);

        // Selection buttons
        HBox menu = new HBox(10);
        for (Buttons type : Buttons.values()) {
            menu.getChildren().add(new MenuButton(type.toString(), type, selectedButton));
        }
        FileChooser fileChooser = new FileChooser();
        Button save = new Button("save");
        save.setOnAction(event -> {
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                Event.fireEvent(canvas, new FileEvent(FileEvent.SAVE, file));
            }
        });
        Button load = new Button("load");
        load.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                Event.fireEvent(canvas, new FileEvent(FileEvent.LOAD, file));
            }
        });
        menu.getChildren().addAll(selectedColor, save, load);
        menu.setAlignment(Pos.CENTER);

        // Info and Help buttons
        HBox info = new HBox(10);
        info.getChildren().addAll(
            new InfoButton("info", primaryStage, infoWindowFlag),
            new InfoButton("help", primaryStage, helpWindowFlag)
        );
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
     * A custom canvas pane for drawing and previewing Buttons (circle, rectangle, polygon).
     */
    public static class Canvas extends AnchorPane {
        private Shape shapePreview = null;
        private final Utils.Pointer<Shape> selectedShape = new Utils.Pointer<Shape>(null);
        private final RotationCircle rotationCircle = new RotationCircle(selectedShape);

        /**
         * Constructs a new Canvas that tracks mouse events and allows shape drawing.
         *
         * @param mouseX         a property holding the current mouse X coordinate
         * @param mouseY         a property holding the current mouse Y coordinate
         * @param selectedButton an IntegerProperty representing the selected shape tool
         */
        Canvas(DoubleProperty mouseX, DoubleProperty mouseY, Buttons.ButtonSelector selectedButton, ColorPicker selectedColor) {
            super();
            setMaxSize(1600, 900);
            setMinSize(1600, 900);
            setPrefSize(1600, 900);
            setStyle("-fx-background-color: white");

            getChildren().add(rotationCircle);

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
                if (!selectedShape.isNull() && selectedButton.get()!=Buttons.EDIT) {
                    rotationCircle.setVisible(false);
                    selectedShape.value().setStroke(null);
                    selectedShape.set(null);
                }
            });

            addEventHandler(FileEvent.LOAD, event -> {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(event.getFile()))) {
                    Utils.ShapeRepr[] arr = (Utils.ShapeRepr[]) in.readObject();
                    getChildren().clear();
                    getChildren().add(rotationCircle);
                    for (Utils.ShapeRepr shapeRepr : arr) {
                        getChildren().add(getChildren().size()-1, shapeRepr.recreate());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            addEventHandler(FileEvent.SAVE, event -> {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(event.getFile()))) {
                    Utils.ShapeRepr[] arr = new Utils.ShapeRepr[getChildren().size()-1];
                    for (int i=0; i<arr.length; i++) {
                        arr[i] = ((Repr) getChildren().get(i)).createRepr();
                    }
                    out.writeObject(arr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            this.setOnMouseDragged(event -> {
                mouseX.set(event.getX());
                mouseY.set(event.getY());
                
                switch (selectedButton.get()) {
                    case null -> {}
                    
                    case Buttons.EDIT -> {
                        if (rotationCircle.getIsRotating()) { return; }

                        if (!selectedShape.isNull() && selectedShape.value() instanceof Movable) {
                            ((Movable) selectedShape.value()).move(event.getX(), event.getY());
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
                        if (!selectedShape.isNull()) {
                            rotationCircle.setVisible(false);
                            selectedShape.value().setStroke(null);
                            selectedShape.set(null);;
                        }
                    }
                }       
            });

            this.setOnMouseExited(event -> {
                mouseX.set(-1);
                mouseY.set(-1);
            });

            this.setOnMousePressed(event -> {
                switch (selectedButton.get()) {
                    case null -> {}

                    case Buttons.EDIT -> {
                        // Select a shape
                        for (int i=getChildren().size()-2; i>=0; i--) {
                            Node node = getChildren().get(i);
                            if (node instanceof Shape shape && shape.getBoundsInParent().contains(event.getX(), event.getY())) {
                                if (!selectedShape.isNull()) { selectedShape.value().setStroke(null); }
                                selectedShape.set(shape);
                                if (selectedShape.value() instanceof Rotatable rotatable) {
                                    rotationCircle.translateXProperty().bind(rotatable.rotationPivotXProperty());
                                    rotationCircle.translateYProperty().bind(rotatable.rotationPivotYProperty());
                                    rotationCircle.setVisible(true);
                                } else {
                                    rotationCircle.setVisible(false);
                                }
                                selectedShape.value().setStroke(Color.RED);
                                break;
                            }
                        }

                        if (event.getButton() == MouseButton.SECONDARY) {
                            selectedShape.value().setFill(selectedColor.getValue());
                        }
                    }
                
                    default -> {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            if (shapePreview != null) {
                                this.getChildren().remove(this.getChildren().size()-2);
                                shapePreview = null;
                            }
                            return;
                        }

                        if (shapePreview == null) {
                            shapePreview = Utils.createShape(selectedButton.getShape(), event.getX(), event.getY(), selectedColor.getValue());
                            getChildren().add(getChildren().size()-1, shapePreview);
                        } else {
                            if (shapePreview instanceof Polygon polygon) {
                                shapePreview = polygon.nextPoint(event.getX(), event.getY());
                            } else {
                                shapePreview = null;
                            }
                        }
                    }
                }
            });

            this.setOnScroll(event -> {
                if (selectedButton.get() == Buttons.EDIT) {
                    if (!selectedShape.isNull() && selectedShape.value() instanceof Resizable resizable) {
                        resizable.resize(event.getDeltaY());
                    }
                }
            });
        }

    }

    /**
     * A custom menu button that updates a ButtonSelector when clicked.
     */
    public static class MenuButton extends Button {
        /**
         * Constructs a new MenuButton.
         *
         * @param text            the text displayed on the button
         * @param id              the ID associated with this button
         * @param buttonSelector  a ButtonSelector to be bound to the button instance
         */
        MenuButton(String text, Buttons id, Buttons.ButtonSelector buttonSelector) {
            super(text);

            this.setOnAction(event -> {
                buttonSelector.set(id);
            });
        }
    }

    /**
     * A button that opens a child stage displaying information loaded from a file.
     */
    public static class InfoButton extends Button {
        private final VBox content = new VBox();

        /**
         * Constructs an InfoButton that opens a window with content from a text file.
         *
         * @param text         the name of the file (without extension) to load and also button's label
         * @param primaryStage the parent stage for modal positioning
         * @param isOpen       property indicating whether the info window is already open
         */
        InfoButton(String text, Stage primaryStage, BooleanProperty isOpen) {
            super(text);
            
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
            content.setAlignment(Pos.CENTER);
            
            this.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!isOpen.get()) {
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
            StackPane wrapper = new StackPane(content);
            wrapper.setAlignment(Pos.CENTER);
            this.setScene(new Scene(wrapper, 400, 400));
            this.setResizable(false);
            this.initOwner(owner);
            this.setOnCloseRequest(event -> {
                isOpen.set(false);
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

    /**
     * A circle used as the input method for shape rotation.
     */
    private static class RotationCircle extends javafx.scene.shape.Circle {
        /**
         * Flag indicating whether the shape is being rotated.
         */
        private boolean isRotating = false;

        /**
         * Constructs a RotationCircle binding it to a shape selector.
         * 
         * @param selectedShape shape selector pointer
         */
        public RotationCircle(Utils.Pointer<Shape> selectedShape) {
            super();
            setRadius(20);
            setFill(null);
            setStroke(Color.BLUE);
            setStrokeWidth(10);
            setOnMouseDragged(event -> {
                isRotating = true;
                if (selectedShape.value() instanceof Rotatable) { ((Rotatable) selectedShape.value()).rotate(event.getX(), event.getY()); }
            });
            setOnMouseReleased(event -> {
                if (isRotating) { isRotating = false; }
            });
            setVisible(false);
        }

        /**
         * Returns whether the shape is being rotated.
         * 
         * @return {@code true} if shape is being rotated, otherwise {@code false}
         */
        public boolean getIsRotating() {
            return isRotating;
        }
    }
    
    /**
     * A custom used for save/load functionality. 
     */
    public static class FileEvent extends Event {
        /** FileEvent variant representing save operation */
        public static final EventType<FileEvent> SAVE = new EventType<>(Event.ANY, "SAVE");
        /** FileEvent variant representing save operation */
        public static final EventType<FileEvent> LOAD = new EventType<>(Event.ANY, "LOAD");
        /** Selected file to save into/load from */
        private final File file;

        /**
         * Constructs a FileEvent of specified type
         * 
         * @param eventType type of the event
         * @param file      selected file
         */
        public FileEvent(EventType<? extends FileEvent> eventType, File file) {
            super(eventType);
            this.file = file;
        }

        /**
         * Returns the file to save into/load from
         * 
         * @return the file object
         */
        public File getFile() {
            return file;
        }
    }
}