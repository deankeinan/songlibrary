package songlibrary;
//hello
import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import songlibrary.model.Song;
import songlibrary.model.SongListWrapper;
import songlibrary.view.SongOverviewController;

public class SongLib extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Song> songData = FXCollections.observableArrayList();


    public SongLib(){
    	loadSongDataFromFile();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Song Library");

        initRootLayout();

        showSongOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SongLib.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showSongOverview() {
        try {
            // Load and center overview
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SongLib.class.getResource("view/SongOverview.fxml"));
            AnchorPane songOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(songOverview);

            // Give the controller access to the main app.
            SongOverviewController controller = loader.getController();
            controller.setSongLib(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the data as an observable list of Songs.
     * @return
     */
    public ObservableList<Song> getSongData() {
        return songData;
    }

    /**
     * Loads song data from the specified file. The current song data will
     * be replaced.
     *
     * @param file
     */
    public void loadSongDataFromFile() {
    	File file = new File("songs.xml");
        try {
            JAXBContext context = JAXBContext
                    .newInstance(SongListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            SongListWrapper wrapper = (SongListWrapper) um.unmarshal(file);

            songData.clear();
            songData.addAll(wrapper.getSongs());

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void saveSongDataToFile() {
    	File file = new File("songs.xml");
        try {
            JAXBContext context = JAXBContext
                    .newInstance(SongListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            SongListWrapper wrapper = new SongListWrapper();
            wrapper.setSongs(songData);
            m.marshal(wrapper, file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
        }
    }


}