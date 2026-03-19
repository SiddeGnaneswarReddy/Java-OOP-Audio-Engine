import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --- ENUM ---
enum PlayerState {
    PLAYING,
    PAUSED,
    STOPPED
}

// --- SONG CLASS ---
class Song {
    private final String title;
    private final String artist;
    private final int durationInSeconds;

    public Song(String title, String artist, int durationInSeconds) {
        this.title = title;
        this.artist = artist;
        this.durationInSeconds = durationInSeconds;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    
    public String getFormattedDuration() {
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return String.format("'%s' by %s [%s]", title, artist, getFormattedDuration());
    }
}

// --- PLAYLIST CLASS ---
class Playlist {
    private final String name;
    private final List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public String getName() { return name; }
    
    public void addSong(Song song) {
        songs.add(song);
        System.out.println("Added: " + song.getTitle() + " to " + name);
    }

    public void removeSong(Song song) {
        if (songs.remove(song)) {
            System.out.println("Removed: " + song.getTitle());
        } else {
            System.out.println("Song not found in playlist.");
        }
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs); 
    }

    public int getTotalSongs() {
        return songs.size();
    }
}

// --- MUSIC PLAYER CLASS ---
class MusicPlayer {
    private Playlist currentPlaylist;
    private int currentSongIndex;
    private PlayerState state;

    public MusicPlayer() {
        this.currentSongIndex = 0;
        this.state = PlayerState.STOPPED;
    }

    public void loadPlaylist(Playlist playlist) {
        this.currentPlaylist = playlist;
        this.currentSongIndex = 0;
        this.state = PlayerState.STOPPED;
        System.out.println("\n--- Loaded Playlist: " + playlist.getName() + " ---");
    }

    public void play() {
        if (isPlaylistEmpty()) return;

        if (state == PlayerState.PLAYING) {
            System.out.println("Track is already playing.");
            return;
        }

        state = PlayerState.PLAYING;
        System.out.println("▶ PLAYING: " + getCurrentSong().toString());
    }

    public void pause() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
            System.out.println("⏸ PAUSED: " + getCurrentSong().getTitle());
        } else {
            System.out.println("Cannot pause. No track is currently playing.");
        }
    }

    public void stop() {
        if (state != PlayerState.STOPPED) {
            state = PlayerState.STOPPED;
            System.out.println("⏹ STOPPED playback.");
        }
    }

    public void nextTrack() {
        if (isPlaylistEmpty()) return;

        if (currentSongIndex < currentPlaylist.getTotalSongs() - 1) {
            currentSongIndex++;
            state = PlayerState.PLAYING;
            System.out.println("⏭ Skipped to NEXT track.");
            play();
        } else {
            System.out.println("End of playlist reached. Stopping playback.");
            stop();
            currentSongIndex = 0; 
        }
    }

    public void previousTrack() {
        if (isPlaylistEmpty()) return;

        if (currentSongIndex > 0) {
            currentSongIndex--;
            state = PlayerState.PLAYING;
            System.out.println("⏮ Skipped to PREVIOUS track.");
            play();
        } else {
            System.out.println("Already at the first track.");
            play(); 
        }
    }

    public void showStatus() {
        if (isPlaylistEmpty()) {
            System.out.println("Player Status: Empty Queue | State: " + state);
            return;
        }
        System.out.printf("Status: %s | Current Track: %s (Track %d of %d)%n", 
            state, getCurrentSong().getTitle(), (currentSongIndex + 1), currentPlaylist.getTotalSongs());
    }

    private Song getCurrentSong() {
        return currentPlaylist.getSongs().get(currentSongIndex);
    }

    private boolean isPlaylistEmpty() {
        if (currentPlaylist == null || currentPlaylist.getTotalSongs() == 0) {
            System.out.println("Playlist is empty or not loaded.");
            return true;
        }
        return false;
    }
}

// --- MAIN RUNNER CLASS ---
public class OOPProject {
    public static void main(String[] args) {
        Song song1 = new Song("Bohemian Rhapsody", "Queen", 354);
        Song song2 = new Song("Shape of You", "Ed Sheeran", 233);
        Song song3 = new Song("Hotel California", "The Eagles", 390);

        Playlist myFavorites = new Playlist("My Favorites");
        myFavorites.addSong(song1);
        myFavorites.addSong(song2);
        myFavorites.addSong(song3);

        MusicPlayer player = new MusicPlayer();
        player.loadPlaylist(myFavorites);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\nWelcome to HarmoniCore Player!");
        
        while (running) {
            System.out.println("\n[1] Play  [2] Pause  [3] Stop  [4] Next  [5] Previous  [6] Status  [7] Exit");
            System.out.print("Select action: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1": player.play(); break;
                case "2": player.pause(); break;
                case "3": player.stop(); break;
                case "4": player.nextTrack(); break;
                case "5": player.previousTrack(); break;
                case "6": player.showStatus(); break;
                case "7": 
                    running = false; 
                    System.out.println("Exiting player. Goodbye!"); 
                    break;
                default: 
                    System.out.println("Invalid input. Please choose a valid option.");
            }
        }
        scanner.close();
    }
}