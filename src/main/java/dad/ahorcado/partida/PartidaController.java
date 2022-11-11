package dad.ahorcado.partida;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.ahorcado.AhorcadoApp;
import dad.ahorcado.puntuaciones.Puntuacion;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
	
	private String palabraOriginal;
	private String palabraOculta;
	private String letrasUsadas;
	private int puntosValor;
	
	private int indiceImagen;
	
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
		letraBoton.disableProperty().bind(model.botonLetraValorProperty());
		resolverBoton.disableProperty().bind(model.botonPalabraValorProperty());
		palabraAdivinarLabel.textProperty().bind(model.palabraOcultadaEspaciadaProperty());
		letrasUsadasLabel.textProperty().bind(model.letrasJugadasProperty());
		imagenLabel.imageProperty().bind(model.imagenProperty());
		
		/** 
		 * Añade un listener que provoca la activación o desactivación del botón <br> 
		 * dependiendo de si la letra escrita ya ha sido usada previamente o <br>
		 * el texto introducido no es una letra sino una palabra (desactivado).
		 */
		
		// load data
		model.setBotonLetraValor(true);
		model.setBotonPalabraValor(true);
		
		model.setPermitir(false);
		
		textoTextField.textProperty().addListener( (o, ov, nv) -> {
			
		if(model.isPermitir()) {
			String letra = eliminarAcentos(textoTextField.textProperty().getValue()).toLowerCase();
			if(letra.length()> 1) {
				letraBoton.defaultButtonProperty().set(false);
				resolverBoton.defaultButtonProperty().set(true);
				model.setBotonLetraValor(true);
				model.setBotonPalabraValor(false);
			} else if (model.getLetrasJugadas().contains(eliminarAcentos(nv).toLowerCase()) && letra.length() == 1){
				letraBoton.defaultButtonProperty().set(false);
				resolverBoton.defaultButtonProperty().set(false);
				model.setBotonLetraValor(true);
				model.setBotonPalabraValor(true);
			} else if (!model.getLetrasJugadas().contains(eliminarAcentos(nv).toLowerCase()) && letra.length() == 1){
				letraBoton.defaultButtonProperty().set(true);
				resolverBoton.defaultButtonProperty().set(false);
				model.setBotonLetraValor(false);
				model.setBotonPalabraValor(true);
			}else {
				letraBoton.defaultButtonProperty().set(false);
				resolverBoton.defaultButtonProperty().set(false);
				model.setBotonLetraValor(true);
				model.setBotonPalabraValor(true);
			}
		}
		});
	}
	
	/**
	 * Comprueba si la palabra escrita actualmente es correcta.
	 */
	public void comprobarPalabra() {
		String palabra = textoTextField.textProperty().getValue();
		if(eliminarAcentos(palabra.toLowerCase()).equals(eliminarAcentos(model.getPalabraElegida().toLowerCase()))) {
			for(int i = 0; i < palabraOculta.length(); i++) {
				if(palabraOculta.charAt(i)=='_') {
					++puntosValor;
					model.setPuntosJugadores(FXCollections.observableArrayList("Puntos:",(++puntosValor)+""));
					// cada letra sin haber sido acertada cuenta como 2 puntos más
				}
			}
			model.setPalabraOcultadaEspaciada(ocultarOEspaciarPalabra(palabraOriginal, false));
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
		String letra = eliminarAcentos(textoTextField.textProperty().getValue().charAt(0) + "").toLowerCase();
		if(eliminarAcentos(model.getPalabraElegida().toLowerCase()).contains(letra)) {
			revelarLetra(letra);
		} else {
			lanzarFallo(letra);
		}
		textoTextField.textProperty().set("");
	}
	
	/** 
	 * Es la función encargada de restaurar todos los datos (excepto las palabras añadidas <br>
	 * y los registros) a su valor por defecto cuando se inicia o pierde la partida.
	 */
	public void recargarDatos() { // para el inicio y/o el final de la partida
		model.setBotonLetraValor(true);
		model.setBotonPalabraValor(true);
		nuevaPalabra(true);
		puntosValor = 0;
		model.setPuntosJugadores(FXCollections.observableArrayList("Puntos:",puntosValor+""));
		model.setPuntos(puntosValor);
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
		p = p.trim();
		for (int i = 0; i < p.length(); i++) {
			if (Character.isLetter(p.charAt(i)) || (p.charAt(i) == '_')) {
				s += ocultarOEspaciar ? "_" : p.charAt(i);
				s += ocultarOEspaciar ? "" : " ";
			} else if ((Character.isSpaceChar(p.charAt(i)))) {
				s += ocultarOEspaciar ? " " : "  ";
			} else if (Character.isDigit(p.charAt(i))) {
				s += p.charAt(i);
				s += ocultarOEspaciar ? "" : " ";
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
		letrasUsadas += letrasUsadas.length() > 0 ? " " + l : l; // evita añadir espacio en el inicio
		model.setLetrasJugadas(letrasUsadas);
	}
	
	public void terminarPartida() {
		TextInputDialog dialogo = new TextInputDialog();
    	dialogo.initOwner(AhorcadoApp.primaryStage);
    	dialogo.setTitle("Game Over");
    	dialogo.setHeaderText("¡Has perdido!");
    	dialogo.setContentText("Introduce tu nombre para que figures junto a tu puntuación en los registros:");
    	Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(AhorcadoApp.class.getResourceAsStream("/hangman/9.png")));
		Optional<String> nombre = dialogo.showAndWait();
		model.setPuntos(puntosValor);
		try {
			model.setNombreJugador(nombre.get()); // si se salió dándole a la x nombre va a ser nulo y va a salir excepción
			if(nombre.get().trim().length()>1) { // si se añaden espacios en blanco entonces no entra abajo y no hace nada
				Puntuacion puntuacionAdd = new Puntuacion(model.getNombreJugador(), model.getPuntos());
		    	
		    	if(!model.getPuntuaciones().contains(puntuacionAdd) || !model.getPuntuaciones().stream() // si no existe el mismo jugador con la misma puntuación
		    		/*y si el jugador ya existe pero no tiene la misma puntuación*/	.anyMatch(element -> element.getPuntos() == model.getPuntos()))
		    		
		    		model.getPuntuaciones().add(puntuacionAdd); // se añade la puntuación a la tabla de puntuaciones
		    		// al haber binding bidireccional se actualizará la tabla del puntuacionesController
		    	try {
					Puntuacion.guardarPuntuaciones("puntuaciones/puntuaciones.csv", puntuacionAdd);
					// cada vez que se termina una partida se guardan las puntuaciones del último jugador
				} catch (IOException e1) {
					/*model.getPuntuaciones().add(new Puntuacion("Error al guardar las puntuaciones. Al cerrar el programa la última puntuación añadida \n"
							+ "recientemente no aparecerá en la lista."));*/
					Alert alerta = new Alert(AlertType.ERROR);
					alerta.initOwner(AhorcadoApp.primaryStage);
			    	alerta.setTitle("Error en las puntuaciones");
			    	alerta.setHeaderText("Error al guardar las puntuaciones. Al cerrar el programa la última puntuación añadida "
							+ "recientemente no aparecerá en la lista.");
			    	Stage stage_error = (Stage) alerta.getDialogPane().getScene().getWindow();
			        stage_error.getIcons().add(new Image(AhorcadoApp.class.getResourceAsStream("/hangman/9.png")));
			    	alerta.showAndWait();
				}
			}
		} catch (NoSuchElementException e) {
			//e.printStackTrace();
			// La puntuación simplemente no se guarda al no haber indicado un nombre
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
			model.setImagen(new Image(rutaImagen));
			existe = true;
			
		}catch (NullPointerException e) {
			setIndiceImagen(getIndiceImagen() + 1);
			model.setImagen(new Image(rutaImagen));
			terminarPartida();
		}
		
		return existe;
	}

	public void revelarLetra(String l) {
		char[] palabraArray = palabraOriginal.toCharArray();
		char[] palabraOcultaArray = palabraOculta.toCharArray();
		for (int i = 0; i < palabraOriginal.length(); i++) {
			if (eliminarAcentos(palabraArray[i] + "").equals(eliminarAcentos(l)) && !Character.isDigit(palabraArray[i])) {
				palabraOcultaArray[i] = palabraOriginal.charAt(i);
				model.setPuntosJugadores(FXCollections.observableArrayList("Puntos:",(++puntosValor)+""));
			}
		}
		
		palabraOculta = new String(palabraOcultaArray);
		model.setPalabraOcultadaEspaciada(ocultarOEspaciarPalabra(palabraOculta, false));
		if(!Character.isDigit(l.charAt(0))) {
			añadirLetraUsada(eliminarAcentos(l)); // lo añade si no es un número. Los números no son ocultos por defecto
		}
		if(palabraOculta.equals(palabraOriginal)) {
			nuevaPalabra(false);
		}
	}
	
	public String eliminarAcentos(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}
	
	public void nuevaPalabra(boolean primeraVez) {
		if(!primeraVez) {
			Alert alerta = new Alert(AlertType.INFORMATION);
	    	alerta.initOwner(AhorcadoApp.primaryStage);
	    	alerta.setTitle("Palabra acertada");
	    	alerta.setHeaderText("¡Has acertado la palabra!");
	    	Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
	        stage.getIcons().add(new Image(AhorcadoApp.class.getResourceAsStream("/hangman/9.png")));
	    	alerta.showAndWait();
		}
		
		int rand = (int) (Math.random() * model.getPalabras().size());
		palabraOriginal = model.getPalabras().get(rand);
		model.setPalabraElegida(palabraOriginal); //
		palabraOculta = ocultarOEspaciarPalabra(palabraOriginal, true); // oculta
		model.setPalabraOcultadaEspaciada(ocultarOEspaciarPalabra(palabraOculta, false)); // espacia pero palabraoculta sigue siendo igual
		setIndiceImagen(1);
		actualizarImagen();
		letrasUsadas="";
		model.setLetrasJugadas("");
		textoTextField.textProperty().set("");
		// System.out.println(palabraOriginal);    // Esto se puede descomentar en caso de que se quiera ver la palabra escogida por consola
	}
	
	public int getIndiceImagen() {
		return indiceImagen;
	}

	public void setIndiceImagen(int indiceImagen) {
		this.indiceImagen = indiceImagen;
	}
	
	public GridPane getView() {
		return view;
	}
	
	@FXML
	void onLetra(ActionEvent event) {
		comprobarLetra();
	}
	
	@FXML
	void onResolver(ActionEvent event) {
		comprobarPalabra();
	}
	
	public PartidaModel getModel() {
		return model;
	}
}