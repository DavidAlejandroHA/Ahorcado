package dad.ahorcado.palabras;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PalabrasModel {
	
	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ObjectProperty<String> seleccionPalabra = new SimpleObjectProperty<>();
	
	public ListProperty<String> cargarPalabras(ListProperty<String> lp) {
		
		try {
			Path path = new File("palabras/palabras.csv").toPath();
			lp.addAll(Files.readAllLines(path, StandardCharsets.UTF_8));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ListProperty<String> lpnew = new SimpleListProperty<>(FXCollections.observableArrayList());
		
		lpnew.addAll(lp.stream()
				.filter(s -> s.length()>0) // esto es para que no existan palabras "vacías" (retorno de carro)
				.collect(Collectors.toList()));
		return lpnew;
	}
	
	public final ListProperty<String> palabrasProperty() {
		return this.palabras;
	}
	
	public final ObservableList<String> getPalabras() {
		return this.palabrasProperty().get();
	}
	
	public final void setPalabras(final ObservableList<String> palabras) {
		this.palabrasProperty().set(palabras);
	}
	
	public final ObjectProperty<String> seleccionPalabraProperty() {
		return this.seleccionPalabra;
	}
	
	public final String getSeleccionPalabra() {
		return this.seleccionPalabraProperty().get();
	}
	
	public final void setSeleccionPalabra(final String seleccionPalabra) {
		this.seleccionPalabraProperty().set(seleccionPalabra);
	}
}
