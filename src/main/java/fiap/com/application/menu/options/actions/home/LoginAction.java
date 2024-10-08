package fiap.com.application.menu.options.actions.home;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.services.ContaService;
import fiap.com.util.InputUtil;

public class LoginAction extends AbstractAction {
    @Override
    protected void action() {
        ServiceContext context = ServiceContext.getInstance();
        ContaService contaService = ContaService.getInstance();

        System.out.println("\nInforme os dados: ");
        String login = InputUtil.getString(" - Login: ");
        String senha = InputUtil.getString(" - Senha: ");

        try {
            Conta conta = contaService.autenticar(login, senha);
            context.login(conta);
        } catch (UnauthorizedException e) {
            System.out.println("Usuário ou senha inválidos!");
        }
    }
}
