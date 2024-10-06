package fiap.com.repository;

import java.sql.*;

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
        String url = "jdbc:h2:./db/h2";
        String user = "sa";
        String password = "";

        return DriverManager.getConnection(url, user, password);
    }

    public void initDb() {
        System.out.println("Inicializando banco de dados...");
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Tabela Ativo
            statement.execute("CREATE TABLE IF NOT EXISTS Ativo (codigo_ativo VARCHAR(10) PRIMARY KEY, nome_ativo VARCHAR(255) NOT NULL, valor_ativo DECIMAL(18, 5) NOT NULL)");
            connection.commit();

            // Tabela Conta
            statement.execute("CREATE TABLE IF NOT EXISTS Conta (cpf VARCHAR(11) PRIMARY KEY, nome VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, data_nascimento DATE NOT NULL, login VARCHAR(50) NOT NULL, senha VARCHAR(255) NOT NULL, saldo DECIMAL(18, 2) DEFAULT 0, CHECK (saldo >= 0))");

            // Constraints da tabela Carteira
            statement.execute("ALTER TABLE Conta ADD CONSTRAINT unique_login UNIQUE (login)");
            connection.commit();

            // Tabela Carteira (associação Many-to-Many entre Conta e Ativo)
            statement.execute("CREATE TABLE IF NOT EXISTS Carteira (cpf VARCHAR(11), codigo_ativo VARCHAR(10), quantidade DECIMAL(18, 5) NOT NULL, PRIMARY KEY (cpf, codigo_ativo))");

            // Constraints da tabela Carteira
            statement.execute("ALTER TABLE Carteira ADD CONSTRAINT fk_carteira_conta FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE");
            statement.execute("ALTER TABLE Carteira ADD CONSTRAINT fk_carteira_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE");
            connection.commit();


            // Tabela HistoricoPrecoAtivo (associação Many-to-One com Ativo)
            statement.execute("CREATE TABLE IF NOT EXISTS HistoricoPrecoAtivo (codigo_ativo VARCHAR(10), data_preco TIMESTAMP, valor_ativo DECIMAL(18, 5) NOT NULL, PRIMARY KEY (codigo_ativo, data_preco))");
            connection.commit();

            // Constraints da tabela HistoricoPrecoAtivo
            statement.execute("ALTER TABLE HistoricoPrecoAtivo ADD CONSTRAINT fk_historico_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE");
            connection.commit();

            // Tabela Transacao
            statement.execute("CREATE TABLE IF NOT EXISTS Transacao (tipo VARCHAR(6) CHECK (tipo IN ('COMPRA', 'VENDA')), valor DECIMAL(18, 2) NOT NULL, data TIMESTAMP, cpf VARCHAR(11), codigo_ativo VARCHAR(10), PRIMARY KEY (cpf, codigo_ativo, data))");
            connection.commit();

            // Constraints da tabela Transacao
            statement.execute("ALTER TABLE Transacao ADD CONSTRAINT fk_transacao_conta FOREIGN KEY (cpf) REFERENCES Conta(cpf) ON DELETE CASCADE");
            statement.execute("ALTER TABLE Transacao ADD CONSTRAINT fk_transacao_ativo FOREIGN KEY (codigo_ativo) REFERENCES Ativo(codigo_ativo) ON DELETE CASCADE");

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco de dados: " + e.getMessage());
        }

        System.out.println("Todas as tabelas e constraints foram criadas com sucesso!");
    }

    public void dropDb() {
        System.out.println("Deletando TUDO do banco de dados");
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            // Desativa integridade referencial
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE;");

            // Deleta todas as tabelas e constraints
            statement.execute("DROP ALL OBJECTS;");

            // Ativa integridade referencial novamente
            statement.execute("SET REFERENTIAL_INTEGRITY TRUE;");

            System.out.println("Todas as tabelas e constraints foram deletadas.");
        } catch (SQLException e) {
            System.out.println("Erro ao operar no banco de dados: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        JdbcHelper jdbc = getInstance();
        jdbc.initDb();
    }

    public void resetDb() {
        dropDb();
        initDb();
    }
}
