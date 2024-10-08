package fiap.com.application.menu.options.actions.broker.active;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Ativo;
import fiap.com.model.Carteira;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class LiquidateActiveAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();
        Carteira carteira = context.getCarteira().orElseThrow(UnauthorizedException::new);

        System.out.println("\nIniciando liquidação de ativo...");

        Ativo ativo = InputUtil.getAtivo();

        try {
            BigDecimal value = carteira.liquidar(ativo);
            System.out.printf("Ativo vendido por R$ %,.2f\n", value);
        } catch (Exception e) {
            System.out.println("Erro liquidando ativo! " + e.getMessage());
        }
    }
}
