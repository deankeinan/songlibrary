package songlibrary.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import songlibrary.SongLib;
import songlibrary.model.Song;

public class SongOverviewController {

	@FXML
	private TableView<Song> songTable;
	@FXML
	private TableColumn<Song, String> titleColumn;
	@FXML
	private TextField titleEditField;
	@FXML
	private TextField artistEditField;
	@FXML
	private TextField albumEditField;
	@FXML
	private TextField yearEditField;
	@FXML
	private TextField titleAddField;
	@FXML
	private TextField artistAddField;
	@FXML
	private TextField albumAddField;
	@FXML
	private TextField yearAddField;

	// Reference to the main application.
	private SongLib songLib;

	/**
	 * The constructor.
	 * The constructor is called before the initialize() method.
	 */
	public SongOverviewController() {
	}

	@FXML
	private void initialize() {
		// Initialize the song table with the two columns.
		titleColumn.setCellValueFactory(
				cellData -> cellData.getValue().titleProperty());
		// Clear song details.

		songTable.sort();

		songTable.getSelectionModel().selectFirst();
		refreshEditPanel();

		// Listen for selection changes and show the song details when changed.
		songTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> refreshEditPanel());

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainApp
	 */
	public void setSongLib(SongLib songLib) {
		this.songLib = songLib;
		// Add observable list data to the table
		songTable.setItems(songLib.getSongData());
	}


	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeleteSong() {
		int selectedIndex = songTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			songTable.getItems().remove(selectedIndex);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(songLib.getPrimaryStage());
			alert.setTitle("Failed to delete.");
			alert.setHeaderText("No Song Selected");
			alert.setContentText("Please select a song in the table.");
			alert.showAndWait();
		}
		songLib.saveSongDataToFile();
	}


	/**
	 * Called when the user selects a song, and updates the editing panel.
	 */
	public void refreshEditPanel(){
		Song songSelected = songTable.getSelectionModel().getSelectedItem();
		if(songSelected !=null){
			titleEditField.setText(songSelected.getTitle());
			artistEditField.setText(songSelected.getArtist());
			albumEditField.setText(songSelected.getAlbum());
			yearEditField.setText(songSelected.getYear());
		}
		else{
			titleEditField.setText("");
			artistEditField.setText("");
			albumEditField.setText("");
			yearEditField.setText("");
		}
	}

	/**
	 * Called when the user clicks Apply Changes.
	 */
	@FXML
	private void handleApply() {
		Song songSelected = songTable.getSelectionModel().getSelectedItem();
		if (isInputValid(titleEditField.getText(),artistEditField.getText(),albumEditField.getText(),yearEditField.getText(),true)) {
			songSelected.setTitle(titleEditField.getText());
			songSelected.setArtist(artistEditField.getText());
			songSelected.setAlbum(albumEditField.getText());
			songSelected.setYear(yearEditField.getText());
			refreshEditPanel();
			songTable.sort();
			songLib.saveSongDataToFile();
		}
	}

	/**
	 * Called when the user clicks revert.
	 */
	@FXML
	private void handleRevert() {
		refreshEditPanel();
	}

	/**
	 * Called when the user opts to add a song.
	 */
	@FXML
	private void handleAdd() {
		if (isInputValid(titleAddField.getText(),artistAddField.getText(),albumAddField.getText(),yearAddField.getText(),false)) {
			Song tempSong = new Song();
			tempSong.setTitle(titleAddField.getText());
			tempSong.setArtist(artistAddField.getText());
			tempSong.setAlbum(albumAddField.getText());
			tempSong.setYear(yearAddField.getText());
			songLib.getSongData().add(tempSong);
			songTable.sort();
			songTable.getSelectionModel().select(tempSong);
			songLib.saveSongDataToFile();
		}
	}

	/**
	 * Checks to see if the data contains a song by the same title and artist.
	 */
	private boolean existsDuplicate(String title, String artist){
		Song tempSong = new Song(title,artist);
		return songLib.getSongData().contains(tempSong);
	}

	//helper
	private boolean isEmpty(String check){
		return (check == null || check.length() == 0);
	}

	/**
	 * Checks the input from both add/edit song functionality as per requirements.
	 *
	 * @param title passed from text field
	 * @param artist passed from text field
	 * @param album passed from text field
	 * @param year passed from text field
	 * @param edit flag to let the method know if an edit operation is happening
	 * @return
	 */
	private boolean isInputValid(String title, String artist, String album, String year, boolean edit) {
		Song songSelected = songTable.getSelectionModel().getSelectedItem();

		String errorMessage = "";

		//Title + Artist are required fields
		if (isEmpty(title)) {
			errorMessage += "No valid title!\n";
		}
		if (isEmpty(artist)) {
			errorMessage += "No valid artist!\n";
		}

		//Allowing the EDIT of album/year on a song without throwing duplicate error
		if(edit == true && songSelected.getTitle().equals(title) && songSelected.getArtist().equals(artist)){
			//do nothing
		}
		else if (existsDuplicate(title, artist)){
			errorMessage += "Song already exists in library!\n";
		}

		//Allow for empty year but not for non-integer year fields.
		year = year.trim();
		if(year.isEmpty() == false){
			try {
				Integer.parseInt(year);
			} catch (NumberFormatException e) {
				errorMessage += "Invalid year. (must be an integer)!\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(songLib.getPrimaryStage());
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}