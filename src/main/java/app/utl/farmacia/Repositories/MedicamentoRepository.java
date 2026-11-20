package app.utl.farmacia.Repositories;

import app.utl.farmacia.Entities.Medicamento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio de medicamentos que invoca los stored procedures de MySQL mediante SimpleJdbcCall.
 */
@Repository
public class MedicamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Medicamento> medicamentoRowMapper = (rs, rowNum) -> new Medicamento(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("principio_activo"),
            rs.getBigDecimal("precio"),
            rs.getInt("stock"),
            rs.getBoolean("requiere_receta"),
            rs.getLong("categoria_id")
    );

    public MedicamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Llama a sp_leer_medicamentos()
    @SuppressWarnings("unchecked")
    public List<Medicamento> buscarTodos() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_leer_medicamentos")
                .returningResultSet("medicamentos", medicamentoRowMapper);

        Map<String, Object> resultado = call.execute();
        return (List<Medicamento>) resultado.get("medicamentos");
    }

    // Llama a sp_leer_medicamento_por_id(p_id)
    @SuppressWarnings("unchecked")
    public Optional<Medicamento> buscarPorId(Long id) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_leer_medicamento_por_id")
                .returningResultSet("medicamento", medicamentoRowMapper);

        Map<String, Object> resultado = call.execute(Map.of("p_id", id));
        List<Medicamento> lista = (List<Medicamento>) resultado.get("medicamento");
        return lista == null ? Optional.empty() : lista.stream().findFirst();
    }

    // Llama a sp_crear_medicamento(...)
    public Medicamento guardar(Medicamento medicamento) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_crear_medicamento");

        // Usamos HashMap porque Map.of no permite valores nulos (por si principioActivo viene null)
        Map<String, Object> params = new HashMap<>();
        params.put("p_nombre", medicamento.getNombre());
        params.put("p_principio_activo", medicamento.getPrincipioActivo());
        params.put("p_precio", medicamento.getPrecio());
        params.put("p_stock", medicamento.getStock());
        params.put("p_requiere_receta", medicamento.getRequiereReceta());
        params.put("p_categoria_id", medicamento.getCategoriaId());

        call.execute(params);

        List<Medicamento> todos = buscarTodos();
        return todos.isEmpty() ? medicamento : todos.get(todos.size() - 1);
    }

    // Llama a sp_actualizar_medicamento(...)
    public boolean actualizar(Medicamento medicamento) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_actualizar_medicamento");

        Map<String, Object> params = new HashMap<>();
        params.put("p_id", medicamento.getId());
        params.put("p_nombre", medicamento.getNombre());
        params.put("p_principio_activo", medicamento.getPrincipioActivo());
        params.put("p_precio", medicamento.getPrecio());
        params.put("p_stock", medicamento.getStock());
        params.put("p_requiere_receta", medicamento.getRequiereReceta());
        params.put("p_categoria_id", medicamento.getCategoriaId());

        call.execute(params);
        return true;
    }

    // Actualiza el stock del medicamento reutilizando sp_actualizar_medicamento
    public boolean actualizarStock(Long id, Integer nuevoStock) {
        Optional<Medicamento> actualOpt = buscarPorId(id);
        if (actualOpt.isEmpty()) {
            return false;
        }
        Medicamento actual = actualOpt.get();
        actual.setStock(nuevoStock);
        return actualizar(actual);
    }

    // Llama a sp_eliminar_medicamento(p_id)
    public boolean eliminarPorId(Long id) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_eliminar_medicamento");

        call.execute(Map.of("p_id", id));
        return true;
    }
}