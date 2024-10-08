package fiap.com.application.menu.options.actions.broker.account;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.services.CarteiraService;

import java.math.BigDecimal;
import java.util.Map;

public class ViewWalletAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();
        Conta conta = context.getConta().orElseThrow(UnauthorizedException::new);

        CarteiraService service = CarteiraService.getInstance();
        Map<String, BigDecimal> carteira = service.buscarCarteira(conta);

        if (carteira.isEmpty()) {
            System.out.println("\nA carteira está vazia!");
        } else {
            System.out.println();
        }

        carteira.forEach((k, v) -> System.out.printf("[%s] - %,.5f Moedas" + "\n", k, v));
    }
}
