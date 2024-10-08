package fiap.com.application.menu.options.actions.home;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.model.Conta;
import fiap.com.services.ContaService;
import fiap.com.util.InputUtil;

import java.util.Date;

public class RegisterAction extends AbstractAction {
    @Override
    protected void action() {
        ServiceContext context = ServiceContext.getInstance();
        ContaService contaService = ContaService.getInstance();

        System.out.println("\nInforme os dados: ");
        String cpf = InputUtil.getString(" - CPF (Apenas dígitos): ");
        String nome = InputUtil.getString(" - Nome: ");
        String email = InputUtil.getString(" - Email: ");
        String login = InputUtil.getString(" - Login: ");
        String senha = InputUtil.getString(" - Senha: ");

        Date dataNascimento = InputUtil.getDate(" - Data de nascimento (dd/MM/yyyy): ", "dd/MM/yyyy");

        Conta conta = contaService.cadastrar(cpf, nome, email, dataNascimento, login, senha);
        context.login(conta);
    }
}
