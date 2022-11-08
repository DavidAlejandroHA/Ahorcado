package dad.ahorcado.partida;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PartidaController implements Initializable {
	
	@FXML
	private Button letraBoton;

	@FXML
	private Button resolverBoton;

	@FXML
	private ImageView imagenLabel;

	@FXML
	private Label letrasUsadasLabel;

	@FXML
	private Label palabraAdivinarLabel;

	@FXML
	private ListView<String> puntosListView;

	@FXML
	private TextField textoTextField;

	@FXML
	private GridPane view;
	
	// model
	
	private PartidaModel model = new PartidaModel();
	
	public PartidaController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// bindings
		puntosListView.itemsProperty().bind(model.puntosJugadoresProperty());
		model.letrasTextoTextFieldProperty().bindBidirectional(textoTextField.textProperty());
		letraBoton.disableProperty().bind(model.botonValorProperty());
		palabraAdivinarLabel.textProperty().bind(model.palabraOcultadaEspaciadaProperty());
		letrasUsadasLabel.textProperty().bind(model.letrasJugadasProperty());
		imagenLabel.imageProperty().bind(model.imagenProperty());
		
		// load data
		
		model.anadirListenerBoton();
	}
	
	public GridPane getView() {
		return view;
	}
	
	@FXML
	void onLetra(ActionEvent event) {
		model.comprobarLetra();
	}
	
	@FXML
	void onResolver(ActionEvent event) {
		model.comprobarPalabra();
	}
	
	public PartidaModel getModel() {
		return model;
	}
}