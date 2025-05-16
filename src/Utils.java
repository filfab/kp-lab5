import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Utils {

    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param x0 x-coordinate of the first point
     * @param y0 y-coordinate of the first point
     * @param x1 x-coordinate of the second point
     * @param y1 y-coordinate of the second point
     * @return the distance between the points (x0, y0) and (x1, y1)
     */
    public static double distance(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
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
        MenuButton(String text, int id, IntegerProperty selectedButton) {
            super(text);

            this.setOnAction(event -> {
                selectedButton.set(id);
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
         * @param text        the name of the file (without extension) to load
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
     * A custom canvas pane for drawing and previewing shapes (circle, rectangle, polygon).
     */
    public static class Canvas extends AnchorPane {
        private Shape shapePreview = null;

        /**
         * Constructs a new Canvas that tracks mouse events and allows shape drawing.
         *
         * @param mouseX         a property holding the current mouse X coordinate
         * @param mouseY         a property holding the current mouse Y coordinate
         * @param selectedButton an IntegerProperty representing the selected shape tool
         */
        Canvas(DoubleProperty mouseX, DoubleProperty mouseY, IntegerProperty selectedButton, ColorPicker selectedColor) {
            super();
            Canvas a = this;

            // Updates the shape preview based on mouse movement
            EventHandler<MouseEvent> mousePositionUpdate = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    mouseX.set(event.getX());
                    mouseY.set(event.getY());

                    if (shapePreview != null) {
                        switch (selectedButton.get()) {
                            case 0: // Circle
                                if (shapePreview instanceof Circle) { ((Circle) shapePreview).setRadius(distance(((Circle) shapePreview).getCenterX(), ((Circle) shapePreview).getCenterY(), mouseX.get(), mouseY.get())); }
                                else {
                                    a.getChildren().removeLast();
                                    shapePreview = null;
                                }
                                break;

                            case 1: // Rectangle
                                if (shapePreview instanceof PivotingRectangle) {
                                    ((Rectangle) shapePreview).setX(Math.min(((PivotingRectangle) shapePreview).pivotX, event.getX()));
                                    ((Rectangle) shapePreview).setY(Math.min(((PivotingRectangle) shapePreview).pivotY, event.getY()));
                                    ((Rectangle) shapePreview).setWidth(Math.abs(event.getX() - Math.max(((Rectangle) shapePreview).getX(), ((PivotingRectangle) shapePreview).pivotX)));
                                    ((Rectangle) shapePreview).setHeight(Math.abs(event.getY() - Math.max(((Rectangle) shapePreview).getY(), ((PivotingRectangle) shapePreview).pivotY)));
                                } else {
                                    a.getChildren().removeLast();
                                    shapePreview = null;
                                }
                                break;
                            
                            case 2: // Polygon
                                if (shapePreview instanceof Polygon) {
                                    ((Polygon) shapePreview).getPoints().set(((Polygon) shapePreview).getPoints().size()-2, event.getX());
                                    ((Polygon) shapePreview).getPoints().set(((Polygon) shapePreview).getPoints().size()-1, event.getY());
                                } else {
                                    a.getChildren().removeLast();
                                    shapePreview = null;
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }
            };            
            this.setOnMouseMoved(mousePositionUpdate);
            this.setOnMouseDragged(mousePositionUpdate);

            this.setOnMouseExited(event -> {
                mouseX.set(-1);
                mouseY.set(-1);
            });

            this.setOnMousePressed(event -> {
                switch (event.getButton()) {
                    case PRIMARY:
                        switch (selectedButton.get()) {
                            case 0: //Circle
                                if (shapePreview == null) {
                                    shapePreview = new Circle(event.getX(), event.getY(), 0);
                                    shapePreview.setFill(selectedColor.getValue());
                                    this.getChildren().add(shapePreview);
                                } else {
                                    shapePreview = null;
                                }
                                break;
                    
                            case 1: // Rectangle
                                if (shapePreview == null) {
                                    shapePreview = new PivotingRectangle(event.getX(), event.getY());
                                    shapePreview.setFill(selectedColor.getValue());
                                    this.getChildren().add(shapePreview);
                                } else {
                                    shapePreview = null;
                                }
                                break;
                        
                            case 2: //Polygon
                                if (shapePreview == null) {
                                    shapePreview = new Polygon(event.getX(), event.getY(), event.getX(), event.getY());
                                    shapePreview.setFill(selectedColor.getValue());
                                    this.getChildren().add(shapePreview);
                                } else if (distance(event.getX(), event.getY(), ((Polygon) shapePreview).getPoints().get(0), ((Polygon) shapePreview).getPoints().get(1)) <= 10) {
                                    ((Polygon) shapePreview).getPoints().removeLast();
                                    ((Polygon) shapePreview).getPoints().removeLast();
                                    shapePreview = null;
                                } else {
                                    ((Polygon) shapePreview).getPoints().addAll(event.getX(), event.getY());
                                }
                                break;

                            default:
                                break;
                        }
                    break;

                    case SECONDARY:
                        switch (selectedButton.get()) {
                            case 0,1,2:
                                if (shapePreview != null) {
                                    this.getChildren().removeLast();
                                    shapePreview = null;
                                }
                                break;

                            case 3:
                                for (int i=getChildren().size()-1; i>=0; i--) {
                                    Node node = getChildren().get(i);
                                    if (node instanceof Shape shape && shape.contains(event.getX(), event.getY())) {
                                        shape.setFill(selectedColor.getValue());
                                        break;
                                    }
                                }
                                break;
                        
                            default:
                                break;
                        }
                
                    default: break;
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

    /**
     * A rectangle that remembers its initial pivot point for resizing purposes.
     */
    private static class PivotingRectangle extends Rectangle {
        double pivotX, pivotY;

        /**
         * Constructs a new PivotingRectangle with a pivot point.
         *
         * @param x the initial X coordinate (pivot)
         * @param y the initial Y coordinate (pivot)
         */
        PivotingRectangle(double x, double y) {
            super(x, y, 0.0, 0.0);
            pivotX = x;
            pivotY = y;
        }
    }

}