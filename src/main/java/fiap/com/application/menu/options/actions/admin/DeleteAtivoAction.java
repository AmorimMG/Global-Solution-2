package fiap.com.application.menu.options.actions.admin;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.services.AtivoService;
import fiap.com.util.InputUtil;

public class DeleteAtivoAction extends AbstractAction {
    @Override
    protected void action() {
        AtivoService ativoService = AtivoService.getInstance();
        var ativos = ativoService.listarAtivos();

        ativos.forEach(ativo -> System.out.printf(" - %s (%s): R$ %,.2f\n", ativo.getCodigoAtivo(), ativo.getNomeAtivo(), ativo.getValorAtivo()));

        String codigo = InputUtil.getString("\nDigite o código do ativo para ser deletado: ");

        ativoService.deletarAtivo(codigo);

        System.out.println("Ativo deletado com sucesso");
    }
}
