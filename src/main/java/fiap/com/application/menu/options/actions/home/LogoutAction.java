package fiap.com.application.menu.options.actions.home;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;

public class LogoutAction extends AbstractAction {
    @Override
    protected void action() {
        ServiceContext context = ServiceContext.getInstance();

        context.logout();

        System.out.println("\nUsuário saiu com sucesso");
    }
}
