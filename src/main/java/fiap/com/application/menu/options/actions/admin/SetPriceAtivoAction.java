package fiap.com.application.menu.options.actions.admin;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.model.Ativo;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class SetPriceAtivoAction extends AbstractAction {
    @Override
    protected void action() {
        System.out.println("\nIniciando alteração de preço...");

        Ativo ativo = InputUtil.getAtivo();
        BigDecimal price = InputUtil.getBigDecimal("Digite o novo preço do ativo: ");

        ativo.setValorAtivo(price);
    }
}
