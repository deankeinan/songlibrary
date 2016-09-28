package songlibrary.model;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Song {
	private StringProperty title;
	private StringProperty artist;
	private StringProperty album;
	private StringProperty year;

	public Song(){
		this(null,null);
	}

	public Song(String title, String artist) {
		this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty("");
        this.year = new SimpleStringProperty("");
	}

	public Song(String title, String artist, String album) {
		this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty(album);
        this.year = new SimpleStringProperty("");
	}

	public Song(String title, String artist, String album, String year) {
		this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty(album);
        this.year = new SimpleStringProperty(year);
	}


	public String getTitle(){
		return title.get();
	}

	public void setTitle(String title){
		this.title.set(title);
	}

	public StringProperty titleProperty(){
		return title;
	}

	public String getArtist(){
		return artist.get();
	}

	public void setArtist(String artist){
		this.artist.set(artist);
	}

	public StringProperty artistProperty(){
		return artist;
	}

	public String getAlbum(){
		return album.get();
	}

	public void setAlbum(String album){
		if(album!=""){
			this.album.set(album);
		}
		else{
			this.album.set("");
		}
	}

	public StringProperty albumProperty(){
		return album;
	}

	public String getYear(){
		return year.get();
	}

	public void setYear(String year){
		if(year!=""){
			this.year.set(year);
		}
		else{
			this.year.set("");
		}
	}

	public StringProperty yearProperty(){
		return year;
	}

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Song)) {
            return false;
        }
        Song song = (Song) o;
        return (this.getTitle().equals(song.getTitle()) && this.getArtist().equals(song.getArtist()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.artist);
    }
}
