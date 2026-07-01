package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.Usuario;

public class UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAO() {

        conexion = Conexion.getConexion();

    }

    public List<Usuario> listar() {

        List<Usuario> lista = new ArrayList<>();

        String sql
                = "SELECT * "
                + "FROM usuarios "
                + "ORDER BY nombre ASC";

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setDni(rs.getString("dni"));
                usuario.setEmail(rs.getString("email"));

                Timestamp ts = rs.getTimestamp("fecha_alta");

                if (ts != null) {

                    usuario.setFecha_alta(ts.toLocalDateTime());

                }

                lista.add(usuario);

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return lista;

    }

    public Usuario obtenerPorId(int id) {

        String sql
                = "SELECT * "
                + "FROM usuarios "
                + "WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setDni(rs.getString("dni"));
                usuario.setEmail(rs.getString("email"));

                Timestamp ts = rs.getTimestamp("fecha_alta");

                if (ts != null) {

                    usuario.setFecha_alta(ts.toLocalDateTime());

                }

                return usuario;

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return null;

    }

}
