package fiap.com.application.menu.options.actions.admin;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.model.Ativo;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class DecreasePriceAtivoAction extends AbstractAction {
    @Override
    protected void action() {
        System.out.println("\nIniciando decremento de preço...");
        Ativo ativo = InputUtil.getAtivo();
        BigDecimal decrement = InputUtil.getBigDecimal("Digite o decremento preço do ativo: ");

        ativo.diminuirValor(decrement);
    }
}
