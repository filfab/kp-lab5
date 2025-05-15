import java.util.ArrayList;

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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Utils {

    public static Double distance(Double x0, Double y0, Double x1, Double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }


    public static class MenuButton extends Button {  
        MenuButton(String text, int id, IntegerProperty selectedButton, ArrayList<Double> points) {
            super(text);

            this.setOnAction(event -> {
                selectedButton.set(id);
                points.clear();
                
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
                        Stage infoStage = new ChildStage(text, primaryStage, isOpen);
                        infoStage.show();
                        isOpen.set(true);
                    }
                }
            });
        }
    }


    public static class ChildStage extends Stage {
        ChildStage(String text, Stage owner, BooleanProperty isOpen) {
            super();

            Label content = new Label(text);
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


        Canvas(DoubleProperty mouseX, DoubleProperty mouseY, IntegerProperty selectedButton, ArrayList<Double> points) {
            super();

            EventHandler<MouseEvent> mousePositionUpdate = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    mouseX.set(event.getX());
                    mouseY.set(event.getY());

                    if (shapePreview != null) {
                        switch (selectedButton.get()) {
                            case 0:
                                ((Circle) shapePreview).setRadius(distance(((Circle) shapePreview).getCenterX(), ((Circle) shapePreview).getCenterY(), mouseX.get(), mouseY.get()));
                                break;

                            case 1:
                                ((Rectangle) shapePreview).setX(Math.min(((Rectangle) shapePreview).getX(), event.getX()));
                                ((Rectangle) shapePreview).setY(Math.min(((Rectangle) shapePreview).getY(), event.getY()));
                                ((Rectangle) shapePreview).setWidth(event.getX() - ((Rectangle) shapePreview).getX());
                                ((Rectangle) shapePreview).setHeight(event.getY() - ((Rectangle) shapePreview).getY());
                                break;
                            
                            case 2:
                                ((Polygon) shapePreview).getPoints().set(((Polygon) shapePreview).getPoints().size()-2, event.getX());
                                ((Polygon) shapePreview).getPoints().set(((Polygon) shapePreview).getPoints().size()-1, event.getY());
                        
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
                                    shapePreview = new Rectangle(event.getX(), event.getY(), event.getX(), event.getY());
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


    public static class PolygonStartPoint extends Button{
        PolygonStartPoint() {
            super();

            this.setOnAction(event -> {

            });
        }
    }

}