package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Inicio;

/**
 *
 * @author Mila
 */
public class InicioDAO {

    /**
     * Obtiene todos los usuarios para el ComboBox.
     */
    public ArrayList<Inicio> obtenerUsuarios() {

        ArrayList<Inicio> lista = new ArrayList<>();

        String sql = "SELECT "
                + "id, "
                + "nombre "
                + "FROM usuarios "
                + "ORDER BY nombre";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Inicio inicio = new Inicio();

                inicio.setUsuarioId(rs.getInt("id"));
                inicio.setNombreUsuario(rs.getString("nombre"));

                lista.add(inicio);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return lista;

    }

    /**
     * Obtiene la renovación actual del usuario.
     */
    public Inicio obtenerInicio(int usuarioId) {

        Inicio inicio = null;

        String sql = "SELECT "
                + "u.id, "
                + "u.nombre, "
                + "r.fecha_renovacion, "
                + "r.fecha_siguiente "
                + "FROM usuarios u "
                + "INNER JOIN renovaciones r "
                + "ON u.id = r.usuario_id "
                + "WHERE u.id = ? "
                + "LIMIT 1";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    inicio = new Inicio();

                    inicio.setUsuarioId(rs.getInt("id"));
                    inicio.setNombreUsuario(rs.getString("nombre"));

                    Date fechaRenovacion = rs.getDate("fecha_renovacion");
                    if (fechaRenovacion != null) {
                        inicio.setFechaRenovacion(fechaRenovacion.toLocalDate());
                    }

                    Date fechaSiguiente = rs.getDate("fecha_siguiente");
                    if (fechaSiguiente != null) {
                        inicio.setFechaSiguiente(fechaSiguiente.toLocalDate());
                    }

                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return inicio;

    }

}
