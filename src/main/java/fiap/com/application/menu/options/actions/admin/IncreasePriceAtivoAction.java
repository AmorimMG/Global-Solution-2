package fiap.com.application.menu.options.actions.admin;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.model.Ativo;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class IncreasePriceAtivoAction extends AbstractAction {
    @Override
    protected void action() {
        System.out.println("\nIniciando incremento de preço...");

        Ativo ativo = InputUtil.getAtivo();
        BigDecimal incremento = InputUtil.getBigDecimal("Digite o incremento preço do ativo: ");

        ativo.aumentarValor(incremento);
    }
}
