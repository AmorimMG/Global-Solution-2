package fiap.com.repository;

import fiap.com.model.Conta;
import fiap.com.util.DateUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ContaDAO {
    private static ContaDAO instance = null;
    private final JdbcHelper jdbcHelper;

    public static ContaDAO getInstance() {
        if (instance == null) {
            instance = new ContaDAO();
        }
        return instance;
    }

    private ContaDAO() {
        this.jdbcHelper = JdbcHelper.getInstance();
    }

    public boolean criar(Conta conta) {
        String sql = "INSERT INTO Conta (CPF, NOME, EMAIL, DATA_NASCIMENTO, LOGIN, SENHA) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, conta.getCpf());
            statement.setString(2, conta.getNome());
            statement.setString(3, conta.getEmail());
            statement.setDate(4, DateUtil.sqlDateFromJavaDate(conta.getDataNascimento()));
            statement.setString(5, conta.getLogin());
            statement.setString(6, conta.getSenha());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar conta no banco de dados:" + e.getMessage());
            return false;
        }
    }

    public Optional<Conta> buscarPorCpf(String cpf) {
        String sql = "SELECT CPF, NOME, EMAIL, DATA_NASCIMENTO, LOGIN, SENHA, SALDO FROM CONTA c WHERE c.CPF = ?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, cpf);

            ResultSet rs = statement.executeQuery();
            Conta conta;
            if (rs.next()) {
                conta = new Conta(
                        rs.getString("CPF"),
                        rs.getString("NOME"),
                        rs.getString("EMAIL"),
                        rs.getDate("DATA_NASCIMENTO"),
                        rs.getString("LOGIN"),
                        rs.getString("SENHA"),
                        new BigDecimal(rs.getString("SALDO"))
                );

                return Optional.of(conta);
            }

            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Erro ao buscar conta no banco de dados:" + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Conta> buscarPorLogin(String login) {
        String sql = "SELECT CPF, NOME, EMAIL, DATA_NASCIMENTO, LOGIN, SENHA, SALDO FROM CONTA c WHERE c.LOGIN = ?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, login);

            ResultSet rs = statement.executeQuery();
            Conta conta = null;
            while (rs.next()) {
                conta = new Conta(
                        rs.getString("CPF"),
                        rs.getString("NOME"),
                        rs.getString("EMAIL"),
                        rs.getDate("DATA_NASCIMENTO"),
                        rs.getString("LOGIN"),
                        rs.getString("SENHA"),
                        new BigDecimal(rs.getString("SALDO"))
                );
            }

            return Optional.ofNullable(conta);
        } catch (Exception e) {
            System.out.println("Erro ao buscar conta no banco de dados:" + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean salvar(Conta conta) {
        String sql = "UPDATE CONTA c set c.SALDO=?, c.LOGIN=?, c.SENHA=?, c.NOME=?, c.DATA_NASCIMENTO=?, c.EMAIL=? WHERE c.CPF=?";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setBigDecimal(1, conta.getSaldo());
            statement.setString(2, conta.getLogin());
            statement.setString(3, conta.getSenha());
            statement.setString(4, conta.getNome());
            statement.setDate(5, DateUtil.sqlDateFromJavaDate(conta.getDataNascimento()));
            statement.setString(6, conta.getEmail());
            statement.setString(7, conta.getCpf());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar conta no banco de dados:" + e.getMessage());
            return false;
        }
    }
}
