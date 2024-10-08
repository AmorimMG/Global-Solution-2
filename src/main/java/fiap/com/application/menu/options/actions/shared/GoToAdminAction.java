package fiap.com.application.menu.options.actions.shared;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.Menu;
import fiap.com.application.menu.options.MenuLevel;
import fiap.com.application.menu.options.actions.AbstractAction;

public class GoToAdminAction extends AbstractAction {
    @Override
    protected void action() {
        ServiceContext context = ServiceContext.getInstance();
        Menu menu = context.getMenu();
        menu.setMenuLevel(MenuLevel.ADMIN);
    }
}
