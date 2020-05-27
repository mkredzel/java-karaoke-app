
import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaBar extends HBox {

    public static Slider time = new Slider();
    Slider vol = new Slider();

    Button playButton = new Button("Play");
    Button pauseButton = new Button("Pause");
    Button skipButton = new Button("Skip");

    Label volume = new Label(" Volume: ");
    
    public static Label currentTime = new Label("");
    public static Label slash = new Label("/");
    public static Label length = new Label("");
    
    public static String vid;

    public static MediaPlayer player;
    
    

    public MediaBar(MediaPlayer play) {
        player = play;

        
        
         
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20, 10, 0, 0));

        vol.setPrefWidth(70);
        vol.setMinWidth(30);
        vol.setValue(100);

        time.setMaxWidth(300);

        HBox.setHgrow(time, Priority.ALWAYS);

        playButton.setPrefWidth(60);
        pauseButton.setPrefWidth(60);
        playButton.setCursor(Cursor.HAND);
        pauseButton.setCursor(Cursor.HAND);
        skipButton.setCursor(Cursor.HAND);
        vol.setCursor(Cursor.HAND);
        time.setCursor(Cursor.HAND);

        getChildren().add(playButton);
        getChildren().add(pauseButton);
        getChildren().add(skipButton);
        getChildren().add(time);
        getChildren().add(currentTime);
        getChildren().add(slash);
        getChildren().add(length);
        getChildren().add(volume);
        getChildren().add(vol);

        // Play, Pause buttons
        playButton.setOnAction((e) -> {
            player.play();
        });

        pauseButton.setOnAction((e) -> {
            player.pause();
        });

        skipButton.setOnAction((e) -> {
            if(KaraokeApp.playlistArray.isEmpty()){
                Alert.display("Playlist", "Can't skip any song now... Playlist is empty...");
            } else {
                player.pause();
            
                KaraokeApp.playlistArray.remove(0);
                KaraokeApp.updatePlaylist();
            
                if(KaraokeApp.playlistArray.isEmpty()){
                    Alert.display("alert", "Playlist is empty. Please add new songs...");
                } else {
                    vid = KaraokeApp.playlistArray.get(0).getVideoFileName();
                    forward(KaraokeApp.filePath + vid);
                }
            }
        });

        player.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();
        });

        time.valueProperty().addListener((Observable ov) -> {
            if (time.isPressed()) {
                player.seek(player.getMedia().getDuration().multiply(time.getValue() / 100));
            }
        });
        
        vol.valueProperty().addListener((Observable ov) -> {
            if (vol.isPressed()) {
                player.setVolume(vol.getValue() / 100);
            }
        });
        
        time.valueProperty().addListener((Observable ov) -> {
            if (time.isPressed()) {
                player.seek(player.getMedia().getDuration().multiply(time.getValue() / 100));
            }
        });
    }
    
    public static void forward(String filePath) {
        player.stop();
        Media media = new Media(filePath);
        player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        Player.mpane.getChildren().add(mediaView);
        player.play();
        player.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();
        });
        
        Platform.runLater(() -> {
            time.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
            
        });
        
        player.setOnEndOfMedia(() -> {
            player.pause();
            
            KaraokeApp.playlistArray.remove(0);
            KaraokeApp.updatePlaylist();
            
            vid = KaraokeApp.playlistArray.get(0).getVideoFileName();
            
            forward(KaraokeApp.filePath + vid);
        });
    }

    protected static void updateValues() {
        Platform.runLater(() -> {
            double minutes = (player.getTotalDuration().toSeconds() % 3600) / 60;
            double seconds =  player.getTotalDuration().toSeconds() % 60;
            String timeString = String.format("%02d:%02d", (int)minutes, (int)seconds);
            
            double currentMinutes = (player.getCurrentTime().toSeconds() % 3600) / 60;
            double currentSeconds =  player.getCurrentTime().toSeconds() % 60;
            String timeStringCurrentTime = String.format("%02d:%02d", (int)currentMinutes, (int)currentSeconds);
            
            time.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
            length.setText(timeString);
            currentTime.setText(timeStringCurrentTime);
        });
    }
}
