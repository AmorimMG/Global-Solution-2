package fiap.com.application.menu.options.actions.broker.active;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Ativo;
import fiap.com.model.Carteira;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class BuyActiveAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();
        Carteira carteira = context.getCarteira().orElseThrow(UnauthorizedException::new);

        System.out.println("\nIniciando compra de ativo...");

        Ativo ativo = InputUtil.getAtivo();

        BigDecimal value = InputUtil.getBigDecimal("Quanto você quer investir nesse ativo (R$)? ");

        try {
            BigDecimal saldo = carteira.comprar(ativo, value);
            System.out.printf("Seu saldo para a moeda [%s] é de %,.2f moedas", ativo.getCodigoAtivo(), saldo);
        } catch (Exception e) {
            System.out.println("Erro comprando ativo! " + e.getMessage());
        }
    }
}
