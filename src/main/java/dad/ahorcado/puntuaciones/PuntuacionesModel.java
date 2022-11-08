package dad.ahorcado.puntuaciones;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PuntuacionesModel {
	
	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<Puntuacion> puntuacionesSorted = new SimpleListProperty<>(puntuaciones.sorted(/*comparator*/
																							(o1, o2) -> o1.compareTo(o2)));/*new Comparador()*/
																								/**
																								 * En caso de no usar el operador lambda, es posible
																								 * hacerlo en su lugar con la clase "Comparador"
																								 */
	public final ListProperty<Puntuacion> puntuacionesProperty() {
		return this.puntuaciones;
	}

	public final ObservableList<Puntuacion> getPuntuaciones() {
		return this.puntuacionesProperty().get();
	}
	
	public final void setPuntuaciones(final ObservableList<Puntuacion> puntuaciones) {
		this.puntuacionesProperty().set(puntuaciones);
	}
	
	public final ListProperty<Puntuacion> puntuacionesSortedProperty() {
		return this.puntuacionesSorted;
	}
	
	public final ObservableList<Puntuacion> getPuntuacionesSorted() {
		return this.puntuacionesSortedProperty().get();
	}
	
	public final void setPuntuacionesSorted(final ObservableList<Puntuacion> puntuacionesSorted) {
		this.puntuacionesSortedProperty().set(puntuacionesSorted);
	}
}
