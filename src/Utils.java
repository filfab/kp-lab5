import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.util.converter.NumberStringConverter;

public class Utils {

    /**
     * Returns the distance between two points.
     * @param x0 first coordinate of the first point
     * @param y0 second coordinate of the first point
     * @param x1 first coordinate of the second point
     * @param y1 second coordinate of the second point
     * @return the distance between (x0,y0) and (x1,y1)
     */
    public static Double distance(Double x0, Double y0, Double x1, Double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }

    /**
     * Class 
     */
    public static class MenuButton extends Button {
        /**
         * Creates a new instance of MenuButton.
         * @param text
         * @param id
         * @param selectedButton
         */
        MenuButton(String text, int id, IntegerProperty selectedButton) {
            super(text);

            this.setOnAction(event -> {
                selectedButton.set(id);
                
            });
        }
    }


    public static class InfoButton extends Button {
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


    public static class ChildStage extends Stage {
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


    public static class Canvas extends AnchorPane {
        private Shape shapePreview = null;


        Canvas(DoubleProperty mouseX, DoubleProperty mouseY, IntegerProperty selectedButton) {
            super();
            Canvas a = this;

            EventHandler<MouseEvent> mousePositionUpdate = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    mouseX.set(event.getX());
                    mouseY.set(event.getY());

                    if (shapePreview != null) {
                        switch (selectedButton.get()) {
                            case 0:
                                if (shapePreview instanceof Circle) { ((Circle) shapePreview).setRadius(distance(((Circle) shapePreview).getCenterX(), ((Circle) shapePreview).getCenterY(), mouseX.get(), mouseY.get())); }
                                else {
                                    a.getChildren().removeLast();
                                    shapePreview = null;
                                }
                                break;

                            case 1:
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
                            
                            case 2:
                                if (shapePreview instanceof Polygon) {
                                    ((Polygon) shapePreview).getPoints().set(((Polygon) shapePreview).getPoints().size()-2, event.getX());
                                    ((Polygon) shapePreview).getPoints().set(((Polygon) shapePreview).getPoints().size()-1, event.getY());
                                } else {
                                    a.getChildren().removeLast();
                                    shapePreview = null;
                                }
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
                            case 0:
                                if (shapePreview == null) {
                                    shapePreview = new Circle(event.getX(), event.getY(), 0);
                                    this.getChildren().add(shapePreview);
                                } else {
                                    shapePreview = null;
                                }
                                break;
                    
                            case 1:
                                if (shapePreview == null) {
                                    shapePreview = new PivotingRectangle(event.getX(), event.getY());
                                    this.getChildren().add(shapePreview);
                                } else {
                                    shapePreview = null;
                                }
                                break;
                        
                            case 2:
                                if (shapePreview == null) {
                                    shapePreview = new Polygon(event.getX(), event.getY(), event.getX(), event.getY());
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
                        
                            default:
                                break;
                        }
                
                    default: break;
                }
                
            });
        }
    }


    public static class Cords extends HBox {
        Cords(DoubleProperty mouseX, DoubleProperty mouseY) {
            super();

            Label mX = new Label();
            Label mY = new Label();
            mX.textProperty().bindBidirectional(mouseX, new NumberStringConverter());
            mY.textProperty().bindBidirectional(mouseY, new NumberStringConverter());

            this.getChildren().addAll(mX, new Label(" : "), mY);
            this.setAlignment(Pos.CENTER);
            this.setMinWidth(80);
        }
    }

    
    private static class PivotingRectangle extends Rectangle {
        Double pivotX, pivotY;

        PivotingRectangle(Double x, Double y) {
            super(x, y, 0.0, 0.0);
            pivotX = x;
            pivotY = y;
        }
    }

}