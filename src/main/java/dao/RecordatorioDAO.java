package dao;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Recordatorio;

public class RecordatorioDAO {

    private final Connection conexion;

    private static final String SELECT_BASE
            = "SELECT "
            + "rec.id, "
            + "rec.renovacion_id, "
            + "u.nombre AS renovacion, "
            + "rec.dias_antes, "
            + "rec.enviado, "
            + "rec.fecha_envio "
            + "FROM recordatorios rec "
            + "INNER JOIN renovaciones ren "
            + "ON rec.renovacion_id = ren.id "
            + "INNER JOIN usuarios u "
            + "ON ren.usuario_id = u.id ";

    public RecordatorioDAO() {
        conexion = Conexion.getConexion();
    }

    public boolean insertar(Recordatorio recordatorio) {

        String sql = "INSERT INTO recordatorios "
                + "(renovacion_id, dias_antes, enviado, fecha_envio) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, recordatorio.getRenovacion_id());
            ps.setInt(2, recordatorio.getDias_antes());
            ps.setBoolean(3, recordatorio.isEnviado());

            asignarFechaEnvio(ps, 4, recordatorio);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean actualizar(Recordatorio recordatorio) {

        String sql = "UPDATE recordatorios SET "
                + "renovacion_id=?, "
                + "dias_antes=?, "
                + "enviado=?, "
                + "fecha_envio=? "
                + "WHERE id=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, recordatorio.getRenovacion_id());
            ps.setInt(2, recordatorio.getDias_antes());
            ps.setBoolean(3, recordatorio.isEnviado());

            asignarFechaEnvio(ps, 4, recordatorio);

            ps.setInt(5, recordatorio.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean eliminar(int id) {

        String sql = "DELETE FROM recordatorios WHERE id=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Recordatorio obtenerPorId(int id) {

        String sql = SELECT_BASE
                + "WHERE rec.id=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<Recordatorio> listar() {

        List<Recordatorio> lista = new ArrayList<>();

        String sql = SELECT_BASE
                + "ORDER BY rec.id ASC";

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    private Recordatorio mapear(ResultSet rs) throws SQLException {

        Recordatorio recordatorio = new Recordatorio();

        recordatorio.setId(rs.getInt("id"));
        recordatorio.setRenovacion_id(rs.getInt("renovacion_id"));
        recordatorio.setRenovacion(rs.getString("renovacion"));
        recordatorio.setDias_antes(rs.getInt("dias_antes"));
        recordatorio.setEnviado(rs.getBoolean("enviado"));

        Timestamp ts = rs.getTimestamp("fecha_envio");

        if (ts != null) {
            recordatorio.setFecha_envio(ts.toLocalDateTime());
        }

        return recordatorio;
    }

    private void asignarFechaEnvio(PreparedStatement ps,
            int indice,
            Recordatorio recordatorio) throws SQLException {

        if (recordatorio.getFecha_envio() == null) {
            ps.setNull(indice, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(indice,
                    Timestamp.valueOf(recordatorio.getFecha_envio()));
        }
    }

    public Recordatorio obtenerPorRenovacion(int renovacionId) {

        String sql = SELECT_BASE
                + "WHERE rec.renovacion_id = ? "
                + "ORDER BY rec.id ASC "
                + "LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, renovacionId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return mapear(rs);

                }

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return null;

    }

}
