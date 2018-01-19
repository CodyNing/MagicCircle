
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 * <p>Click and drag your mouse. 
 * MagicCircle will set the first click as the center 
 * of an equilateral triangle. As you drag your mouse,
 * one of three corners will follow your mouse to extend
 * and spin the triangle. Further, it shifts the angle
 * and generate another equilateral triangle to form a
 * hexagram. It then draws a circle on the outside to
 * make the drawing looks like a magic circle.</p>
 *
 * @author Zhuo (Cody) Ning
 * @version 1.0
 */
public class MagicCircle extends Application {
    
    /** Three. */
    public static final int THREE = 3;
        
    /** The contents of the application scene. */
    private Group root;

    /** center point. */
    private Point2D center;
    
    /** circle to move to first mouse click location. */
    private Circle atCenter = new Circle(0, 0, THREE);
    
    /** Triangle polygon to draw on the scene. */
    private Polygon triangle;
    
    /** Second Triangle polygon to draw on the scene. */
    private Polygon triangle2;
    
    /** Circle to draw on the scene.*/
    private Circle circle;
   
    /**
     * Displays an initially empty scene, waiting for the user to draw lines
     * with the mouse.
     * 
     * @param primaryStage
     *            a Stage
     */
    public void start(Stage primaryStage) {
        root = new Group(atCenter);
        atCenter.setFill(Color.CYAN);

        final int appWidth = 800;
        final int appHeight = 500;
        Scene scene = new Scene(root, appWidth, appHeight, Color.BLACK);
        
        scene.setOnMousePressed(this::processMousePress);
        scene.setOnMouseDragged(this::processMouseDrag);
        scene.setOnMouseReleased(this::processMouseRelease);

        primaryStage.setTitle("Equilateral Triangle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Initialize the center point of the triangle when mouse first press.
     * @param event
     *              mouse click.
     */
    private void processMousePress(MouseEvent event) {
        center = new Point2D(event.getX(), event.getY());
        atCenter.setCenterX(event.getX());
        atCenter.setCenterY(event.getY());
        circle = new Circle(0, 0, THREE);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(THREE);
        circle.setStroke(Color.CYAN);
        root.getChildren().add(circle);
    }
    
    /**
     * Draw appropriate hexagram magic circle when mouse drag.
     * @param event
     *              mouse drag.
     */
    private void processMouseDrag(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        double cX = center.getX();
        double cY = center.getY();
        double delta = Math.PI / THREE;
        root.getChildren().remove(triangle);
        root.getChildren().remove(triangle2);
        final double[] points = getPoint(x, y, cX, cY, 0);
        final double[] points2 = getPoint(x, y, cX, cY, delta);
        triangle = setUpPolygon(points);
        triangle2 = setUpPolygon(points2);
        circle.setCenterX(cX);
        circle.setCenterY(cY);
        circle.setRadius(getRadius(x, y, cX, cY));
        root.getChildren().add(triangle);
        root.getChildren().add(triangle2);
        
    }
    
    /**
     * Remove triangle when mouse release.
     * @param event
     *              mouse release.
     */
    private void processMouseRelease(MouseEvent event) {
        root.getChildren().remove(triangle);
        root.getChildren().remove(triangle2);
        root.getChildren().remove(circle);
    }
    
    /**
     * Use polar coordinate system to calculate the coordinate
     * of each points of the triangle.
     * @param x1 mouse x
     * @param y1 mouse y
     * @param x2 center x
     * @param y2 center y
     * @param delta spin corner
     * @return a double array with 6 triangle coordinates
     */
    private double[] getPoint(double x1, double y1, double x2,
            double y2, double delta) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        double r = Math.sqrt(Math.pow(xDiff, 2) 
                + Math.pow(yDiff, 2));
        double alpha = Math.atan2(yDiff, xDiff);
        double alphaDiff = 2 * Math.PI / THREE;
        double x = r * Math.cos(alpha + delta) + x2;
        double y = r * Math.sin(alpha + delta) + y2;
        double nx1 = r * Math.cos(alpha + delta + alphaDiff) + x2;
        double ny1 = r * Math.sin(alpha + delta + alphaDiff) + y2;
        double nx2 = r * Math.cos(alpha + delta - alphaDiff) + x2;
        double ny2 = r * Math.sin(alpha + delta - alphaDiff) + y2;
        final double[] points = {
                x, y,
                nx1, ny1,
                nx2, ny2
        };
        return points;
    }
    
    /**
     * Set up polygon by preferred settings.
     * @param point coordinates for polygon.
     * @return a set Polygon
     */
    private Polygon setUpPolygon(double[] point) {
        Polygon toSet = new Polygon(point);
        toSet.setStrokeWidth(THREE);
        toSet.setStroke(Color.CYAN);
        toSet.setFill(Color.TRANSPARENT);
        return toSet;
    }
    
    /**
     * Return the radius of mouse to the center.
     * @param x1 mouse x
     * @param y1 mouse y
     * @param x2 center x
     * @param y2 center y
     * @return radius
     */
    private double getRadius(double x1, double y1, double x2, double y2) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        return Math.sqrt(Math.pow(xDiff, 2) 
                + Math.pow(yDiff, 2));
    }


    /**
     * Launches the JavaFX application.
     * 
     * @param args
     *            command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

