package dad.ahorcado.partida;

import dad.ahorcado.puntuaciones.Puntuacion;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class PartidaModel {

	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<String> puntosJugadores = new SimpleListProperty<>(FXCollections.observableArrayList());

	private StringProperty palabraElegida = new SimpleStringProperty();
	private StringProperty palabraOcultadaEspaciada = new SimpleStringProperty();
	private StringProperty letrasJugadas = new SimpleStringProperty();
	private StringProperty nombreJugador = new SimpleStringProperty();

	private IntegerProperty puntos = new SimpleIntegerProperty();

	private BooleanProperty botonLetraValor = new SimpleBooleanProperty();
	private BooleanProperty botonPalabraValor = new SimpleBooleanProperty();
	private BooleanProperty permitir = new SimpleBooleanProperty();

	private ObjectProperty<Image> imagen = new SimpleObjectProperty<>();

	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>();

	public final ListProperty<String> palabrasProperty() {
		return this.palabras;
	}

	public final ObservableList<String> getPalabras() {
		return this.palabrasProperty().get();
	}

	public final void setPalabras(final ObservableList<String> palabras) {
		this.palabrasProperty().set(palabras);
	}

	public final ListProperty<String> puntosJugadoresProperty() {
		return this.puntosJugadores;
	}

	public final ObservableList<String> getPuntosJugadores() {
		return this.puntosJugadoresProperty().get();
	}

	public final void setPuntosJugadores(final ObservableList<String> puntosJugadores) {
		this.puntosJugadoresProperty().set(puntosJugadores);
	}

	public final StringProperty palabraElegidaProperty() {
		return this.palabraElegida;
	}

	public final String getPalabraElegida() {
		return this.palabraElegidaProperty().get();
	}

	public final void setPalabraElegida(final String palabraElegida) {
		this.palabraElegidaProperty().set(palabraElegida);
	}

	public final StringProperty palabraOcultadaEspaciadaProperty() {
		return this.palabraOcultadaEspaciada;
	}

	public final String getPalabraOcultadaEspaciada() {
		return this.palabraOcultadaEspaciadaProperty().get();
	}

	public final void setPalabraOcultadaEspaciada(final String palabraOcultadaEspaciada) {
		this.palabraOcultadaEspaciadaProperty().set(palabraOcultadaEspaciada);
	}

	public final StringProperty letrasJugadasProperty() {
		return this.letrasJugadas;
	}

	public final String getLetrasJugadas() {
		return this.letrasJugadasProperty().get();
	}

	public final void setLetrasJugadas(final String letrasJugadas) {
		this.letrasJugadasProperty().set(letrasJugadas);
	}

	public final StringProperty nombreJugadorProperty() {
		return this.nombreJugador;
	}

	public final String getNombreJugador() {
		return this.nombreJugadorProperty().get();
	}

	public final void setNombreJugador(final String nombreJugador) {
		this.nombreJugadorProperty().set(nombreJugador);
	}

	public final IntegerProperty puntosProperty() {
		return this.puntos;
	}

	public final int getPuntos() {
		return this.puntosProperty().get();
	}

	public final void setPuntos(final int puntos) {
		this.puntosProperty().set(puntos);
	}

	public final BooleanProperty botonLetraValorProperty() {
		return this.botonLetraValor;
	}

	public final boolean isBotonLetraValor() {
		return this.botonLetraValorProperty().get();
	}

	public final void setBotonLetraValor(final boolean botonLetraValor) {
		this.botonLetraValorProperty().set(botonLetraValor);
	}

	public final BooleanProperty botonPalabraValorProperty() {
		return this.botonPalabraValor;
	}

	public final boolean isBotonPalabraValor() {
		return this.botonPalabraValorProperty().get();
	}

	public final void setBotonPalabraValor(final boolean botonPalabraValor) {
		this.botonPalabraValorProperty().set(botonPalabraValor);
	}

	public final BooleanProperty permitirProperty() {
		return this.permitir;
	}

	public final boolean isPermitir() {
		return this.permitirProperty().get();
	}

	public final void setPermitir(final boolean permitir) {
		this.permitirProperty().set(permitir);
	}

	public final ObjectProperty<Image> imagenProperty() {
		return this.imagen;
	}

	public final Image getImagen() {
		return this.imagenProperty().get();
	}

	public final void setImagen(final Image imagen) {
		this.imagenProperty().set(imagen);
	}

	public final ListProperty<Puntuacion> puntuacionesProperty() {
		return this.puntuaciones;
	}

	public final ObservableList<Puntuacion> getPuntuaciones() {
		return this.puntuacionesProperty().get();
	}

	public final void setPuntuaciones(final ObservableList<Puntuacion> puntuaciones) {
		this.puntuacionesProperty().set(puntuaciones);
	}

}
