package modelo;

import controlador.BatallaController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.*;
import java.util.Random;

/**
 * Clase que representa la lógica de una batalla entre dos Pokémon: Charizard y Lucario.
 * 
 * Esta clase maneja el estado de los Pokémon, sus movimientos, el turno actual,
 * y la interacción con la interfaz a través del controlador BatallaController.
 * 
 * No contiene referencias directas a elementos gráficos como Stage o Scene, solo a controles UI necesarios
 * para actualizar la interfaz (labels y botones).
 */
public class Batalla {

    // Pokémon involucrados en la batalla
    Pokemon charizard, lucario;

    // Indica si es el turno de Charizard (true) o de Lucario (false)
    boolean turnoCharizard = true;

    // Referencias a etiquetas de la interfaz para mostrar información de turno y vida
    Label labelTurno;
    Label vidaCharizard, vidaLucario;

    // Arreglos con botones de movimientos para cada Pokémon (para actualizar estado y texto)
    Button[] botonesMovCharizard = new Button[4];
    Button[] botonesMovLucario = new Button[4];

    // Controlador asociado para comunicación con la UI
    private BatallaController controller;

    /**
     * Constructor que inicializa los Pokémon y sus movimientos con valores predeterminados.
     * Establece la vida inicial al 100% (1.0).
     */
    public Batalla() {
        charizard = new Pokemon("Mega Charizard", new Movimiento[]{
                new Movimiento("Llamarada", 20, 2),
                new Movimiento("Garra Dragón", 12, 6),
                new Movimiento("Acróbata", 11, 7),
                new Movimiento("Lanzallamas", 8, 10)
        });
        charizard.setVida(1.0);

        lucario = new Pokemon("Mega Lucario", new Movimiento[]{
                new Movimiento("Abocajarro", 25, 3),
                new Movimiento("Patada Alta", 12, 5),
                new Movimiento("Puño Certero", 10, 7),
                new Movimiento("Esfera Aural", 5, 6)
        });
        lucario.setVida(1.0);
    }

    /**
     * Establece el controlador de la batalla para poder actualizar la interfaz gráfica.
     * Obtiene referencias a etiquetas y botones desde el controlador.
     * 
     * @param controller Instancia del controlador BatallaController.
     */
    public void setController(BatallaController controller) {
        this.controller = controller;
        this.labelTurno = controller.getLabelTurno();
        this.vidaCharizard = controller.getVidaCharizard();
        this.vidaLucario = controller.getVidaLucario();
        this.botonesMovCharizard = controller.getBotonesCharizard();
        //this.botonesMovLucario = controller.getBotonesLucario();
    }

    /**
     * Método para realizar un ataque.
     * Se especifica si ataca Charizard (true) o Lucario (false),
     * y el índice del movimiento que se usará.
     * 
     * Se decrementan los PP del movimiento, se calcula el daño y se actualizan las barras y botones.
     * Cambia el turno y actualiza el texto en la interfaz.
     * 
     * @param esCharizard true si ataca Charizard, false si ataca Lucario.
     * @param movIndex índice del movimiento usado.
     */
    public void atacar(boolean esCharizard, int movIndex) {
        Pokemon atacante = esCharizard ? charizard : lucario;
        Pokemon defensor = esCharizard ? lucario : charizard;
        Movimiento mov = atacante.movimientos[movIndex];

        if (mov.pp <= 0) {
            System.out.println("Sin PP para " + mov.nombre);
            return;
        }

        mov.pp--;

        defensor.vida -= mov.dano / 100.0;
        if (defensor.vida < 0) defensor.vida = 0;

        System.out.println(atacante.nombre + " usó " + mov.nombre + ". Vida de " + defensor.nombre + ": " + (int) (defensor.vida * 100) + "%");

        actualizarBarrasVida();
        actualizarBotones();

        if (defensor.vida <= 0) {
            labelTurno.setText("¡" + defensor.nombre + " ha sido derrotado!");
            verificarFinBatalla();
            controller.desactivarBotones();
        } else {
            turnoCharizard = !turnoCharizard;
            labelTurno.setText(turnoCharizard ? "Turno de Charizard" : "Turno de Lucario");
        }
        controller.actualizarUI();
    }

    /**
     * Método que ejecuta un ataque aleatorio de Lucario.
     * Se asegura de que Lucario tenga movimientos con PP disponible antes de atacar.
     */
    public void ataqueAleatorioLucario() {
        if (charizard.getVida() <= 0 || lucario.getVida() <= 0) return;

        Random rand = new Random();
        Movimiento[] movimientos = lucario.getMovimientos();

        int intentos = 0;
        int index;
        do {
            index = rand.nextInt(movimientos.length);
            intentos++;
        } while (movimientos[index].getPp() <= 0 && intentos < 10);

        if (movimientos[index].getPp() > 0) {
            atacar(false, index); 
        } 
    }

    /**
     * Inicializa o reinicia una partida nueva.
     * Restablece la vida, turno y PP de movimientos de ambos Pokémon.
     * Actualiza la interfaz y muestra mensaje por consola.
     */
    public void iniciarPartidaNueva() {
        charizard.setVida(1.0);
        lucario.setVida(1.0);
        turnoCharizard = true;
        labelTurno.setText("Turno de Charizard");
        charizard.resetMovimientos();
        lucario.resetMovimientos();
        actualizarBarrasVida();
        actualizarBotones();
        System.out.println("Iniciando partida nueva...");
    }

    /**
     * Actualiza las barras de vida y etiquetas con el porcentaje actual de vida de cada Pokémon.
     */
    private void actualizarBarrasVida() {
        charizard.actualizarBarra();
        lucario.actualizarBarra();
        vidaCharizard.setText((int) (charizard.vida * 100) + "%");
        vidaLucario.setText((int) (lucario.vida * 100) + "%");
    }

    /**
     * Actualiza los textos y estado (habilitado/deshabilitado) de los botones de movimientos
     * de Charizard según el turno actual y los PP restantes.
     */
    private void actualizarBotones() {
        for (int i = 0; i < charizard.movimientos.length; i++) {
            Movimiento m = charizard.movimientos[i];
            botonesMovCharizard[i].setText(m.nombre + " (PP: " + m.pp + "/" + m.ppMax + ")");
            botonesMovCharizard[i].setDisable(!turnoCharizard || m.pp <= 0 || charizard.vida <= 0);
        }
        /* Código comentado para deshabilitar botones de Lucario si fuera necesario
        for (int i = 0; i < lucario.movimientos.length; i++) {
            botonesMovLucario[i].setDisable(true);
        }*/
    }

    /**
     * Guarda el estado actual de la partida en un archivo de texto local.
     * Se almacenan vida, turno y PP de movimientos de ambos Pokémon.
     */
    public void guardarPartida() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("partida_guardada.txt"))) {
            writer.println(charizard.vida);
            writer.println(lucario.vida);
            writer.println(turnoCharizard);
            for (Movimiento m : charizard.movimientos) writer.println(m.pp);
            for (Movimiento m : lucario.movimientos) writer.println(m.pp);
            System.out.println("Partida guardada.");
        } catch (IOException e) {
            System.out.println("Error al guardar partida.");
        }
    }

    /**
     * Carga una partida previamente guardada desde archivo de texto local,
     * actualizando vida, turno y PP de movimientos.
     * Actualiza también la interfaz con los nuevos valores cargados.
     */
    public void cargarPartida() {
        try (BufferedReader reader = new BufferedReader(new FileReader("partida_guardada.txt"))) {
            charizard.vida = Double.parseDouble(reader.readLine());
            lucario.vida = Double.parseDouble(reader.readLine());
            turnoCharizard = Boolean.parseBoolean(reader.readLine());
            for (Movimiento m : charizard.movimientos) m.pp = Integer.parseInt(reader.readLine());
            for (Movimiento m : lucario.movimientos) m.pp = Integer.parseInt(reader.readLine());

            actualizarBarrasVida();
            actualizarBotones();
            labelTurno.setText(turnoCharizard ? "Turno de Charizard" : "Turno de Lucario");

            System.out.println("Partida cargada.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar partida: " + e.getMessage());
        }
    }

    /**
     * Deshabilita todos los botones de movimientos de ambos Pokémon (por ejemplo, al finalizar batalla).
     */
    private void deshabilitarTodosLosBotones() {
        for (Button b : botonesMovCharizard) {
            b.setDisable(true);
        }
        for (Button b : botonesMovLucario) {
            b.setDisable(true);
        }
    }

    /**
     * Verifica si la batalla ha finalizado por derrota de alguno de los Pokémon,
     * muestra mensaje en consola y deshabilita botones.
     */
    private void verificarFinBatalla() {
        if (charizard.getVida() <= 0) {
            System.out.println("¡Lucario ganó!");
            deshabilitarTodosLosBotones();
        } else if (lucario.getVida() <= 0) {
            System.out.println("¡Charizard ganó!");
            deshabilitarTodosLosBotones();
        }
    }

    // Getters para acceder a los Pokémon y estado de turno desde otras clases/controladores.

    public Pokemon getCharizard() {
        return charizard;
    }

    public Pokemon getLucario() {
        return lucario;
    }

    public boolean getTurnoCharizard() {
        return turnoCharizard;
    }
}
