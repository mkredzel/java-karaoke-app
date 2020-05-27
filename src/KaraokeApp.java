
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class KaraokeApp extends Application {

    static String dataFile = System.getProperty("user.dir") + File.separator + "songs.txt";

    public static ArrayList<Song> songArray = new ArrayList<Song>();
    public static ArrayList<Song> playlistArray = new ArrayList<Song>();

    public ObservableList<Song> songArrayObs = FXCollections.observableArrayList(songArray);
    public static ObservableList<Song> playlistArrayObs;

    public static TableView<Song> libraryView;
    public static TableView<Song> playlistView;
    
    public static Player player;
    public static Media media;
    
    public static boolean found;

    public static String filePath;
    
    @Override
    public void start(final Stage primaryStage) {

        filePath = "file:///C:/Users/Marcel/Desktop/KaraokeApp/";
        player = new Player(filePath + "nosong.mp4");
        
        // Library label
        Label libraryLabel = new Label("Library");
        libraryLabel.setTextFill(Color.BLACK);
        libraryLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 24));

        // Playlist label
        Label playlistLabel = new Label("Playlist");
        playlistLabel.setTextFill(Color.BLACK);
        playlistLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 24));

        // TABLE VIEW FOR LIBRARY
        libraryView = new TableView<>();
        
        TableColumn<Song, String> titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Song, String> artistColumn = new TableColumn("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn<Song, String> lengthColumn = new TableColumn("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("playingTime"));

        TableColumn<Song, String> fileColumn = new TableColumn("File Name");
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("videoFileName"));

        titleColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.5));
        artistColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.33));
        lengthColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.15));
        fileColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.2));

        for (int i = 0; i < songArrayObs.size(); i++) {
            String title = songArrayObs.get(i).getTitle();
            String artist = songArrayObs.get(i).getArtist();
            int playingTime = Integer.parseInt(songArrayObs.get(i).getPlayingTime());
            String videoFileName = songArrayObs.get(i).getVideoFileName();

            int minutes = (playingTime % 3600) / 60;
            int seconds = playingTime % 60;
            String timeString = String.format("%02d:%02d", minutes, seconds);

            libraryView.getItems().add(new Song(title, artist, timeString, videoFileName));
        }

        libraryView.getColumns().addAll(titleColumn, artistColumn, lengthColumn, fileColumn);

        libraryView.setPrefWidth(320);
        libraryView.setPrefHeight(240);

        // TABLE VIEW FOR PLAYLIST  
        playlistView = new TableView<>();
        
        TableColumn<Song, String> titlePlaylistColumn = new TableColumn("Title");
        titlePlaylistColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Song, String> artistPlaylistColumn = new TableColumn("Artist");
        artistPlaylistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn<Song, String> lengthPlaylistColumn = new TableColumn("Length");
        lengthPlaylistColumn.setCellValueFactory(new PropertyValueFactory<>("playingTime"));

        TableColumn<Song, String> filePlaylistColumn = new TableColumn("File Name");
        filePlaylistColumn.setCellValueFactory(new PropertyValueFactory<>("videoFileName"));

        playlistView.setPrefWidth(320);
        playlistView.setPrefHeight(240);
        playlistView.setPlaceholder(new Label("No songs added yet!"));

        playlistView.getColumns().addAll(titlePlaylistColumn, artistPlaylistColumn, lengthPlaylistColumn, filePlaylistColumn);
        
        libraryView.getSelectionModel().selectFirst();
        titlePlaylistColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.5));
        artistPlaylistColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.33));
        lengthPlaylistColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.15));
        filePlaylistColumn.prefWidthProperty().bind(libraryView.widthProperty().multiply(0.2));

        // Search
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Song's Title");
        Button searchBtn = new Button("Search");

        HBox searchBox = new HBox(searchField, searchBtn);

        // Layout
        VBox library = new VBox(libraryLabel, searchBox, libraryView);
        VBox playlist = new VBox(playlistLabel, playlistView);

        VBox.setMargin(playlistLabel, new Insets(25, 0, 0, 0));

        searchBox.setAlignment(Pos.CENTER);
        library.setAlignment(Pos.CENTER);
        playlist.setAlignment(Pos.CENTER);

        Button addToPlaylistBtn = new Button("Add To Playlist");
        Button deleteBtn = new Button("Delete");
        Button addBtn = new Button("Add New Song");
        Button deletePlaylistBtn = new Button("Delete Selected Song From Playlist");

        addToPlaylistBtn.setPrefWidth(120);
        deleteBtn.setPrefWidth(85);
        addBtn.setPrefWidth(115);
        deletePlaylistBtn.setPrefWidth(320);
        addToPlaylistBtn.setCursor(Cursor.HAND);
        deleteBtn.setCursor(Cursor.HAND);
        addBtn.setCursor(Cursor.HAND);
        searchBtn.setCursor(Cursor.HAND);
        deletePlaylistBtn.setCursor(Cursor.HAND);

        // Add New Song To Library
        addBtn.setOnAction(e -> {
            AddSong.display();
        });
        
        // Delete From Library
        deleteBtn.setOnAction(e -> {
            int i = libraryView.getSelectionModel().getSelectedIndex();

            Song selectedItem = libraryView.getSelectionModel().getSelectedItem();
            libraryView.getItems().remove(selectedItem);
            songArray.remove(i);
        });

        // Add To Playlist
         addToPlaylistBtn.setOnAction(e -> {
            
            Song selectedItem = libraryView.getSelectionModel().getSelectedItem();
             
            String videoTitle = selectedItem.getTitle();
            String videoArtist = selectedItem.getArtist();
            String videoLength = selectedItem.getPlayingTime();
            String videoFileName = selectedItem.getVideoFileName();
            
            playlistArray.add(new Song(videoTitle, videoArtist, videoLength, videoFileName));   
            
            updatePlaylist();
            
            if(playlistArray.size() == 1){
                String vid = playlistArray.get(0).getVideoFileName();
                MediaBar.forward(filePath + vid);
            }
        });
         
        // Delete From Playlist
        deletePlaylistBtn.setOnAction(e -> {
            if(playlistArray.size() == 1){
                MediaBar.player.pause();
                int i = playlistView.getSelectionModel().getSelectedIndex();

                playlistArrayObs.remove(i);
            
                Song selectedItem = playlistView.getSelectionModel().getSelectedItem();
                playlistView.getItems().remove(selectedItem);
                playlistArray.remove(i);
                
                updatePlaylist();
                Alert.display("alert", "Playlist is empty. Please add new songs...");
            } else {
            
                int i = playlistView.getSelectionModel().getSelectedIndex();

                playlistArrayObs.remove(i);
            
                Song selectedItem = playlistView.getSelectionModel().getSelectedItem();
                playlistView.getItems().remove(selectedItem);
                playlistArray.remove(i);
                   
                updatePlaylist();
            
                if(i == 0){
                    String vid = playlistArray.get(0).getVideoFileName();
                    MediaBar.forward(filePath + vid);
                }
            }
        });
        
        // Search
        searchBtn.setOnAction(e -> {
            String searchValue = searchField.getText().toLowerCase();
            
             for(int i=0; i < songArray.size(); i++) {
                if(songArray.get(i).getTitle().toLowerCase().equals(searchValue)){
                    libraryView.requestFocus();
                    libraryView.getSelectionModel().select(i);
                    libraryView.getFocusModel().focus(i);
                    
                    libraryView.scrollTo(i);
                    found = true;
                    break;
                } else {
                    found = false;
                }
            }
            if(found == false){
                Alert.display("Song not found", "Song not found, please try with another title...");
            }
        });

        HBox hbox = new HBox(library, playlist);

        VBox playerWithTables = new VBox(player, hbox);

        HBox bottomButtons = new HBox(addToPlaylistBtn, deleteBtn, addBtn, deletePlaylistBtn);

        VBox finalView = new VBox(playerWithTables, bottomButtons);

        Scene scene = new Scene(finalView, 640, 710, Color.BLACK);

        primaryStage.setTitle("Karaoke App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void readFile(String file) {
        try {
            Scanner fileReader = new Scanner(new File(file));

            while (fileReader.hasNextLine()) {
                String currentLine = fileReader.nextLine();
                String[] category = currentLine.split("\t");

                String title = category[0];
                String artist = category[1];
                String playingTime = category[2];
                String videoFileName = category[3];

                songArray.add(new Song(title, artist, playingTime, videoFileName));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File Not Found...");
        }
    }
    
    public static void updatePlaylist() {
        playlistArrayObs = FXCollections.observableArrayList(playlistArray);  
        playlistView.setItems(playlistArrayObs);
    }

    public static void main(String[] args) {
        readFile(dataFile);
        launch(args);
    }
}
