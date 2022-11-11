package dad.ahorcado.palabras;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.ahorcado.AhorcadoApp;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PalabrasController implements Initializable {
	
	// view

	@FXML
	private BorderPane view;

	@FXML
	private Button nuevoButton;

	@FXML
	private ListView<String> palabrasList;

	@FXML
	private Button quitarButton;
	
	// model

	PalabrasModel model = new PalabrasModel();
	
	public PalabrasController() {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PalabrasView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// bindings
		palabrasList.itemsProperty().bindBidirectional(model.palabrasProperty());
		model.seleccionPalabraProperty().bind(palabrasList.getSelectionModel().selectedItemProperty());
		quitarButton.disableProperty().bind(Bindings.when(model.seleccionPalabraProperty().isNull()
																	.or(model.palabrasProperty().sizeProperty().lessThan(2)))
																		.then(true)
																		.otherwise(false));
	}

	public BorderPane getView() {
		return view;
	}

	@FXML
	void onNuevoAction(ActionEvent event) throws IOException {

		TextInputDialog dialogo = new TextInputDialog();
		dialogo.initOwner(AhorcadoApp.primaryStage);
		dialogo.setTitle("Añadir nueva palabra");
		dialogo.setHeaderText("Añade una nueva palabra a la lista");
		Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(AhorcadoApp.class.getResourceAsStream("/hangman/9.png")));
		Optional<String> textoDialogo = dialogo.showAndWait();
		if(textoDialogo.isPresent() && !textoDialogo.get().isBlank() && !model.palabrasProperty().stream().anyMatch(p -> textoDialogo.get().equals(p))) {
			model.palabrasProperty().add(textoDialogo.get());
			Path path = new File("palabras/palabras.csv").toPath(); //
			Files.writeString(path, "\n"+textoDialogo.get(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		}
		

	}

	@FXML
	void onQuitarAction(ActionEvent event) throws IOException {
		StringBuilder lineas = new StringBuilder();
		Path path = new File("palabras/palabras.csv").toPath(); //
		Files.lines(path)
			.filter(linea -> !linea.contains(model.seleccionPalabraProperty().getValue())) // retorna la línea que contiene a la palabra a quitar
			.forEach(s -> lineas.append(s+"\n"));
		Files.writeString(path, lineas.substring(0, lineas.length()-1), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING); 
		/**
		 * Reemplaza el fichero actual por uno que no contiene la palabra a quitar
		 * Además se elimina el último retorno que contiene la última palabra
		 */
		model.palabrasProperty().remove(model.seleccionPalabraProperty().get());
	}
	
	public PalabrasModel getModel() {
		return model;
	}
	
}
