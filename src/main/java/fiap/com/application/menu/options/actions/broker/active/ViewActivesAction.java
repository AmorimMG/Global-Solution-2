package fiap.com.application.menu.options.actions.broker.active;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.model.Ativo;
import fiap.com.services.AtivoService;

import java.util.List;

public class ViewActivesAction extends AbstractAction {
    @Override
    protected void action() {
        AtivoService ativoService = AtivoService.getInstance();
        List<Ativo> ativos = ativoService.listarAtivos();

        System.out.println("\nOs ativos cadastrados nessa corretora são:");

        ativos.forEach(ativo -> System.out.printf(" - %s (%s): R$ %,.2f\n", ativo.getCodigoAtivo(), ativo.getNomeAtivo(), ativo.getValorAtivo()));
    }
}
