package controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import application.AccesoDatos;
import application.AccesoDatos.PartidaGuardada;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Controlador del menú principal de la aplicación.
 * 
 * Gestiona la navegación entre escenas, específicamente para:
 * - Cambiar a la escena del juego/batalla.
 * - Cargar una partida guardada y abrir la escena de batalla con el estado cargado.
 */
public class MenuController {

    // Referencia al Stage principal de la aplicación para poder cambiar escenas.
    private Stage primaryStage;

    // Referencia a la escena de batalla que se puede establecer desde la clase principal.
    private Scene escenaBatalla;

    /**
     * Asigna el Stage principal de la aplicación.
     * 
     * @param stage El Stage principal que se usará para cambiar escenas.
     */
    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Asigna la escena de batalla que puede usarse para cambiar directamente desde el menú.
     * 
     * @param escena Escena de batalla ya creada para mostrar.
     */
    public void setEscenaBatalla(Scene escena) {
        this.escenaBatalla = escena;
    }

    /**
     * Evento para cambiar la escena actual a la escena de batalla.
     * 
     * @param event Evento de acción disparado por la interfaz (por ejemplo, clic en botón).
     */
    @FXML
    private void cambiarAEscenaJuego(ActionEvent event) {
        if (primaryStage != null && escenaBatalla != null) {
            primaryStage.setScene(escenaBatalla);
        }
    }

    /**
     * Evento para cargar la última partida guardada desde la base de datos o almacenamiento,
     * crear una nueva escena de batalla inicializada con dicha partida y mostrarla.
     * 
     * @param event Evento de acción disparado por la interfaz (por ejemplo, clic en botón).
     */
    @FXML
    private void cargarPartida(ActionEvent event) {
        try {
            AccesoDatos gestor = new AccesoDatos();
            // Carga la última partida guardada.
            PartidaGuardada partida = gestor.cargarUltimaPartida();

            // Crea un nuevo controlador de batalla e inicializa con la partida cargada.
            BatallaController controlador = new BatallaController();
            controlador.cargarPartida(partida);

            // Carga el archivo FXML de la escena de batalla y establece el controlador personalizado.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Batalla.fxml"));
            loader.setController(controlador);
            Parent root = loader.load();

            // Crea la escena de batalla con la raíz cargada.
            Scene escena = new Scene(root);

            // Cambia la escena mostrada en el Stage principal.
            primaryStage.setScene(escena);

        } catch (Exception e) {
            e.printStackTrace();
            // Aquí se puede añadir código para mostrar un mensaje de error al usuario.
        }
    }
}
