package dad.ahorcado.partida;

import java.util.Optional;

import dad.ahorcado.AhorcadoApp;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;

public class PartidaModel {

	private ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
	private ListProperty<String> puntosJugadores = new SimpleListProperty<>(FXCollections.observableArrayList());

	private StringProperty palabraElegida = new SimpleStringProperty();
	private StringProperty palabraOcultadaEspaciada = new SimpleStringProperty();
	private StringProperty letrasJugadas = new SimpleStringProperty();
	private StringProperty letrasTextoTextField = new SimpleStringProperty();
	private StringProperty nombreJugador = new SimpleStringProperty();
	
	private IntegerProperty puntos = new SimpleIntegerProperty();
	
	private BooleanProperty botonValor = new SimpleBooleanProperty();
	
	private ObjectProperty<Image> imagen = new SimpleObjectProperty<>();
	
	private ListProperty<Puntuacion> puntuaciones = new SimpleListProperty<>();
	
	private String palabraOriginal;
	private String palabraOculta;
	private String letrasUsadas;
	private int puntosValor;
	
	private int indiceImagen;
	
	/** 
	 * Añade un listener que provoca la activación o desactivación del botón <br> 
	 * dependiendo de si la letra escrita ya ha sido usada previamente o <br>
	 * el texto introducido no es una letra sino una palabra (desactivado).
	 */
	public void anadirListenerBoton() {
		letrasTextoTextField.addListener( (o, ov, nv) -> {
			String letra = letrasTextoTextField.getValue();
			if(letrasJugadas.getValue().contains(nv) || letra.length()!= 1) {
				botonValor.set(true);
			} else {
				botonValor.set(false);
			}
		});
	}
	
	/**
	 * Comprueba si la palabra escrita actualmente es correcta.
	 */
	public void comprobarPalabra() {
		String palabra = letrasTextoTextField.getValue();
		if(palabra.equals(palabraElegida.getValue())) {
			for(int i = 0; i < palabraOculta.length(); i++) {
				if(palabraOculta.charAt(i)=='_') {
					++puntosValor;
					puntosJugadores.set(FXCollections.observableArrayList("Puntos:",(++puntosValor)+""));
					// cada letra sin haber sido acertada cuenta como 2 puntos más
				}
			}
			palabraOcultadaEspaciada.set(ocultarOEspaciarPalabra(palabraOriginal, false));
			nuevaPalabra(false);
		} else {
			actualizarImagen();
		}
	}
	
	/** 
	 * Comprueba si la letra escrita en el campo de texto es contenida por la <br> 
	 * palabra a adivinar. <br>
	 * En caso de acertar, se añade 1 punto al marcador.
	 */
	public void comprobarLetra() {
		String letra = letrasTextoTextField.getValue().charAt(0) + "";
		if(palabraElegida.get().contains(letra)) {
			revelarLetra(letra);
		} else {
			lanzarFallo(letra);
		}
		letrasTextoTextField.set("");
	}
	
	/** 
	 * Es la función encargada de restaurar todos los datos (excepto las palabras añadidas <br>
	 * y los registros) a su valor por defecto cuando se inicia o pierde la partida.
	 */
	public void recargarDatos() { // para el inicio y/o el final de la partida
		nuevaPalabra(true);
		botonValor.set(true);
		puntosValor = 0;
		puntosJugadores.set(FXCollections.observableArrayList("Puntos:",puntosValor+""));
		puntos.set(puntosValor);
	}
	
	/**
	 * Esta función se encarga de devolver un String cuyas letras han sido cambiadas <br>
	 * por un guión bajo ( _ ) o cuyo contenido es el mismo que el original salvo que <br>
	 * cada letra está separada por un espacio.
	 * @param p : String a transformar
	 * @param ocultarOEspaciar :<br>
	 * <ul>
	 * <li> true : Reemplaza todas las letras de la palabra por un guión bajo ( _ )
	 * <li> false : Añade espacios entre cada carácter a la palabra a retornar
	 * </ul>
	 * <br>
	 * 
	 * @return La palabra "censurada" o espaciada, dependiendo del valor de <b>ocultarOEspaciar</b>
	 */
	public String ocultarOEspaciarPalabra(String p, boolean ocultarOEspaciar) {
		String s = ""; // true -> oculta // false -> espacia
		for (int i = 0; i < p.trim().length(); i++) {
			if (Character.isLetterOrDigit(p.charAt(i)) || (p.charAt(i) + "").equals("_")) {
				s += ocultarOEspaciar ? "_" : p.charAt(i);
				s += ocultarOEspaciar ? "" : " ";
			} else if ((Character.isSpaceChar(p.charAt(i)))) {
				s += " ";
			}
		}
		return s;
	}
	
	
	public void lanzarFallo(String l) {
		if(actualizarImagen()) { // si siguen habiendo vidas (imágenes a recorrer) disponibles
			if(!letrasUsadas.contains(l)) {
				añadirLetraUsada(l);
			}
		}
	}
	
	/**
	 * Añade la letra que recibe al label de letras usadas
	 * @param l : letra a añadir
	 */
	public void añadirLetraUsada(String l) {
		letrasUsadas += " " + l;
		letrasUsadas = letrasUsadas.trim(); // elimina los espacios del inicio
		letrasJugadas.set(letrasUsadas);
	}
	
	public void terminarPartida() {
		TextInputDialog dialogo = new TextInputDialog();
    	dialogo.initOwner(AhorcadoApp.primaryStage);
    	dialogo.setTitle("Game Over");
    	dialogo.setHeaderText("¡Has perdido!");
    	dialogo.setContentText("Introduce tu nombre para que figures junto a tu puntuación en los registros:");
		Optional<String> nombre = dialogo.showAndWait();
    	puntos.set(puntosValor);
    	nombreJugador.set(nombre.get());
    	
    	Puntuacion puntuacionAdd = new Puntuacion(nombreJugador.getValue(), puntos.getValue());
    	
    	if(!puntuaciones.get().contains(puntuacionAdd) || !puntuaciones.stream() // si no existe el mismo jugador con la misma puntuación
    		/*y si el jugador ya existe pero no tiene la misma puntuación*/	.anyMatch(element -> getPuntos()== puntos.getValue()))
    		
    	puntuaciones.add(puntuacionAdd); // se añade la puntuación a la tabla de puntuaciones
    	// al haber binding bidireccional se actualizará la tabla del puntuacionesController
    	try {
			Puntuacion.guardarPuntuaciones("puntuaciones/puntuaciones.csv", puntuacionAdd);
			// cada vez que se termina una partida se guardan las puntuaciones del último jugador
		} catch (Exception e1) {
			puntuaciones.add(new Puntuacion("Error al guardar las puntuaciones. Al cerrar el programa la última puntuación añadida \n"
					+ "recientemente no aparecerá en la lista."));
		}
    	recargarDatos();
	}
	
	public boolean actualizarImagen() {
		boolean existe = false;
		String rutaImagen = null;
		try{
			rutaImagen = getClass().getResource("/hangman/" + getIndiceImagen() + ".png").toString();
			@SuppressWarnings("unused")
			String rutaImagenSiguiente = getClass().getResource("/hangman/" + (getIndiceImagen()+1) + ".png").toString();
			// esto es para que lanze excepción y se muestre la siguiente imagen en vez de que se muestre y volver a fallar para reiniciar
			setIndiceImagen(getIndiceImagen() + 1);
			setImagen(new Image(rutaImagen));
			existe = true;
			
		}catch (NullPointerException e) {
			setIndiceImagen(getIndiceImagen() + 1);
			setImagen(new Image(rutaImagen));
			terminarPartida();
		}
		
		return existe;
	}

	public void revelarLetra(String l) {
		char[] palabraArray = palabraOriginal.toCharArray();
		char[] palabraOcultaArray = palabraOculta.toCharArray();
		for (int i = 0; i < palabraOriginal.length(); i++) {
			if ((palabraArray[i] + "").equals(l)) {
				palabraOcultaArray[i] = palabraOriginal.charAt(i);
				puntosJugadores.set(FXCollections.observableArrayList("Puntos:",(++puntosValor)+""));
			}
		}
		
		palabraOculta = new String(palabraOcultaArray);
		palabraOcultadaEspaciada.set(ocultarOEspaciarPalabra(palabraOculta, false));
		añadirLetraUsada(l);
		if(palabraOculta.equals(palabraOriginal)) {
			nuevaPalabra(false);
		}
	}
	
	public void nuevaPalabra(boolean primeraVez) {
		if(!primeraVez) {
			Alert dialogo = new Alert(AlertType.INFORMATION);
	    	dialogo.initOwner(AhorcadoApp.primaryStage);
	    	dialogo.setTitle("Palabra acertada");
	    	dialogo.setHeaderText("¡Has acertado la palabra!");
	    	dialogo.showAndWait();
		}
		
		int rand = (int) (Math.random() * palabras.getSize());
		palabraOriginal = palabras.get(rand);
		palabraElegida.set(palabraOriginal); //
		palabraOculta = ocultarOEspaciarPalabra(palabraOriginal, true); // oculta
		palabraOcultadaEspaciada.set(ocultarOEspaciarPalabra(palabraOculta, false)); // espacia pero palabraoculta sigue siendo igual
		setIndiceImagen(1);
		actualizarImagen();
		letrasUsadas="";
		letrasJugadas.set("");
		letrasTextoTextField.set("");
		// System.out.println(palabraOriginal);    // Esto se puede descomentar en caso de que se quiera ver la palabra escogida por consola
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
	

	public final StringProperty letrasTextoTextFieldProperty() {
		return this.letrasTextoTextField;
	}
	

	public final String getLetrasTextoTextField() {
		return this.letrasTextoTextFieldProperty().get();
	}
	

	public final void setLetrasTextoTextField(final String letrasTextoTextField) {
		this.letrasTextoTextFieldProperty().set(letrasTextoTextField);
	}
	

	public final BooleanProperty botonValorProperty() {
		return this.botonValor;
	}
	

	public final boolean isBotonValor() {
		return this.botonValorProperty().get();
	}
	

	public final void setBotonValor(final boolean botonValor) {
		this.botonValorProperty().set(botonValor);
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

	public int getIndiceImagen() {
		return indiceImagen;
	}

	public void setIndiceImagen(int indiceImagen) {
		this.indiceImagen = indiceImagen;
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
