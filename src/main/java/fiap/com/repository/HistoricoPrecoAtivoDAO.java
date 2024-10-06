package fiap.com.repository;

import fiap.com.model.Ativo;
import fiap.com.model.HistoricoPrecoAtivo;
import fiap.com.services.HistoricoPrecoAtivoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoPrecoAtivoDAO {
    private static HistoricoPrecoAtivoDAO instance = null;
    private final JdbcHelper jdbcHelper;
    public static HistoricoPrecoAtivoDAO getInstance() {
        if (instance == null) {
            instance = new HistoricoPrecoAtivoDAO();
        }
        return instance;
    }

    private HistoricoPrecoAtivoDAO() {
        jdbcHelper = JdbcHelper.getInstance();
    }

    public boolean inserir(HistoricoPrecoAtivo historicoPrecoAtivo) {
        String sql = "INSERT INTO HISTORICOPRECOATIVO (CODIGO_ATIVO, DATA_PRECO, VALOR_ATIVO) VALUES (?, ?, ?)";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, historicoPrecoAtivo.codigoAtivo());
            statement.setTimestamp(2, Timestamp.valueOf( historicoPrecoAtivo.dataPreco()));
            statement.setBigDecimal(3, historicoPrecoAtivo.valorAtivo());

            statement.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar histórico de preço no banco de dados:" + e.getMessage());
            return false;
        }
    }

    public List<HistoricoPrecoAtivo> pegarHistorico(Ativo ativo) {
        String sql = "SELECT CODIGO_ATIVO, DATA_PRECO, VALOR_ATIVO FROM HISTORICOPRECOATIVO h WHERE h.CODIGO_ATIVO=? ORDER BY h.DATA_PRECO DESC";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, ativo.getCodigoAtivo());

            ResultSet rs = statement.executeQuery();
            List<HistoricoPrecoAtivo> precos = new ArrayList<>();
            while (rs.next()) {
                HistoricoPrecoAtivo t = new HistoricoPrecoAtivo(
                        rs.getString("CODIGO_ATIVO"),
                        rs.getTimestamp("DATA_PRECO").toLocalDateTime(),
                        rs.getBigDecimal("VALOR_ATIVO")
                );

                precos.add(t);
            }


            return precos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar historico de precos no banco de dados:" + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<HistoricoPrecoAtivo> pegarHistoricoCompleto() {
        String sql = "SELECT CODIGO_ATIVO, DATA_PRECO, VALOR_ATIVO FROM HISTORICOPRECOATIVO h ORDER BY h.DATA_PRECO DESC";
        try (Connection connection = jdbcHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            ResultSet rs = statement.executeQuery();
            List<HistoricoPrecoAtivo> precos = new ArrayList<>();
            while (rs.next()) {
                HistoricoPrecoAtivo t = new HistoricoPrecoAtivo(
                        rs.getString("CODIGO_ATIVO"),
                        rs.getTimestamp("DATA_PRECO").toLocalDateTime(),
                        rs.getBigDecimal("VALOR_ATIVO")
                );

                precos.add(t);
            }


            return precos;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar historico de precos no banco de dados:" + e.getMessage());
            return new ArrayList<>();
        }
    }
}
