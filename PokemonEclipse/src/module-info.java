module PokemonEclipse {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires java.desktop;
	requires java.sql;
    
    // Exporta el paquete del controlador a javafx.fxml
    exports controlador to javafx.fxml;
    
    // Abre el paquete si usas inyección FXML con campos privados
    opens controlador to javafx.fxml;
    
    // Exporta otros paquetes necesarios
    exports application;
    exports modelo;
}