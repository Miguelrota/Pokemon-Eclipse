package modelo;

import java.util.Random;
import javafx.scene.control.ProgressBar;

/**
 * Representa un Pokémon con nombre, vida, movimientos y una barra visual de vida.
 */
public class Pokemon {

    /** Nombre del Pokémon (ejemplo: "Mega Charizard") */
    public String nombre;

    /** Vida actual del Pokémon, representada como un valor entre 0.0 y 1.0 */
    double vida = 1.0;

    /** Barra de progreso que representa visualmente la vida del Pokémon */
    ProgressBar barraVida;

    /** Array de movimientos que el Pokémon puede usar en batalla */
    public Movimiento[] movimientos;

    /**
     * Constructor para crear un Pokémon con nombre y movimientos.
     * Inicializa la barra de vida con la vida inicial.
     * 
     * @param nombre Nombre del Pokémon
     * @param movimientos Array de movimientos disponibles para este Pokémon
     */
    public Pokemon(String nombre, Movimiento[] movimientos) {
        this.nombre = nombre;
        this.movimientos = movimientos;
        this.barraVida = new ProgressBar(vida);
    }

    /**
     * Obtiene la vida actual del Pokémon.
     * @return Vida en rango 0.0 - 1.0
     */
    public double getVida() {
        return vida;
    }

    /**
     * Establece la vida actual del Pokémon.
     * @param vida Nuevo valor de vida (0.0 - 1.0)
     */
    public void setVida(double vida) {
        this.vida = vida;
    }

    /**
     * Obtiene la barra de progreso que representa la vida visualmente.
     * @return Barra de progreso de vida
     */
    public ProgressBar getBarraVida() {
        return barraVida;
    }

    /**
     * Obtiene el array de movimientos disponibles del Pokémon.
     * @return Array de objetos Movimiento
     */
    public Movimiento[] getMovimientos() {
        return movimientos;
    }

    /**
     * Restaura los PP (puntos de poder) de todos los movimientos al máximo.
     */
    public void resetMovimientos() {
        for (Movimiento m : movimientos) {
            m.resetPP();
        }
    }

    /**
     * Selecciona y devuelve un movimiento al azar entre los disponibles.
     * @return Movimiento elegido aleatoriamente
     */
    public Movimiento ataqueAleatorio() {
        Random rand = new Random();
        int index = rand.nextInt(movimientos.length); // elige índice al azar
        return movimientos[index];
    }

    /**
     * Actualiza la barra de vida para reflejar el estado actual de vida.
     */
    public void actualizarBarra() {
        barraVida.setProgress(vida);
    }
}
