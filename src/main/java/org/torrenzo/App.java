package org.torrenzo;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static final float WIDTH = 1400;
    public static final float HEIGHT = 1000;
    public static final String SCENE_TITLE = "GritsAintGroceries";
    public static final String rusty_tile = "/Wood-100.jpg";

    private double anchorX, anchorY = 0;
    private double anchorAngleX, anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);



    @Override
    public void start(Stage stage) {

        Box box = createBoxResource();

        SmartGroup group = new SmartGroup();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH/2);
        group.translateYProperty().set(HEIGHT/2);
        group.translateZProperty().set(-1500);

        initMouseControl(group, scene, stage);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W:
                    group.translateZProperty().set(group.getTranslateZ() + 100);
                    break;
                case S:
                    group.translateZProperty().set(group.getTranslateZ() - 100);
                    break;
                case A:
                    group.roll(10);
                    break;
                case D:
                    group.roll(-10);
                    break;
                case NUMPAD6:
                    group.yaw(10);
                    break;
                case NUMPAD4:
                    group.yaw(-10);
                    break;
                default:
                    break;
            }
        });

        stage.setTitle(SCENE_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    private Box createBoxResource() {

        Box box = new Box(100,20,50);

        PhongMaterial boxMaterial = new PhongMaterial();
        try {
            Image rustyTile = new Image(getClass().getResourceAsStream(rusty_tile));
            boxMaterial.setDiffuseMap(rustyTile);
            box.setMaterial(boxMaterial);
            System.out.println("diffuse map!");


        } catch(Exception e)  {
            System.out.println("diffuse map failed with " + e.getLocalizedMessage());
            boxMaterial.setDiffuseColor(Color.LIGHTSTEELBLUE);
            box.setMaterial(boxMaterial);
        }
        return box;
    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(e -> {
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(e -> {
            angleX.set((anchorAngleX - (anchorY - e.getSceneY())));
            angleY.set((anchorAngleY + (anchorX - e.getSceneX())));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, e -> group.translateZProperty().set(group.getTranslateZ() + e.getDeltaY()));
    }

    public static void main(String[] args) {
        launch();
    }

    static class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();

        void roll(int degrees) {
            r = new Rotate(degrees, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void yaw(int degrees) {
            r = new Rotate(degrees, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }


    }

}