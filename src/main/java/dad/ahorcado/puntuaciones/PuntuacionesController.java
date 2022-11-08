package dad.ahorcado.puntuaciones;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class PuntuacionesController implements Initializable {
	
	@FXML
    private ListView<Puntuacion> listView;

    @FXML
    private BorderPane view;
    
    public PuntuacionesController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PuntuacionesView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
    private PuntuacionesModel model = new PuntuacionesModel();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.itemsProperty().bind(model.puntuacionesSortedProperty());
	}
	
	public PuntuacionesModel getModel() {
		return model;
	}

	public BorderPane getView() {
		return view;
	}
}
