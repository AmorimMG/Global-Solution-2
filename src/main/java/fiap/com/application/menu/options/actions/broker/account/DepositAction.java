package fiap.com.application.menu.options.actions.broker.account;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class DepositAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();

        Conta conta = context.getConta().orElseThrow(UnauthorizedException::new);

        BigDecimal deposito = InputUtil.getBigDecimal("\nQuanto você quer depositar? ");

        try {
            conta.depositar(deposito);
        } catch (Exception e) {
            System.out.println("Erro depositando! " + e.getMessage());
        }
    }
}
