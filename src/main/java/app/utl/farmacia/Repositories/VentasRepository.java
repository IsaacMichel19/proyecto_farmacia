package app.utl.farmacia.Repositories;

import app.utl.farmacia.Entities.Ventas;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repositorio de ventas que invoca los stored procedures de MySQL mediante SimpleJdbcCall.
 */
@Repository
public class VentasRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Ventas> ventaRowMapper = (rs, rowNum) -> {
        Timestamp ts = rs.getTimestamp("fecha_venta");
        return new Ventas(
                rs.getLong("id"),
                rs.getLong("cliente_id"),
                rs.getLong("medicamento_id"),
                rs.getInt("cantidad"),
                rs.getBigDecimal("total"),
                ts != null ? ts.toLocalDateTime() : null
        );
    };

    public VentasRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Llama a sp_leer_ventas()
    @SuppressWarnings("unchecked")
    public List<Ventas> buscarTodas() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_leer_ventas")
                .returningResultSet("ventas", ventaRowMapper);

        Map<String, Object> resultado = call.execute();
        return (List<Ventas>) resultado.get("ventas");
    }

    // Llama a sp_leer_venta_por_id(p_id)
    @SuppressWarnings("unchecked")
    public Optional<Ventas> buscarPorId(Long id) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_leer_venta_por_id")
                .returningResultSet("venta", ventaRowMapper);

        Map<String, Object> resultado = call.execute(Map.of("p_id", id));
        List<Ventas> lista = (List<Ventas>) resultado.get("venta");
        return lista == null ? Optional.empty() : lista.stream().findFirst();
    }

    // Llama a sp_crear_venta(p_cliente_id, p_medicamento_id, p_cantidad)
    public Ventas guardar(Ventas venta) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_crear_venta");

        call.execute(Map.of(
                "p_cliente_id", venta.getClienteId(),
                "p_medicamento_id", venta.getMedicamentoId(),
                "p_cantidad", venta.getCantidad()
        ));

        List<Ventas> todas = buscarTodas();
        return todas.isEmpty() ? venta : todas.get(0); // sp_leer_ventas ordena por fecha_venta DESC
    }
}