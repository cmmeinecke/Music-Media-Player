/* Mp3 Player Program by Caitlin Meinecke
Creates a function mp3 player that plays mp3 files selected by user
 */

//declares the package
package com.example.mediaplayerr;

//imports necessary JavaFX packages the program needs to run
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

// Imports necessary packages and classes for the program to run
import java.io.File;            //needed to work with files & directories
import java.util.ArrayList;     //needed for arrays
import java.util.List;          //imports List interface

//create Main class that extends the JavaFX Application class, so it can run as JavaFx app
public class Main extends Application {
    private Stage primaryStage;         //holds primaryStage variable (main window)
    private MediaPlayer mediaPlayer;    //holds mediaPlayer variable (plays media files)
    private Label fileNameLabel;    //holds fileNameLabel variable (displays name of file playing)
    private MediaView mediaView;    //holds mediaView variable (displays media content)
    private double width;   //holds width variable
    private double height;  //holds height variable

    //variable to hold list of file paths of mp3 files to be played
    private final List<String> filePaths = new ArrayList<>();
    private int currentFileIndex = 0;   //variable to manage currently playing file

    //main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }

    // Creates the start method which configures the main stage, or main window
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Sets up the initial scene
        Scene scene = setScene(this.width, this.height);
        MenuItem open = new MenuItem("Open");
        Menu file = new Menu("File");
        file.getItems().add(open);
        //sets up the primary stage
        primaryStage.setTitle("Caitlin's Mp3 Player");
        primaryStage.setScene(scene);

        //adds toolbar to the scene
        HBox toolBar = addToolBar(primaryStage);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mediaView);
        borderPane.setBottom(toolBar);

        // Sets up the file name label and adds it to the top of the scene
        fileNameLabel = new Label("Select a folder to start playing");
        fileNameLabel.setTextFill(Color.WHITE);
        HBox fileNameBox = new HBox(fileNameLabel);
        fileNameBox.setAlignment(Pos.CENTER);
        borderPane.setTop(fileNameBox);

        // Sets the background color of the scene
        borderPane.setStyle("-fx-background-color: Purple");
        scene = new Scene(borderPane, 600, 300);
        scene.setFill(Color.PURPLE);
        primaryStage.setScene(scene);

        //show the primary stage
        primaryStage.show();
    }

    // Set up the main scene with a MediaView and a file name label
    private Scene setScene(double width, double height) {
        this.width = width;
        this.height = height;

        // Adds a drop shadow effect to the MediaView
        DropShadow dropshadow = new DropShadow();
        dropshadow.setOffsetY(5.0);
        dropshadow.setOffsetX(5.0);
        dropshadow.setColor(Color.WHITE);

        // Creates a MediaView and applies the drop shadow effect
        mediaView = new MediaView();
        mediaView.setEffect(dropshadow);

        // Sets up the BorderPane to contain the MediaView and the toolbar
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mediaView);
        borderPane.setBottom(addToolBar(primaryStage));

        // Sets up the file name label and adds it to the top of the scene
        fileNameLabel = new Label("Select a folder to start playing");
        fileNameLabel.setTextFill(Color.WHITE);
        HBox fileNameBox = new HBox(fileNameLabel);
        fileNameBox.setAlignment(Pos.CENTER);
        borderPane.setTop(fileNameBox);

        // Sets the background color of the scene
        borderPane.setStyle("-fx-background-color: black");
        Scene scene = new Scene(borderPane, 600, 300);
        scene.setFill(Color.BLACK);

        return scene;
    }

    // Creates and configures the toolbar with its buttons
    private HBox addToolBar(Stage primaryStage) {
        HBox toolBar = new HBox();
        toolBar.setPadding(new Insets(20));
        toolBar.setAlignment(Pos.CENTER);
        toolBar.alignmentProperty().isBound();
        toolBar.setSpacing(5);
        toolBar.setStyle("-fx-background-color: Black");

        // Creates the working buttons with my selected images
        Button backButton = createButtonWithImage("src/main/resources/Images/Back.png");
        Button playButton = createButtonWithImage("src/main/resources/Images/Play.png");
        Button pauseButton = createButtonWithImage("src/main/resources/Images/Pause.png");
        Button forwardButton = createButtonWithImage("src/main/resources/Images/Forward.png");
        Button stopButton = createButtonWithImage("src/main/resources/Images/Stop.png");
        Button folderButton = createButtonWithImage("src/main/resources/Images/Folder.png");
        Button nextButton = createButtonWithImage("src/main/resources/Images/Next.png");

        // Adds all the buttons to the toolbar
        toolBar.getChildren().addAll(folderButton, backButton, playButton, pauseButton, stopButton, forwardButton, nextButton);

        // Sets actions for each button

        // The play button will play the media
        playButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.play();
        });

        // The pause button will pause the media
        pauseButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.pause();
        });

        // The stop button completely stops the media
        stopButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.stop();
        });

        // Forward buttons fast forwards by 1.5 times the current position of the media
        forwardButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().multiply(1.5));
        });

        // The back rewinds the media by 1.5 times its current position
        backButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().divide(1.5));
        });

        //the folder button allows a user to choose folder to play media from
        folderButton.setOnAction((ActionEvent e) -> {
            DirectoryChooser dc = new DirectoryChooser();
            File directory = dc.showDialog(primaryStage);
            /* if directory selected, method is called to load file paths of the mp3 &
            stored in the filePaths list and the first file in the list is played
            */
            if (directory != null) {
                loadFilesFromDirectory(directory);
                playFile(filePaths.get(0));
            }
        });

        // Next button allows user to select the next song in the folder
        nextButton.setOnAction((ActionEvent e) -> {
            playNextFile();
        });

        return toolBar;
    }

    // Creates buttons with the image selected
    private Button createButtonWithImage(String imagePath) {
        Image image = new Image("file:" + imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        Button button = new Button();
        button.setGraphic(imageView);

        return button;
    }

    // Updates the file name displayed on the label
    private void updateFileNameLabel(String fileName) {
        Platform.runLater(() -> {
            fileNameLabel.setText("File: " + fileName);
            fileNameLabel.setFont(new Font(24)); // Sets the font size to 24
            fileNameLabel.requestLayout();  // Forces a layout pass to update the UI
        });
    }

    // Plays the specified file
    private void playFile(String filePath) {
        // Creates a new Media instance from the file path
        Media newMedia = new Media(new File(filePath).toURI().toString());

        /* if media is already playing, it stops and disposes of previous media
        when new media is selected
         */
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        // Creates a new media player with the new media
        mediaPlayer = new MediaPlayer(newMedia);

        // Updates the displayed file name
        updateFileNameLabel(new File(filePath).getName());

        mediaView.setMediaPlayer(mediaPlayer);

        // AutoPlays the media
        mediaPlayer.setAutoPlay(true);
    }

    // Plays the next file in the list
    private void playNextFile() {
        if (!filePaths.isEmpty()) {
            currentFileIndex = (currentFileIndex + 1) % filePaths.size();
            playFile(filePaths.get(currentFileIndex));
        }
    }

    // Loads the mp3 files from the selected directory (folder) using an array
    private void loadFilesFromDirectory(File directory) {
        // Clears any previous file paths
        filePaths.clear();
        // Obtains array of File objects from selected directory
        File[] files = directory.listFiles();
        if (files != null) {
            // For loop that iterates through the File objects and confirms they are mp3 files
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                    // Adds files to list if they meet criteria
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }
    }
}
