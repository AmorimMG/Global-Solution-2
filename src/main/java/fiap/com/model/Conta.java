package fiap.com.model;

import fiap.com.repository.ContaDAO;

import java.math.BigDecimal;
import java.util.Date;

public class Conta {
    private final String cpf;  // Primary Key
    private final String nome;
    private final String email;
    private final Date dataNascimento; // Simples String para teste, sem Date
    private final String login;
    private final String senha;
    private BigDecimal saldo = BigDecimal.ZERO;


    public Conta(String cpf, String nome, String email, Date dataNascimento, String login, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.login = login;
        this.senha = senha;
    }

    public Conta(String cpf, String nome, String email, Date dataNascimento, String login, String senha, BigDecimal saldo) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.login = login;
        this.senha = senha;
        this.saldo = saldo;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public BigDecimal depositar(BigDecimal deposito) {
        if (deposito.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo!");
        }

        saldo = saldo.add(deposito);
        this.salvar();
        return saldo;
    }

    public BigDecimal sacar(BigDecimal saque) {
        if (saque.compareTo(BigDecimal.ZERO) < 0 || saque.compareTo(saldo) > 0) {
            throw new IllegalArgumentException("Valor inválido de saque!");
        }

        saldo = saldo.subtract(saque);
        this.salvar();
        return saldo;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", saldo=" + saldo +
                '}';
    }

    private void salvar() {
        ContaDAO dao = ContaDAO.getInstance();
        dao.salvar(this);
    }
}
