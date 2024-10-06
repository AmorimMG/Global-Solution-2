package fiap.com.repository;

import fiap.com.model.Conta;
import fiap.com.model.Transacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {
    private static TransacaoDAO instance = null;
    private final JdbcHelper jdbcHelper;
    public static TransacaoDAO getInstance() {
        if (instance == null) {
            instance = new TransacaoDAO();
        }
        return instance;
    }

    private TransacaoDAO() {
        jdbcHelper = JdbcHelper.getInstance();
    }

    public boolean inserir(Transacao transacao) {
        String sql = "INSERT INTO TRANSACAO (TIPO, VALOR, DATA, CPF, CODIGO_ATIVO) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, transacao.tipo().name());
            statement.setBigDecimal(2, transacao.valor());
            statement.setTimestamp(3, Timestamp.valueOf(transacao.data()));
            statement.setString(4, transacao.cpf());
            statement.setString(5, transacao.cdAtivo());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar transacao no banco de dados:" + e.getMessage());
            return false;
        }
    }

    public List<Transacao> pegarHistorico(Conta conta) {
        String sql = "SELECT TIPO, VALOR, DATA, CPF, CODIGO_ATIVO FROM TRANSACAO t WHERE t.CPF=? ORDER BY t.DATA DESC";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, conta.getCpf());

            ResultSet rs = statement.executeQuery();
            List<Transacao> transacoes = new ArrayList<>();
            while (rs.next()) {
                Transacao t = new Transacao(
                        Transacao.TipoTransacao.valueOf(rs.getString("TIPO")),
                        rs.getBigDecimal("VALOR"),
                        rs.getTimestamp("DATA").toLocalDateTime(),
                        rs.getString("CPF"),
                        rs.getString("CODIGO_ATIVO")
                );

                transacoes.add(t);
            }


            return transacoes;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar transacao no banco de dados:" + e.getMessage());
            return new ArrayList<>();
        }
    }
}
