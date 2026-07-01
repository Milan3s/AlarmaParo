package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Calendario;
import models.Usuario;

public class CalendarioDAO {

    private final Connection conexion;
    private final UsuarioDAO usuarioDAO;

    public CalendarioDAO() {

        conexion = Conexion.getConexion();
        usuarioDAO = new UsuarioDAO();

    }

    public List<Calendario> listarEventos() {

        List<Calendario> lista = new ArrayList<>();

        String sql =
                "SELECT "
                + "id, "
                + "usuario_id, "
                + "fecha_siguiente, "
                + "estado, "
                + "observaciones "
                + "FROM renovaciones "
                + "ORDER BY fecha_siguiente ASC";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Calendario calendario = new Calendario();

                calendario.setRenovacion_id(rs.getInt("id"));
                calendario.setUsuario_id(rs.getInt("usuario_id"));

                Usuario usuario = usuarioDAO.obtenerPorId(rs.getInt("usuario_id"));

                if (usuario != null) {
                    calendario.setUsuario(usuario.getNombre());
                }

                calendario.setFecha(rs.getDate("fecha_siguiente").toLocalDate());
                calendario.setEstado(rs.getString("estado"));
                calendario.setObservaciones(rs.getString("observaciones"));

                lista.add(calendario);

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return lista;

    }

}