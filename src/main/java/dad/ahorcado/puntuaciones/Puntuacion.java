package dad.ahorcado.puntuaciones;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Puntuacion implements Comparable<Puntuacion>{
	
	private String nombre;
	private int puntos;

	public Puntuacion(String nombre, int puntos){
		this.nombre = nombre;
		this.puntos = puntos;
	}
	
	public Puntuacion(String nombre){ // Este constructor será útil para añadir un mensaje a la tabla en caso de que se quiera dar un aviso
		this.nombre = nombre;																								// o mensaje
		this.puntos = -1;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	
	@Override
	public String toString() {	// Si puntos está a -1 no muestra los puntos. Si está a -1 es porque se usó el segundo el segundo constructor
		return nombre + (puntos>=0 ? ": " : "") + (puntos>=0 ? puntos+(puntos>1 ? " puntos." : " punto.") : "");
	}

	@Override
	public int compareTo(Puntuacion p) {
		return p.puntos - this.puntos;
	}
	
	public static List<Puntuacion> loadPuntuaciones(String filename) throws Exception{
		File file = new File(filename);
		if(!file.exists()) {
			return new ArrayList<>();
		}
		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8); // método no estático
		return lines.stream() // flujo de objetos de la colección
					.filter(s -> s.length()>0) // esto es por si existe líneas "vacías" que contienen el retorno de carro
					.map(line -> line.split(","))
					.map(parts -> {
						String nombre = parts[0];
						int puntos =Integer.parseInt(parts[1]);
						return new Puntuacion(nombre, puntos);
					})
					.collect(Collectors.toList());
	}
	
	public static void guardarPuntuaciones(String filename, Puntuacion p) throws Exception {
		Path path = new File(filename).toPath();
		Files.writeString(path, "\n"+p.getNombre()+","+p.getPuntos(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	}
}
