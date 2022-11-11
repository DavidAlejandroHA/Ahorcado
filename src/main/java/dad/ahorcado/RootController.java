package dad.ahorcado;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dad.ahorcado.palabras.PalabrasController;
import dad.ahorcado.partida.PartidaController;
import dad.ahorcado.puntuaciones.Puntuacion;
import dad.ahorcado.puntuaciones.PuntuacionesController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class RootController implements Initializable {
	
	// controllers
	private PalabrasController palabrasController = new PalabrasController(); 
	private PartidaController partidaController = new PartidaController();
	private PuntuacionesController puntuacionesController = new PuntuacionesController();
	
	// models
	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>(FXCollections.observableArrayList());
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// load data
		palabras = palabrasController.getModel().cargarPalabras(palabras); // mete dentro de palabrasProperty todas las palabras del fichero
		
		partidaController.getModel().setPalabras(palabras);
		palabrasController.getModel().setPalabras(palabras);
		
		List<Puntuacion> puntuacionesList;
		try {
			puntuacionesList = Puntuacion.loadPuntuaciones("puntuaciones/puntuaciones.csv"); // alert
			puntuaciones.addAll(puntuacionesList);
		} catch (Exception e1) {
			puntuaciones.add(new Puntuacion("Error al cargar las puntuaciones."));
		}
		// bindings
		
		palabrasController.getModel().palabrasProperty().bind(palabras);
		partidaController.getModel().palabrasProperty().bind(palabras);
		
		puntuacionesController.getModel().puntuacionesProperty().bind(puntuaciones);
		// Le pasa las puntuaciones cargadas del fichero a la tabla de puntuaciones
		partidaController.getModel().puntuacionesProperty().bindBidirectional(puntuaciones);
		/** Le pasa las puntuaciones cargadas del fichero a la property de puntuaciones.
		 * Esta servirá para añadir puntuaciones nuevas a esa property, y gracias al binding
		 * bidireccional se actualiza la tabla del puntuacionesController
		 */
		
		palabrasTab.setContent(palabrasController.getView());
		partidasTab.setContent(partidaController.getView());
		puntuacionesTab.setContent(puntuacionesController.getView());
		
		if(partidaController.getModel().palabrasProperty().get().size()>=1) { // Si no hay palabras
			partidaController.recargarDatos();
			partidaController.getModel().setPermitir(true);
		} else {
			avisoSinPalabras();
		}
	}
	
	private void avisoSinPalabras() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.initOwner(AhorcadoApp.primaryStage);
    	alerta.setTitle("Sin palabras para jugar");
    	alerta.setHeaderText("La lista de palabras está vacía.");
    	alerta.setContentText("Añade varias palabras a la lista y reinicia el programa para iniciar el juego.");
    	Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(AhorcadoApp.class.getResourceAsStream("/hangman/9.png"))); // AhorcadoApp.primaryStage.getIcons() aún es nulo porque rootcontroller se inicia antes
    	alerta.showAndWait();
	}
	
	@FXML 
	private Tab partidasTab, palabrasTab, puntuacionesTab;
	
	@FXML
	private TabPane view;
	
	public RootController() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TabPane getView() {
		return view;
	}
}
