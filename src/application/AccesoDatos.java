package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Movimiento;
import modelo.Pokemon;

/**
 * Clase para manejar la conexión a la base de datos y operaciones
 * relacionadas con guardar y cargar partidas de Pokémon.
 */
public class AccesoDatos {

    /** Conexión con la base de datos MySQL */
    private Connection conexion;

    /**
     * Constructor que establece la conexión con la base de datos MySQL.
     * @throws SQLException Si ocurre un error en la conexión
     */
    public AccesoDatos() throws SQLException {
        // Datos de conexión
        String url = "jdbc:mysql://localhost:3306/Pokemon";
        String usuario = "root";
        String contraseña = "4c4e6l7t8A.";

        // Establecer conexión
        this.conexion = DriverManager.getConnection(url, usuario, contraseña);
    }

    /**
     * Guarda una partida en la base de datos con el nombre, turno actual y
     * estado de los Pokémon (Charizard y Lucario).
     * @param nombreGuardado Nombre identificador de la partida
     * @param turnoCharizard Indica si es turno de Charizard
     * @param charizard Objeto Pokemon del jugador
     * @param lucario Objeto Pokemon del rival
     * @throws SQLException Si ocurre un error durante la operación SQL
     */
    public void guardarPartida(String nombreGuardado, boolean turnoCharizard, Pokemon charizard, Pokemon lucario) throws SQLException {
        try {
            conexion.setAutoCommit(false);

            // Insertar registro de partida guardada y obtener su ID generado
            String insertPartida = "INSERT INTO partidas_guardadas (nombre_guardado, turno) VALUES (?, ?)";
            PreparedStatement psPartida = conexion.prepareStatement(insertPartida, Statement.RETURN_GENERATED_KEYS);
            psPartida.setString(1, nombreGuardado);
            psPartida.setBoolean(2, turnoCharizard);
            psPartida.executeUpdate();

            ResultSet rs = psPartida.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("No se pudo obtener el ID de la partida");
            int partidaId = rs.getInt(1);

            // Insertar Pokémon del jugador y rival en la tabla batalla
            insertarPokemon(partidaId, true, charizard);
            insertarPokemon(partidaId, false, lucario);

            conexion.commit();
            System.out.println("¡Partida guardada exitosamente!");
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        }
    }

    /**
     * Inserta un Pokémon y sus movimientos en la base de datos.
     * @param partidaId ID de la partida a la que pertenece el Pokémon
     * @param esJugador Indica si el Pokémon es del jugador (true) o rival (false)
     * @param p Objeto Pokemon a insertar
     * @return ID generado del Pokémon insertado
     * @throws SQLException Si ocurre un error SQL
     */
    private int insertarPokemon(int partidaId, boolean esJugador, Pokemon p) throws SQLException {
        String insertPokemon = "INSERT INTO batalla (partida_id, jugador, nombre_pokemon, vida) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conexion.prepareStatement(insertPokemon, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, partidaId);
        ps.setBoolean(2, esJugador);
        ps.setString(3, p.nombre);
        ps.setDouble(4, p.getVida());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (!rs.next()) throw new SQLException("No se pudo obtener el ID del Pokémon");
        int pokemonId = rs.getInt(1);

        // Insertar movimientos asociados al Pokémon
        for (Movimiento m : p.movimientos) {
            String insertMov = "INSERT INTO movimientos_pokemon (pokemon_id, nombre_movimiento, pp_actual, pp_maximo) VALUES (?, ?, ?, ?)";
            PreparedStatement psMov = conexion.prepareStatement(insertMov);
            psMov.setInt(1, pokemonId);
            psMov.setString(2, m.nombre);
            psMov.setInt(3, m.pp);
            psMov.setInt(4, m.ppMax);
            psMov.executeUpdate();
        }

        return pokemonId;
    }

    /**
     * Clase interna para representar una partida guardada con su estado.
     */
    public class PartidaGuardada {
        public boolean turnoCharizard;
        public Pokemon charizard;
        public Pokemon lucario;

        public PartidaGuardada(boolean turnoCharizard, Pokemon charizard, Pokemon lucario) {
            this.turnoCharizard = turnoCharizard;
            this.charizard = charizard;
            this.lucario = lucario;
        }
    }

    /**
     * Carga la última partida guardada de la base de datos,
     * recuperando el estado de los Pokémon y el turno actual.
     * @return Objeto PartidaGuardada con los datos cargados
     * @throws SQLException Si no se encuentra ninguna partida o error SQL
     */
    public PartidaGuardada cargarUltimaPartida() throws SQLException {
        String query = "SELECT * FROM partidas_guardadas ORDER BY fecha DESC LIMIT 1";
        PreparedStatement ps = conexion.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) throw new SQLException("No se encontró ninguna partida guardada");

        int partidaId = rs.getInt("id");
        boolean turno = rs.getBoolean("turno");

        Pokemon charizard = null;
        Pokemon lucario = null;

        // Consultar los Pokémon relacionados a la partida
        String sqlPokemon = "SELECT * FROM batalla WHERE partida_id = ?";
        PreparedStatement psPoke = conexion.prepareStatement(sqlPokemon);
        psPoke.setInt(1, partidaId);
        ResultSet rsPoke = psPoke.executeQuery();

        while (rsPoke.next()) {
            int pokeId = rsPoke.getInt("id");
            String nombre = rsPoke.getString("nombre_pokemon");
            double vida = rsPoke.getDouble("vida");
            boolean esJugador = rsPoke.getBoolean("jugador");

            // Cargar movimientos asociados al Pokémon
            List<Movimiento> listaMovimientos = new ArrayList<>();
            String sqlMov = "SELECT * FROM movimientos_pokemon WHERE pokemon_id = ?";
            PreparedStatement psMov = conexion.prepareStatement(sqlMov);
            psMov.setInt(1, pokeId);
            ResultSet rsMov = psMov.executeQuery();

            while (rsMov.next()) {
                String nombreMov = rsMov.getString("nombre_movimiento");
                int pp = rsMov.getInt("pp_actual");
                int ppMax = rsMov.getInt("pp_maximo");
                double dano = rsMov.getDouble("dano"); // Asegúrate que la tabla movimientos_pokemon tenga esta columna

                Movimiento m = new Movimiento(nombreMov, dano, ppMax);
                m.setPp(pp);
                listaMovimientos.add(m);
            }

            Movimiento[] movimientosArray = listaMovimientos.toArray(new Movimiento[0]);
            Pokemon p = new Pokemon(nombre, movimientosArray);
            p.setVida(vida);

            if (esJugador) {
                charizard = p;
            } else {
                lucario = p;
            }
        }

        return new PartidaGuardada(turno, charizard, lucario);
    }
}
