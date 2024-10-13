package fiap.com.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcHelper {
    private static JdbcHelper instance = null;

    private JdbcHelper() {
    }

    public static JdbcHelper getInstance() {
        if (instance == null) {
            instance = new JdbcHelper();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
        String user = "RM97891";
        String password = "210501";

        Connection connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);

        return connection;
    }

    public void initDb() {
        System.out.println("Inicializando banco de dados...");
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Tabela Ativo
            statement.execute("CREATE TABLE Ativo (codigo_ativo VARCHAR2(10) PRIMARY KEY, nome_ativo VARCHAR2(255) NOT NULL, valor_ativo NUMBER(18, 5) NOT NULL)");
            connection.commit();

            // Tabela Conta
            statement.execute("CREATE TABLE Conta (cpf VARCHAR2(11) PRIMARY KEY, nome VARCHAR2(255) NOT NULL, email VARCHAR2(255) NOT NULL, data_nascimento DATE NOT NULL, login VARCHAR2(50) NOT NULL, senha VARCHAR2(255) NOT NULL, saldo NUMBER(18, 2) DEFAULT 0 CHECK (saldo >= 0))");
            connection.commit();

            // Constraints da tabela Conta
            statement.execute("ALTER TABLE Conta ADD CONSTRAINT unique_login UNIQUE (login)");
            connection.commit();

            // Tabela Carteira (associação Many-to-Many entre Conta e Ativo)
            statement.execute("CREATE TABLE Carteira (cpf VARCHAR2(11), codigo_ativo VARCHAR2(10), quantidade NUMBER(18, 5) NOT NULL, PRIMARY KEY (cpf, codigo_ativo))");
            connection.commit();

            // Constraints da tabela Carteira
            statement.execute("ALTER TABLE Carteira ADD CONSTRAINT fk_carteira_conta FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE");
            statement.execute("ALTER TABLE Carteira ADD CONSTRAINT fk_carteira_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE");
            connection.commit();

            // Tabela HistoricoPrecoAtivo (associação Many-to-One com Ativo)
            statement.execute("CREATE TABLE HistoricoPrecoAtivo (codigo_ativo VARCHAR2(10), data_preco TIMESTAMP, valor_ativo NUMBER(18, 5) NOT NULL, PRIMARY KEY (codigo_ativo, data_preco))");
            connection.commit();

            // Constraints da tabela HistoricoPrecoAtivo
            statement.execute("ALTER TABLE HistoricoPrecoAtivo ADD CONSTRAINT fk_historico_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE");
            connection.commit();

            // Tabela Transacao
            statement.execute("CREATE TABLE Transacao (tipo VARCHAR2(6) CHECK (tipo IN ('COMPRA', 'VENDA')), valor NUMBER(18, 2) NOT NULL, data TIMESTAMP, cpf VARCHAR2(11), codigo_ativo VARCHAR2(10), PRIMARY KEY (cpf, codigo_ativo, data))");
            connection.commit();

            // Constraints da tabela Transacao
            statement.execute("ALTER TABLE Transacao ADD CONSTRAINT fk_transacao_conta FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE");
            statement.execute("ALTER TABLE Transacao ADD CONSTRAINT fk_transacao_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE");
            connection.commit();

            System.out.println("Todas as tabelas e constraints foram criadas com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco de dados: " + e.getMessage());
        }
    }


    public void dropDb() {
        System.out.println("Removendo tabelas do banco de dados...");
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Dropar constraints das tabelas que fazem referência a outras tabelas
            statement.execute("ALTER TABLE Transacao DROP CONSTRAINT fk_transacao_conta");
            statement.execute("ALTER TABLE Transacao DROP CONSTRAINT fk_transacao_ativo");
            connection.commit();

            statement.execute("ALTER TABLE HistoricoPrecoAtivo DROP CONSTRAINT fk_historico_ativo");
            connection.commit();

            statement.execute("ALTER TABLE Carteira DROP CONSTRAINT fk_carteira_conta");
            statement.execute("ALTER TABLE Carteira DROP CONSTRAINT fk_carteira_ativo");
            connection.commit();

            // Dropar as tabelas
            statement.execute("DROP TABLE Transacao");
            connection.commit();

            statement.execute("DROP TABLE HistoricoPrecoAtivo");
            connection.commit();

            statement.execute("DROP TABLE Carteira");
            connection.commit();

            statement.execute("DROP TABLE Conta");
            connection.commit();

            statement.execute("DROP TABLE Ativo");
            connection.commit();

            System.out.println("Todas as tabelas foram removidas com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao remover as tabelas do banco de dados: " + e.getMessage());
        }
    }


    public static void main(String[] args) throws SQLException {
        JdbcHelper jdbc = getInstance();
        jdbc.initDb();
    }

    public void resetDb() {
        dropDb();
        initDb();
    }
}
