
public class Song {
    
    private String title;
    private String artist;
    private String playingTime;
    private String videoFileName;
    
    public Song(String title, String artist, String playingTime, String videoFileName){
        this.title = title;
        this.artist = artist;
        this.playingTime = playingTime;
        this.videoFileName = videoFileName;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getArtist(){
        return artist;
    }
    
    public String getPlayingTime(){
        return playingTime;
    }
    
    public String getVideoFileName(){
        return videoFileName;
    }
}

