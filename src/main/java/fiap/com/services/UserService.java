package fiap.com.services;

import fiap.com.model.Conta;

import java.util.Date;
import java.util.Optional;

public class UserService  {
    private static UserService instance = null;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    public Conta cadastrar(String cpf, String nome, String email, Date dataNascimento, String login, String senha) {
        Conta conta = new Conta(
                cpf, nome, email, dataNascimento, login, senha
        );
        return conta;
        // TODO save
    }

    public Optional<Conta> autenticar(String login, String senha) {
        // TODO find from DB, check login and pass
        return Optional.empty();
    }

}
