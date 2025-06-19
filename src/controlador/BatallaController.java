package controlador;

import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.SQLException;

import application.AccesoDatos;
import application.AccesoDatos.PartidaGuardada;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import modelo.Batalla;
import modelo.Movimiento;
import modelo.Pokemon;

/**
 * Controlador para manejar la lógica y la interfaz de la batalla entre dos Pokémon:
 * Charizard y Lucario.
 * 
 * Gestiona los ataques disponibles, actualiza la interfaz de usuario con el estado actual
 * de la batalla (vida de los Pokémon, estado de los movimientos) y permite guardar y cargar partidas.
 */
public class BatallaController {

    // Indica si es el turno de Charizard.
    private boolean turnoCharizard;

    // Instancias de Pokémon que participan en la batalla.
    private Pokemon charizard;
    private Pokemon lucario;

    // Objeto que maneja la lógica de la batalla.
    private Batalla batalla;

    // Componentes gráficos de la interfaz vinculados con FXML.
    @FXML private ProgressBar barraCharizard;  // Barra de vida de Charizard.
    @FXML private ProgressBar barraLucario;    // Barra de vida de Lucario.
    @FXML private Label vidaCharizard;         // Texto que muestra el porcentaje de vida de Charizard.
    @FXML private Label vidaLucario;            // Texto que muestra el porcentaje de vida de Lucario.

    // Botones para los diferentes movimientos de Charizard.
    @FXML private Button botonLlamarada;
    @FXML private Button botonLanzallamas;
    @FXML private Button botonDragon;
    @FXML private Button botonAcrobata;

    @FXML private ImageView imagenFuego;        // Imagen relacionada con los ataques de fuego (posiblemente decorativa).

    @FXML private Button guardarPartida;        // Botón para guardar el estado actual de la batalla.

    @FXML private Label labelTurno;              // Label que indica de quién es el turno actual.

    /**
     * Asigna la batalla activa al controlador y actualiza la interfaz gráfica.
     * 
     * @param batalla instancia del objeto Batalla que maneja la lógica de combate.
     */
    public void setBatalla(Batalla batalla) {
        this.batalla = batalla;
        actualizarUI();
    }

    /**
     * Obtiene el label que muestra el turno actual.
     * 
     * @return Label que indica el turno.
     */
    public Label getLabelTurno() {
        return labelTurno;
    }

    /**
     * Método asociado al botón "Llamarada". Realiza el ataque de tipo "Llamarada".
     * Luego, si Lucario sigue con vida, ejecuta un ataque aleatorio de Lucario.
     */
    @FXML
    private void atacarConLlamarada() {
        if (batalla == null) return;
        batalla.atacar(true, 0);
        if (batalla.getLucario().getVida() > 0) {
            batalla.ataqueAleatorioLucario();
        }
        actualizarUI();
    }

    /**
     * Método asociado al botón "Garra Dragón". Realiza el ataque "Garra Dragón".
     * Luego, si Lucario sigue con vida, ejecuta un ataque aleatorio de Lucario.
     */
    @FXML
    private void atacarConGarraDragon() {
        if (batalla == null) return;
        batalla.atacar(true, 1);
        if (batalla.getLucario().getVida() > 0) {
            batalla.ataqueAleatorioLucario();
        }
        actualizarUI();
    }

    /**
     * Método asociado al botón "Acrobata". Realiza el ataque "Acrobata".
     * Luego, si Lucario sigue con vida, ejecuta un ataque aleatorio de Lucario.
     */
    @FXML
    private void atacarConAcrobata() {
        if (batalla == null) return;
        batalla.atacar(true, 2);
        if (batalla.getLucario().getVida() > 0) {
            batalla.ataqueAleatorioLucario();
        }
        actualizarUI();
    }

    /**
     * Método asociado al botón "Lanzallamas". Realiza el ataque "Lanzallamas".
     * Luego, si Lucario sigue con vida, ejecuta un ataque aleatorio de Lucario.
     */
    @FXML
    private void atacarConLanzallamas() {
        if (batalla == null) return;
        batalla.atacar(true, 3);
        if (batalla.getLucario().getVida() > 0) {
            batalla.ataqueAleatorioLucario();
        }
        actualizarUI();
    }

    /**
     * Actualiza la interfaz gráfica con el estado actual de la batalla:
     * - Barras de vida de ambos Pokémon.
     * - Texto con porcentaje de vida.
     * - Estado (habilitado/deshabilitado) y texto de los botones según los PP disponibles.
     */
    public void actualizarUI() {
        if (batalla != null) {
            Pokemon charizard = batalla.getCharizard();
            Pokemon lucario = batalla.getLucario();

            barraCharizard.setProgress(charizard.getVida());
            barraLucario.setProgress(lucario.getVida());

            vidaCharizard.setText(String.format("%.0f%%", charizard.getVida() * 100));
            vidaLucario.setText(String.format("%.0f%%", lucario.getVida() * 100));

            actualizarTextosBotones();
        }
    }

    /**
     * Actualiza los textos de los botones de ataque con el nombre del movimiento
     * y la cantidad de PP restantes. También deshabilita los botones si no hay PP.
     */
    private void actualizarTextosBotones() {
        Movimiento[] movimientos = batalla.getCharizard().getMovimientos();
        botonLlamarada.setText("LLAMARADA (PP: " + movimientos[0].pp + ")");
        botonDragon.setText("GARRA DRAGÓN (PP: " + movimientos[1].pp + ")");
        botonAcrobata.setText("ACROBATA (PP: " + movimientos[2].pp + ")");
        botonLanzallamas.setText("LANZALLAMAS (PP: " + movimientos[3].pp + ")");

        botonLlamarada.setDisable(movimientos[0].pp <= 0);
        botonDragon.setDisable(movimientos[1].pp <= 0);
        botonAcrobata.setDisable(movimientos[2].pp <= 0);
        botonLanzallamas.setDisable(movimientos[3].pp <= 0);
    }

    /**
     * Desactiva todos los botones de ataque, usualmente para impedir interacciones
     * cuando la batalla ha finalizado o está en un estado no interactivo.
     */
    public void desactivarBotones() {
        botonLlamarada.setDisable(true);
        botonDragon.setDisable(true);
        botonAcrobata.setDisable(true);
        botonLanzallamas.setDisable(true);
    }

    /**
     * Obtiene el label que muestra la vida de Charizard.
     * 
     * @return Label con la vida de Charizard.
     */
    public Label getVidaCharizard() {
        return vidaCharizard;
    }

    /**
     * Obtiene el label que muestra la vida de Lucario.
     * 
     * @return Label con la vida de Lucario.
     */
    public Label getVidaLucario() {
        return vidaLucario;
    }

    /**
     * Devuelve un arreglo con los botones de ataque de Charizard para un manejo externo.
     * 
     * @return Array de botones de ataque.
     */
    public Button[] getBotonesCharizard() {
        return new Button[]{botonLlamarada, botonDragon, botonAcrobata, botonLanzallamas};
    }

    /**
     * Método asociado al botón de guardar partida. Guarda el estado actual de la batalla
     * usando la clase AccesoDatos.
     */
    @FXML
    private void guardarPartida() {
        try {
            AccesoDatos gestor = new AccesoDatos();
            gestor.guardarPartida("Guardado manual",
                                  batalla.getTurnoCharizard(),
                                  batalla.getCharizard(),
                                  batalla.getLucario());
            System.out.println("Partida guardada exitosamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga una partida guardada restaurando los estados de los Pokémon y el turno.
     * 
     * @param partida instancia de PartidaGuardada con los datos a cargar.
     */
    public void cargarPartida(PartidaGuardada partida) {
        this.turnoCharizard = partida.turnoCharizard;
        this.charizard = partida.charizard;
        this.lucario = partida.lucario;
    }

}
