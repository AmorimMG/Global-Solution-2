package fiap.com.application.menu.options.actions.broker.account;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.exception.UnauthorizedException;
import fiap.com.model.Conta;
import fiap.com.model.Transacao;
import fiap.com.services.TransacaoService;
import fiap.com.util.DateUtil;

import java.util.List;

public class ViewTransactionHistoryAction extends AbstractAction {
    @Override
    protected void action() throws UnauthorizedException {
        System.out.println("\nIniciando histórico de transações...");

        ServiceContext context = ServiceContext.getInstance();
        Conta conta = context.getConta().orElseThrow(UnauthorizedException::new);

        TransacaoService service = TransacaoService.getInstance();
        List<Transacao> historico = service.buscarHistoricoPorConta(conta);

        historico.forEach(transacao -> System.out.printf("[%s]: [%S] %s - R$ %,.2f\n",
                DateUtil.formatLocalDateTime(transacao.data()),
                transacao.tipo().name(),
                transacao.cdAtivo(),
                transacao.valor()
        ));
    }
}
