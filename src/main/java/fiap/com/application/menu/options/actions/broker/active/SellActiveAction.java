package fiap.com.application.menu.options.actions.broker.active;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.application.menu.options.actions.broker.account.ViewWalletAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Ativo;
import fiap.com.model.Carteira;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;
import java.util.Optional;

public class SellActiveAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();
        Carteira carteira = context.getCarteira().orElseThrow(UnauthorizedException::new);

        System.out.println("\nIniciando venda de ativo...");

        Ativo ativo = InputUtil.getAtivo();

        Optional<BigDecimal> opt = carteira.getAtivo(ativo);

        if (opt.isEmpty()) {
            System.out.println("Você não possui moedas desse ativo! Os ativos que você possui são:");
            AbstractAction action = new ViewWalletAction();
            action.execute();
            return;
        } else {
            BigDecimal quantidade = opt.get();
            System.out.printf("Você possui %,.2f moedas desse ativo\n", quantidade);
        }

        BigDecimal value = InputUtil.getBigDecimal("Quanto você quer vender desse ativo (Moedas)? ");

        try {
            BigDecimal venda = carteira.vender(ativo, value);
            System.out.printf("Você lucrou R$ %,.2f com a venda", venda);
        } catch (Exception e) {
            System.out.println("Erro vendendo ativo! " + e.getMessage());
        }
    }
}
