package fiap.com.application.menu.options.actions.broker.account;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class WithdrawAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();

        Conta conta = context.getConta().orElseThrow(UnauthorizedException::new);

        BigDecimal sacar = InputUtil.getBigDecimal("\nQuanto você quer sacar? ");

        try {
            BigDecimal saque = conta.sacar(sacar);
            System.out.printf("Saldo após o saque R$ %,.5f\n", saque);
        } catch (Exception e) {
            System.out.println("Erro sacando dinheiro! " + e.getMessage());
        }

    }
}
