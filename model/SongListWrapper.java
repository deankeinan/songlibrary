package songlibrary.model;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of songs to save to XML.
 * @author Dean Keinan
 */

@XmlRootElement(name = "songs")
public class SongListWrapper {

    private List<Song> songs;


    public List<Song> getSongs() {
        return songs;
    }

    @XmlElement(name = "song")
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}