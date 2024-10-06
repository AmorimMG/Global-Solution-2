package fiap.com.services;

import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.repository.ContaDAO;

import java.util.Date;
import java.util.Optional;

public class ContaService {
    private static ContaService instance = null;
    private final ContaDAO contaDAO;

    public static ContaService getInstance() {
        if (instance == null) {
            instance = new ContaService();
        }

        return instance;
    }

    private ContaService() {
        this.contaDAO = ContaDAO.getInstance();
    }

    public Conta cadastrar(String cpf, String nome, String email, Date dataNascimento, String login, String senha) {
        Conta conta = new Conta(
                cpf, nome, email, dataNascimento, login, senha
        );

        boolean success = contaDAO.criar(conta);
        if (!success) {
            System.out.println("Erro ao criar usuário");
        }

        return conta;
    }

    public Conta autenticar(String login, String senha) throws UnauthorizedException {
        Optional<Conta> opt = contaDAO.buscarPorLogin(login);
        Conta conta = opt.orElseThrow(() -> new UnauthorizedException("Login ou Senha inválidos!"));

        if (!senha.equals(conta.getSenha())) {
            throw new UnauthorizedException("Login ou Senha inválidos!");
        }

        return conta;
    }

    public Optional<Conta> buscarPorCpf(String cpf) {
        return contaDAO.buscarPorCpf(cpf);
    }

    /**
     * Rode esse método para cadastrar um usuário
     * */
    public static void main(String[] args) throws UnauthorizedException {
        String cpf, nome, email, login, senha;
        Date dataNascimento;

        cpf = "12345678910";
        nome = "Andre Chamis";
        email = "andre_chamis@fiap.com";
        login = "user";
        senha = "pass";
        dataNascimento = new Date();

        ContaService u = new ContaService();

        System.out.println("Cadastrando usuário no sistema...");
        u.cadastrar(cpf, nome, email, dataNascimento, login, senha);

        System.out.println("Buscando usuário no sistema...");
        Optional<Conta> res = u.buscarPorCpf(cpf);
        System.out.println("Resultado: " + res);
    }
}
