package app.utl.farmacia.Repositories;

import app.utl.farmacia.Entities.Clientes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio de clientes que invoca los stored procedures de MySQL mediante SimpleJdbcCall.
 * Buena práctica: encapsula el acceso a BD y reutiliza un RowMapper para evitar duplicar mapeo.
 */
@Repository
public class ClientesRepository {

    private final JdbcTemplate jdbcTemplate;

    // Convierte cada fila devuelta por el SP en una entidad Clientes
    private final RowMapper<Clientes> clienteRowMapper = (rs, rowNum) -> new Clientes(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("telefono")
    );

    public ClientesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Llama a sp_leer_clientes() y recupera el result set mapeado a lista
    @SuppressWarnings("unchecked")
    public List<Clientes> buscarTodos() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_leer_clientes")
                .returningResultSet("clientes", clienteRowMapper);

        Map<String, Object> resultado = call.execute();
        return (List<Clientes>) resultado.get("clientes");
    }

    // Llama a sp_leer_cliente_por_id(p_id)
    @SuppressWarnings("unchecked")
    public Optional<Clientes> buscarPorId(Long id) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_leer_cliente_por_id")
                .returningResultSet("cliente", clienteRowMapper);

        Map<String, Object> resultado = call.execute(Map.of("p_id", id));
        List<Clientes> lista = (List<Clientes>) resultado.get("cliente");
        return lista == null ? Optional.empty() : lista.stream().findFirst();
    }

    // Llama a sp_crear_cliente(p_nombre, p_telefono)
    public Clientes guardar(Clientes cliente) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_crear_cliente");

        call.execute(Map.of(
                "p_nombre", cliente.getNombre(),
                "p_telefono", cliente.getTelefono()
        ));

        // Recuperamos el último registro creado para devolverlo con su ID generado
        List<Clientes> todos = buscarTodos();
        return todos.isEmpty() ? cliente : todos.get(todos.size() - 1);
    }

    // Llama a sp_actualizar_cliente(p_id, p_nombre, p_telefono)
    public boolean actualizar(Clientes cliente) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_actualizar_cliente");

        call.execute(Map.of(
                "p_id", cliente.getId(),
                "p_nombre", cliente.getNombre(),
                "p_telefono", cliente.getTelefono()
        ));
        return true;
    }

    // Llama a sp_eliminar_cliente(p_id)
    public boolean eliminarPorId(Long id) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_eliminar_cliente");

        call.execute(Map.of("p_id", id));
        return true;
    }
}