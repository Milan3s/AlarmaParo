package dao;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Historial;

public class HistorialDAO {

    private final Connection conexion;

    private static final String SELECT_BASE =
            "SELECT "
            + "h.id, "
            + "h.usuario_id, "
            + "u.nombre AS renovacion, "
            + "h.fecha_renovacion, "
            + "h.fecha_siguiente, "
            + "h.observaciones, "
            + "h.fecha_registro "
            + "FROM historial_renovaciones h "
            + "INNER JOIN usuarios u "
            + "ON h.usuario_id = u.id ";

    public HistorialDAO() {
        conexion = Conexion.getConexion();
    }

    public boolean insertar(Historial historial) {

        String sql = "INSERT INTO historial_renovaciones "
                + "(usuario_id, fecha_renovacion, fecha_siguiente, observaciones, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, historial.getUsuario_id());
            ps.setDate(2, Date.valueOf(historial.getFecha_renovacion()));
            ps.setDate(3, Date.valueOf(historial.getFecha_siguiente()));
            ps.setString(4, historial.getObservaciones());

            asignarFechaRegistro(ps, 5, historial);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;

    }

    public boolean actualizar(Historial historial) {

        String sql = "UPDATE historial_renovaciones SET "
                + "usuario_id=?, "
                + "fecha_renovacion=?, "
                + "fecha_siguiente=?, "
                + "observaciones=?, "
                + "fecha_registro=? "
                + "WHERE id=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, historial.getUsuario_id());
            ps.setDate(2, Date.valueOf(historial.getFecha_renovacion()));
            ps.setDate(3, Date.valueOf(historial.getFecha_siguiente()));
            ps.setString(4, historial.getObservaciones());

            asignarFechaRegistro(ps, 5, historial);

            ps.setInt(6, historial.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;

    }

    public boolean eliminar(int id) {

        String sql = "DELETE FROM historial_renovaciones WHERE id=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;

    }

    public Historial obtenerPorId(int id) {

        String sql = SELECT_BASE
                + "WHERE h.id=?";

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

    public List<Historial> listar() {

        List<Historial> lista = new ArrayList<>();

        String sql = SELECT_BASE
                + "ORDER BY h.id ASC";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;

    }

    private Historial mapear(ResultSet rs) throws SQLException {

        Historial historial = new Historial();

        historial.setId(rs.getInt("id"));
        historial.setUsuario_id(rs.getInt("usuario_id"));
        historial.setRenovacion(rs.getString("renovacion"));
        historial.setFecha_renovacion(rs.getDate("fecha_renovacion").toLocalDate());
        historial.setFecha_siguiente(rs.getDate("fecha_siguiente").toLocalDate());
        historial.setObservaciones(rs.getString("observaciones"));

        Timestamp ts = rs.getTimestamp("fecha_registro");

        if (ts != null) {
            historial.setFecha_registro(ts.toLocalDateTime());
        }

        return historial;

    }

    private void asignarFechaRegistro(PreparedStatement ps,
                                      int indice,
                                      Historial historial) throws SQLException {

        if (historial.getFecha_registro() == null) {
            ps.setNull(indice, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(indice,
                    Timestamp.valueOf(historial.getFecha_registro()));
        }

    }

}