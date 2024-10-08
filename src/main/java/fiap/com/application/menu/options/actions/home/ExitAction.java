package fiap.com.application.menu.options.actions.home;

import fiap.com.application.menu.options.actions.AbstractAction;

public class ExitAction extends AbstractAction {
    @Override
    protected void action() {
        System.out.println("\nEncerrando a aplicação...");
        System.exit(0);
    }
}
