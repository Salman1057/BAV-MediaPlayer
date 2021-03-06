/*This is dev-changeAudioOnScrolling branch and we'll focus on beta-feature of
 * changing the audio while the mouse scrolls within the scene....
 * */

package com.example.demo;

/*optimized imports*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/*Main Class from your Program will Run...*/
public class HelloApplication extends Application {
    public static Scene scene;
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));   /*hello-view.fxml is the file containing your GUI*/

        /*Setting the scene*/

        scene = new Scene(root);
        scene.setFill(Color.BLACK);

        /*Set the title of the window....*/

        stage.setTitle("BAV MediaPlayer");
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());  /*Uncomment this line to add your CSS file*/

        /*Setting the min-height && min-width of the stage, so it doesn't collapse*/

        stage.setMinHeight(500);
        stage.setMinWidth(550);

        /*Line of code to enter or exit from the fullscreen on double-clicking the mediaView*/

        scene.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}