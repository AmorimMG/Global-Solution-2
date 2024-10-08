package fiap.com.application.menu.options.actions.admin;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.services.AtivoService;
import fiap.com.util.InputUtil;

import java.math.BigDecimal;

public class RegisterAtivoAction extends AbstractAction {
    @Override
    protected void action() {
        System.out.println("\nIniciando cadastro de ativo...");
        String codigo = InputUtil.getString("Digite o código do ativo: ");
        String nome = InputUtil.getString("Digite o nome do ativo: ");
        BigDecimal preco = InputUtil.getBigDecimal("Digite o preço do ativo: ");

        AtivoService ativoService = AtivoService.getInstance();
        try {
            ativoService.criar(codigo, nome, preco);
            System.out.println("\nAtivo cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar ativo...");
        }
    }
}
