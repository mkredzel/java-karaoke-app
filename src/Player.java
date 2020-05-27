
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Player extends BorderPane {
	
    public static Media media;
    public static MediaPlayer player;
    public static MediaView view;
    public static Pane mpane;
    public static MediaBar bar;
    public static BorderPane borderPane = new BorderPane();
	
    public Player(String file) {
        media = new Media(file);
        player = new MediaPlayer(media);
        view = new MediaView(player);
        mpane = new Pane();
        
        mpane.getChildren().add(view);

        setCenter(mpane);

        bar = new MediaBar(player);

        setBottom(bar);
	}
}