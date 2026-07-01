package dao;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Renovacion;

public class RenovacionDAO {

    private final Connection conexion;

    public RenovacionDAO() {
        conexion = Conexion.getConexion();
    }

    // 1. INSERTAR
    public int insertar(Renovacion renovacion) {
        String sql = "INSERT INTO renovaciones "
                + "(usuario_id, fecha_renovacion, fecha_siguiente, estado, observaciones) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, renovacion.getUsuario_id());
            ps.setDate(2, Date.valueOf(renovacion.getFecha_renovacion()));
            ps.setDate(3, Date.valueOf(renovacion.getFecha_siguiente()));
            ps.setString(4, renovacion.getEstado());
            ps.setString(5, renovacion.getObservaciones());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    // 2. ACTUALIZAR
    public boolean actualizar(Renovacion renovacion) {
        String sql = "UPDATE renovaciones SET "
                + "usuario_id=?, "
                + "fecha_renovacion=?, "
                + "fecha_siguiente=?, "
                + "estado=?, "
                + "observaciones=? "
                + "WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, renovacion.getUsuario_id());
            ps.setDate(2, Date.valueOf(renovacion.getFecha_renovacion()));
            ps.setDate(3, Date.valueOf(renovacion.getFecha_siguiente()));
            ps.setString(4, renovacion.getEstado());
            ps.setString(5, renovacion.getObservaciones());
            ps.setInt(6, renovacion.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // 3. ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM renovaciones WHERE id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // 4. OBTENER POR ID
    public Renovacion obtenerPorId(int id) {
        String sql = "SELECT r.*, u.nombre AS usuario "
                + "FROM renovaciones r "
                + "INNER JOIN usuarios u ON u.id = r.usuario_id "
                + "WHERE r.id=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Renovacion renovacion = new Renovacion();
                renovacion.setId(rs.getInt("id"));
                renovacion.setUsuario_id(rs.getInt("usuario_id"));
                renovacion.setUsuario(rs.getString("usuario"));
                renovacion.setFecha_renovacion(rs.getDate("fecha_renovacion").toLocalDate());
                renovacion.setFecha_siguiente(rs.getDate("fecha_siguiente").toLocalDate());
                renovacion.setEstado(rs.getString("estado"));
                renovacion.setObservaciones(rs.getString("observaciones"));
                Timestamp ts = rs.getTimestamp("fecha_creacion");
                if (ts != null) {
                    renovacion.setFecha_creacion(ts.toLocalDateTime());
                }
                return renovacion;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 5. LISTAR TODOS
    public List<Renovacion> listar() {
        List<Renovacion> lista = new ArrayList<>();
        String sql = "SELECT r.*, u.nombre AS usuario "
                + "FROM renovaciones r "
                + "INNER JOIN usuarios u ON u.id = r.usuario_id "
                + "ORDER BY r.fecha_siguiente DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Renovacion renovacion = new Renovacion();
                renovacion.setId(rs.getInt("id"));
                renovacion.setUsuario_id(rs.getInt("usuario_id"));
                renovacion.setUsuario(rs.getString("usuario"));
                renovacion.setFecha_renovacion(rs.getDate("fecha_renovacion").toLocalDate());
                renovacion.setFecha_siguiente(rs.getDate("fecha_siguiente").toLocalDate());
                renovacion.setEstado(rs.getString("estado"));
                renovacion.setObservaciones(rs.getString("observaciones"));
                Timestamp ts = rs.getTimestamp("fecha_creacion");
                if (ts != null) {
                    renovacion.setFecha_creacion(ts.toLocalDateTime());
                }
                lista.add(renovacion);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // 6. OBTENER POR USUARIO
    public Renovacion obtenerPorUsuario(int usuarioId) {
        String sql = "SELECT r.*, u.nombre AS usuario "
                + "FROM renovaciones r "
                + "INNER JOIN usuarios u ON u.id = r.usuario_id "
                + "WHERE r.usuario_id = ? "
                + "ORDER BY r.fecha_siguiente ASC "
                + "LIMIT 1";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Renovacion renovacion = new Renovacion();
                renovacion.setId(rs.getInt("id"));
                renovacion.setUsuario_id(rs.getInt("usuario_id"));
                renovacion.setUsuario(rs.getString("usuario"));
                renovacion.setFecha_renovacion(rs.getDate("fecha_renovacion").toLocalDate());
                renovacion.setFecha_siguiente(rs.getDate("fecha_siguiente").toLocalDate());
                renovacion.setEstado(rs.getString("estado"));
                renovacion.setObservaciones(rs.getString("observaciones"));
                Timestamp ts = rs.getTimestamp("fecha_creacion");
                if (ts != null) {
                    renovacion.setFecha_creacion(ts.toLocalDateTime());
                }
                return renovacion;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
