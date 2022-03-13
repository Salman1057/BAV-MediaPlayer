/*This is dev-changeAudioOnScrolling branch and we'll focus on beta-feature of
* changing the audio while the mouse scrolls within the scene....
* */

package com.example.demo;

/*optimized imports for the file*/

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HelloController {
    /*Path to the file where all the recent media will be placed...*/
    private final String recentTextFilepath = "src\\main\\java\\com\\example\\demo\\recent-media.txt";
    @FXML
    private MenuItem exitButton;
    @FXML
    private Button playButton;
    @FXML
    private Label elapsedTime;
    @FXML
    private Label totalDuration;
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider progressBar;

/*Choose File method for selecting file from the directory using FileChooser Class
    Then ready the file to be played and some other methods to be performed while initializing the MediaPlayer*/

    public void chooseFile() throws IOException {
        /*Sets the initial directory to the Videos like on Windows it'll be C:\Users\<user-name>\Videos*/
        File file1 = new File(System.getProperty("user.home"), "\\Videos");
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, file1);
        File file = fileChooser.showOpenDialog(null);
        String path = file.toURI().toString(); /*To get the path of the file selected by the user.*/
        System.out.println("Path: " + path);

        /*FileWriter obj to write recent media to
         * txt file and can clear the file on demand...
         * */

        FileWriter fw = new FileWriter(recentTextFilepath, true);
        fw.write(path + "\n");
        fw.close();

        /*if the path contains something other than null, then ready the MediaPlayer...*/

        if (path != null) {
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            /*To stop a video, when clicked on OpenFile Button while playing a media file*/
//
//            if (Objects.requireNonNull(mediaPlayer).getStatus().equals(MediaPlayer.Status.PLAYING)) {
//                mediaPlayer.stop();
//                System.out.println("\nVideo Stopped...!");
//            }
//
//            if (mediaPlayer!=null){
//                mediaPlayer.stop();
//            }

            //Creating Bindings for the media to resize with the resizing of the window

            DoubleProperty widthProperty = mediaView.fitWidthProperty();
            DoubleProperty heightProperty = mediaView.fitHeightProperty();

            widthProperty.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProperty.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            mediaView.setPreserveRatio(true);     /*This line sets the scaling of the video....*/

            /*Setting the VolumeSlider of the Player
             * first line sets the slider and the next line has a listener which listens for any changes on the slider
             * and then sets the volume of the player according to the value set by the user...
             * */

            volumeSlider.setValue(mediaPlayer.getVolume() * 70);
            volumeSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));

            exitButton.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

            /*This line sets the progress-bar...Increases as the video plays. Min value is 0 and Max value is set to the duration of the video*/

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> progressBar.setValue(newValue.toSeconds()));
            progressBar.setOnMousePressed(event -> mediaPlayer.seek(Duration.seconds(progressBar.getValue())));

            /*Sets the elapsed time which is a Label on the Player GUI with the fx:id elaspedTime and a totalDuration
             * which are calculated in the getTimeString() method...and then set their values using listener.
             * */

            mediaPlayer.currentTimeProperty().addListener(ov -> {
                if (!progressBar.isValueChanging()) {
                    double total = mediaPlayer.getTotalDuration().toMillis();
                    double current = mediaPlayer.getCurrentTime().toMillis();
                    elapsedTime.setText(getTimeString(current));
                    totalDuration.setText(getTimeString(total));
                }
            });

            /*After all checks, we're ready to launch our media player*/

            mediaPlayer.setOnReady(() -> {
                Duration total = media.getDuration();
                progressBar.setMax(total.toSeconds());

            });

            /*Plays the Media captured by the user...*/
            mediaPlayer.play();
        }
    }

    /*Method to set the initial directory of the FileChooser to the Videos Directory*/

    private static void configureFileChooser(final FileChooser fileChooser, File file1) {
        fileChooser.setTitle("Select File to Play");
        fileChooser.setInitialDirectory(file1);
    }

    /*Method to calculate the Elapsed Time of the Media...HH:MM:SS format*/

    public String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
        millis /= 60;
        String h = formatTime(millis % 24);
        return h + ":" + m + ":" + s;
    }

    /*For formatting the time (Elapsed + Total)*/

    private String formatTime(double time) {
        int t = (int) time;
        if (t > 9) {
            return String.valueOf(t);
        }
        return "0" + t;
    }

    /*Method to clear recent played media items from the file..*/

    public void clearRecentFile() throws IOException {
        FileWriter writer = new FileWriter(recentTextFilepath);
        writer.write("");
        writer.close();
        System.out.println("Cleared all recent media");
    }

    /*Method to be called whenever Play-Pause button is clicked...Uses if-else to switch the signs for play-pause*/

    public void play_pauseVideo() {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            playButton.setText("||");
            mediaPlayer.pause();
        } else {
            playButton.setText(">>");
            mediaPlayer.play();
        }
    }

    /*Close the Program*/

    public void closeProgram() {
        Platform.exit();
    }


    /*To stop the video and makes the MediaPlayer to points to Null*/

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
            System.out.println("\nVideo Stopped....!!!");
        }
    }

    /*Method to get call whenever you wants to skip your media...Pass the value as an argument and enter the
     * double argument to the method
     */

    public void skip() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
//        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-5))); /*Just pass the desired amount of time in the arguments and pass it*/
    }

    /*Method to set Rate of the Media Playback...
     * 1 is normal(default), 0.5 is slower, 1.5 is faster
     * pass a double argument to changes its value dynamically
     * */

    public void setRate() {
        mediaPlayer.setRate(1.5);
//        mediaPlayer.setRate(0.5);
    }
}