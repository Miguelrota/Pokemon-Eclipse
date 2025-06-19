package application;

import controlador.MenuController;
import controlador.BatallaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Batalla;

/**
 * Clase principal de la aplicación Pokémon Eclipse.
 * 
 * Extiende javafx.application.Application para manejar el ciclo de vida
 * de la aplicación JavaFX.
 * 
 * Se encarga de cargar las interfaces (FXML) y controladores para
 * el menú principal y la escena de batalla, y de iniciar la ventana principal.
 */
public class JuegoPokemon extends Application {

    /**
     * Método start llamado al iniciar la aplicación.
     * Carga las vistas y controladores de menú y batalla desde archivos FXML,
     * instancia la lógica de la batalla y los conecta con los controladores,
     * configura las escenas y muestra la ventana principal.
     * 
     * @param primaryStage Escenario principal de la aplicación.
     * @throws Exception Si ocurre algún error durante la carga de los recursos.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Carga la vista y controlador del menú principal desde FXML
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/Vista/Menu.fxml"));
        Parent rootMenu = menuLoader.load();
        MenuController menuController = menuLoader.getController();

        // Carga la vista y controlador de la escena de batalla desde FXML
        FXMLLoader batallaLoader = new FXMLLoader(getClass().getResource("/Vista/Batalla.fxml"));
        Parent rootBatalla = batallaLoader.load();
        BatallaController batallaController = batallaLoader.getController();

        // Crea instancia de la lógica de batalla y la conecta con su controlador
        Batalla batalla = new Batalla();
        batallaController.setBatalla(batalla);
        batalla.setController(batallaController);

        // Crea escenas con las interfaces cargadas
        Scene sceneMenu = new Scene(rootMenu);
        Scene sceneBatalla = new Scene(rootBatalla);

        // Pasa al controlador del menú el Stage principal y la escena de batalla para cambiar entre ellas
        menuController.setStage(primaryStage);
        menuController.setEscenaBatalla(sceneBatalla);

        // Configura el Stage principal
        primaryStage.setTitle("Pokémon Eclipse");
        primaryStage.setScene(sceneMenu);
        primaryStage.setFullScreen(true); // Modo pantalla completa
        primaryStage.setFullScreenExitHint(""); // Oculta mensaje para salir pantalla completa
        primaryStage.show(); // Muestra ventana
    }

    /**
     * Método main que lanza la aplicación JavaFX.
     * @param args argumentos desde línea de comandos (no usados).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
