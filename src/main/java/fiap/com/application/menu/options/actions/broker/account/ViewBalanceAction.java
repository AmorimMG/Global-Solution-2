package fiap.com.application.menu.options.actions.broker.account;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;

public class ViewBalanceAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        System.out.println("\nBuscando balança...");

        ServiceContext context = ServiceContext.getInstance();

        Conta conta = context.getConta().orElseThrow(UnauthorizedException::new);

        System.out.printf("Saldo: R$ %,.2f", conta.getSaldo());

    }
}
