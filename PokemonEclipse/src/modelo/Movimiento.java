package modelo;

/**
 * Representa un movimiento o ataque que un Pokémon puede realizar en batalla.
 * 
 * Contiene información sobre el nombre del movimiento, el daño que inflige,
 * y los puntos de poder (PP) disponibles para usarlo.
 */
public class Movimiento {

    /** Nombre del movimiento (ej. "Llamarada") */
    public String nombre;

    /** Daño que inflige el movimiento */
    double dano;

    /** Puntos de poder actuales (PP), que indican cuántas veces se puede usar el movimiento */
    public int pp;

    /** Puntos de poder máximos (PP máximos) */
    public int ppMax;

    /**
     * Constructor para crear un movimiento con su nombre, daño y PP máximo.
     * Inicializa los PP actuales al máximo.
     * 
     * @param nombre Nombre del movimiento
     * @param dano Daño que inflige el movimiento
     * @param ppMax Puntos de poder máximos (PP máximo)
     */
    public Movimiento(String nombre, double dano, int ppMax) {
        this.nombre = nombre;
        this.dano = dano;
        this.ppMax = ppMax;
        this.pp = ppMax;
    }

    /**
     * Restaura los PP del movimiento al valor máximo.
     */
    public void resetPP() {
        this.pp = ppMax;
    }

    // Getters y setters

    /**
     * Obtiene los puntos de poder actuales.
     * @return PP actuales
     */
    public int getPp() {
        return pp;
    }

    /**
     * Establece los puntos de poder actuales.
     * @param pp Nuevos puntos de poder
     */
    public void setPp(int pp) {
        this.pp = pp;
    }

    /**
     * Obtiene el daño del movimiento.
     * @return Daño infligido
     */
    public double getDano() {
        return dano;
    }

    /**
     * Establece el daño del movimiento.
     * @param dano Nuevo daño
     */
    public void setDano(double dano) {
        this.dano = dano;
    }
}
