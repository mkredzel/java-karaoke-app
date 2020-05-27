
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AddSong {

    static TextField songTitle = new TextField("");
    static TextField songArtist = new TextField("");
    static TextField songLength = new TextField("");
    static TextField songVideo = new TextField("");

    public static void display() {

        Stage window = new Stage();

        window.setTitle("Add Song To Library");

        Label titleLabel = new Label("Title");
        Label artistLabel = new Label("Artist");
        Label lengthLabel = new Label("Length in seconds");
        Label videoLabel = new Label("Video File Name");

        Button addBtn = new Button("Add");
        Button closeBtn = new Button("Close");

        addBtn.setOnAction(e -> {

            String title = songTitle.getText();
            String artist = songArtist.getText();
            String lengthValidation = songLength.getText();
            
            String videoFileName = songVideo.getText();

            //Validation of the users input.
            if ("".equals(title) || "".equals(artist) || "".equals(lengthValidation) || "".equals(videoFileName)) {
                Alert.display("Alert", "All fields must be filled out.");
            } else if (!lengthValidation.matches("[0-9]+")) {
                Alert.display("Alert", "Length values must be digits.");
            } else {
                int playingTime = Integer.parseInt(songLength.getText());
                int minutes = (playingTime % 3600) / 60;
                int seconds = playingTime % 60;
                String timeString = String.format("%02d:%02d", minutes, seconds);
                KaraokeApp.songArray.add(new Song(title, artist, timeString, videoFileName));
                KaraokeApp.libraryView.getItems().add(new Song(title, artist, timeString, videoFileName));
                KaraokeApp.libraryView.requestFocus();
                KaraokeApp.libraryView.scrollTo(KaraokeApp.songArray.size());
                KaraokeApp.libraryView.getSelectionModel().select(KaraokeApp.songArray.size());
                KaraokeApp.libraryView.getFocusModel().focus(KaraokeApp.songArray.size());
                songTitle.clear();
                songArtist.clear();
                songLength.clear();
                songVideo.clear();
                
                window.close();
            }
        });

        closeBtn.setOnAction(e -> {
            window.close();
        });

        GridPane addWindow = new GridPane();
        addWindow.add(titleLabel, 0, 1);
        addWindow.add(songTitle, 1, 1);
        addWindow.add(artistLabel, 0, 2);
        addWindow.add(songArtist, 1, 2);
        addWindow.add(lengthLabel, 0, 3);
        addWindow.add(songLength, 1, 3);
        addWindow.add(videoLabel, 0, 4);
        addWindow.add(songVideo, 1, 4);

        HBox submit = new HBox(10, addBtn, closeBtn);

        VBox pane = new VBox(10, addWindow, submit);

        addBtn.setMinWidth(100);
        closeBtn.setMinWidth(100);

        VBox.setMargin(addWindow, new Insets(0, 0, 0, 20));

        addWindow.setVgap(5);
        addWindow.setHgap(20);

        submit.setStyle("-fx-font-size: 1.2em; ");

        songTitle.setMaxWidth(100);
        songArtist.setMaxWidth(100);
        songLength.setMaxWidth(100);
        songVideo.setMaxWidth(100);

        submit.setAlignment(Pos.CENTER);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane, 250, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}
