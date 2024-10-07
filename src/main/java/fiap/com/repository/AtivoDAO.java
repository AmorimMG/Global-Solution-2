package fiap.com.repository;

import fiap.com.model.Ativo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AtivoDAO{
    private static AtivoDAO instance = null;
    private final JdbcHelper jdbcHelper;

    public static AtivoDAO getInstance() {
        if (instance == null) {
            instance = new AtivoDAO();
        }
        return instance;
    }

    private AtivoDAO() {
        jdbcHelper = JdbcHelper.getInstance();
    }

    public boolean criar(Ativo ativo) {
        String sql = "INSERT INTO ATIVO (CODIGO_ATIVO, NOME_ATIVO, VALOR_ATIVO)  VALUES (?, ?, ?)";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, ativo.getCodigoAtivo());
            statement.setString(2, ativo.getNomeAtivo());
            statement.setBigDecimal(3, ativo.getValorAtivo());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar ativo no banco de dados:" + e.getMessage());
            return false;
        }
    }

    public Optional<Ativo> acharPorCodigo(String codigo) {
        String sql = "SELECT CODIGO_ATIVO, NOME_ATIVO, VALOR_ATIVO FROM ATIVO a WHERE a.CODIGO_ATIVO = ?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, codigo);

            ResultSet rs = statement.executeQuery();
            Ativo ativo;
            if (rs.next()) {
                ativo = new Ativo(
                        rs.getString("CODIGO_ATIVO"),
                        rs.getString("NOME_ATIVO"),
                        new BigDecimal(rs.getString("VALOR_ATIVO"))
                );

                return Optional.of(ativo);
            }

            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Erro ao buscar ativo no banco de dados:" + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Ativo> listarAtivos() {
        String sql = "SELECT * FROM ATIVO";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            ResultSet rs = statement.executeQuery();
            List<Ativo> ativos = new ArrayList<>();
            while (rs.next()) {
                ativos.add(new Ativo(
                        rs.getString("CODIGO_ATIVO"),
                        rs.getString("NOME_ATIVO"),
                        new BigDecimal(rs.getString("VALOR_ATIVO"))
                ));
            }

            return ativos;
        } catch (Exception e) {
            System.out.println("Erro ao buscar ativos no banco de dados:" + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean salvar(Ativo ativo) {
        String sql = "UPDATE ATIVO a set a.VALOR_ATIVO=?, a.NOME_ATIVO=? WHERE a.CODIGO_ATIVO=?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setBigDecimal(1, ativo.getValorAtivo());
            statement.setString(2, ativo.getNomeAtivo());
            statement.setString(3, ativo.getCodigoAtivo());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar ativo no banco de dados:" + e.getMessage());
            return false;
        }
    }

    public boolean deletar(Ativo ativo) {
        String sql = "DELETE FROM ATIVO a WHERE a.CODIGO_ATIVO=?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, ativo.getCodigoAtivo());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar ativo no banco de dados:" + e.getMessage());
            return false;
        }
    }
}
