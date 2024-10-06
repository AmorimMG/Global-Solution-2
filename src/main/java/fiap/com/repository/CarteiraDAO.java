package fiap.com.repository;

import fiap.com.model.Conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CarteiraDAO {
    private static CarteiraDAO instance = null;
    private final JdbcHelper jdbcHelper;

    private CarteiraDAO() {
        jdbcHelper = JdbcHelper.getInstance();
    }

    public static CarteiraDAO getInstance() {
        if (instance == null) {
            instance = new CarteiraDAO();
        }
        return instance;
    }

    public boolean salvar(String cpf, String codigo, BigDecimal quantidade, boolean novo) {
        if (novo) {
            return inserir(cpf, codigo, quantidade);
        } else {
            return atualizar(cpf, codigo, quantidade);
        }
    }

    private boolean atualizar(String cpf, String codigo, BigDecimal quantidade) {
        String sql = "UPDATE CARTEIRA SET QUANTIDADE=? WHERE CPF=? AND CODIGO_ATIVO=?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setBigDecimal(1, quantidade);
            statement.setString(2, cpf);
            statement.setString(3, codigo);

            statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir ativo na carteira no banco de dados:" + e.getMessage());
            return false;
        }
    }

    private boolean inserir(String cpf, String codigo, BigDecimal quantidade) {
        String sql = "INSERT INTO CARTEIRA (CPF, CODIGO_ATIVO, QUANTIDADE) VALUES (?, ?, ?)";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, cpf);
            statement.setString(2, codigo);
            statement.setBigDecimal(3, quantidade);
            
            statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar ativo na carteira no banco de dados:" + e.getMessage());
            return false;
        }
    }

    public Optional<BigDecimal> buscarPorCpfCodigo(String cpf, String codigoAtivo) {
        String sql = "SELECT QUANTIDADE FROM CARTEIRA c WHERE c.CPF=? AND c.CODIGO_ATIVO=?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, cpf);
            statement.setString(2, codigoAtivo);

            ResultSet rs = statement.executeQuery();
            BigDecimal bigDecimal = null;

            if (rs.next()) {
                bigDecimal = rs.getBigDecimal("QUANTIDADE");
                return Optional.of(bigDecimal);
            }

            return Optional.empty();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar ativo na carteira no banco de dados:" + e.getMessage());
            return Optional.empty();
        }
    }

    public Map<String, BigDecimal> buscarCarteira(Conta conta) {
        String sql = "SELECT CODIGO_ATIVO, QUANTIDADE FROM CARTEIRA c WHERE c.CPF=?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, conta.getCpf());

            ResultSet rs = statement.executeQuery();
            BigDecimal bigDecimal = null;

            Map<String, BigDecimal> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("CODIGO_ATIVO"), rs.getBigDecimal("QUANTIDADE"));
            }

            return map;
        } catch (Exception e) {
            System.out.println("Erro ao buscar ativo na carteira no banco de dados:" + e.getMessage());
            return new HashMap<>();
        }
    }
}
