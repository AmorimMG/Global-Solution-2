package fiap.com.application.menu;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.MenuAction;
import fiap.com.application.menu.options.MenuLevel;
import fiap.com.exception.EnumValueNotFound;
import fiap.com.exception.UnauthorizedException;
import fiap.com.util.InputUtil;

import java.util.List;

public class Menu {
    private MenuLevel menuLevel;
    private List<MenuAction> menuActions;

    public Menu() {
        ServiceContext context = ServiceContext.getInstance();
        context.setMenu(this);

        menuLevel = MenuLevel.HOME;
        try {
            menuActions = MenuAction.getAllByMenuLevel(menuLevel);
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMenuLevel(MenuLevel menuLevel) {
        try {
            menuActions = MenuAction.getAllByMenuLevel(menuLevel);
            this.menuLevel = menuLevel;
        } catch (UnauthorizedException e) {
            System.out.println("Você deve estar autenticado para acessar esse menu!");
        }
    }

    public void execute() {
        while (true) {
            System.out.println("\n================\n");
            showActions();
            MenuAction action = choseAction();
            try {
                action.getAction().execute();
            } catch (UnauthorizedException e) {
                System.out.println("Você deve estar autenticado para fazer isso!");
            }
        }
    }

    private MenuAction choseAction() {
        while (true) {
            int value = InputUtil.getInteger("Escolha uma opção: ");
            try {
                return MenuAction.fromValueAndLevel(value, menuLevel);
            } catch (EnumValueNotFound e) {
                System.out.printf("\nOpção [%d] inválida!\n", value);
            }
        }
    }

    private void showActions() {
        System.out.println("Escolha uma opção abaixo: ");
        ServiceContext context = ServiceContext.getInstance();
        for (MenuAction action : menuActions) {
            if (!action.isShowOnLoggedOff() && !context.isAuthenticated()) {
                continue;
            }

            if (MenuLevel.HOME == menuLevel && action.isShowOnLoggedOff() && context.isAuthenticated() && !MenuAction.EXIT.equals(action)) {
                continue;
            }

            System.out.printf("%d - %s\n", action.getValue(), action.getLabel());
        }
    }
}
