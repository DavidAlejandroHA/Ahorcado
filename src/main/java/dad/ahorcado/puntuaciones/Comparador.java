package dad.ahorcado.puntuaciones;

import java.util.Comparator;

public class Comparador implements Comparator<Puntuacion> {

	@Override
	public int compare(Puntuacion o1, Puntuacion o2) {
		return o1.compareTo(o2);
	}
	
	// Esta clase por defecto no tiene uso, a no ser que la clase PuntuacionesModel la use
}
