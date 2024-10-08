package fiap.com.application.menu.options.actions.broker.account;

import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.model.Ativo;
import fiap.com.model.HistoricoPrecoAtivo;
import fiap.com.services.HistoricoPrecoAtivoService;
import fiap.com.util.DateUtil;
import fiap.com.util.InputUtil;

import java.util.List;

public class ViewPriceHistoryAction extends AbstractAction {
    @Override
    protected void action() {
        System.out.println("\nBuscando histórico de ativo...");

        Ativo ativo = InputUtil.getAtivo();
        HistoricoPrecoAtivoService service = HistoricoPrecoAtivoService.getInstance();
        List<HistoricoPrecoAtivo> historico = service.buscarHistoricoPorAtivo(ativo);

        historico.forEach(h -> System.out.printf("[%s]: %s - R$ %,.2f\n", DateUtil.formatLocalDateTime(h.dataPreco()), h.codigoAtivo(), h.valorAtivo()));
    }
}
