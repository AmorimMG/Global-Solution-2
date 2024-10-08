package fiap.com.application.menu.options;

import fiap.com.application.ServiceContext;
import fiap.com.application.menu.options.actions.AbstractAction;
import fiap.com.application.menu.options.actions.admin.*;
import fiap.com.application.menu.options.actions.broker.account.*;
import fiap.com.application.menu.options.actions.broker.active.BuyActiveAction;
import fiap.com.application.menu.options.actions.broker.active.LiquidateActiveAction;
import fiap.com.application.menu.options.actions.broker.active.SellActiveAction;
import fiap.com.application.menu.options.actions.broker.active.ViewActivesAction;
import fiap.com.application.menu.options.actions.home.ExitAction;
import fiap.com.application.menu.options.actions.home.LoginAction;
import fiap.com.application.menu.options.actions.home.LogoutAction;
import fiap.com.application.menu.options.actions.home.RegisterAction;
import fiap.com.application.menu.options.actions.shared.*;
import fiap.com.exception.EnumValueNotFound;
import fiap.com.exception.UnauthorizedException;

import java.util.Arrays;
import java.util.List;

public enum MenuAction {
    EXIT(MenuLevel.HOME, new ExitAction(), 0, "Sair"),
    GO_TO_ADMIN(MenuLevel.HOME, new GoToAdminAction(), 1, "Admin", false),
    GO_TO_BROKER(MenuLevel.HOME, new GoToBrokerAction(), 2, "Corretora", false),
    LOGIN(MenuLevel.HOME, new LoginAction(), 3, "Login"),
    LOGOUT(MenuLevel.HOME, new LogoutAction(), 4, "Logout", false),
    REGISTER(MenuLevel.HOME, new RegisterAction(), 5, "Cadastrar usuário"),

    ADMIN_HOME(MenuLevel.ADMIN, new GoToHomeAction(), 0, "Voltar"),
    REGISTER_ATIVO(MenuLevel.ADMIN, new RegisterAtivoAction(), 1, "Cadastrar ativo"),
    DELETE_ATIVO(MenuLevel.ADMIN, new DeleteAtivoAction(), 2, "Deletar ativo"),
    SET_PRICE_ATIVO(MenuLevel.ADMIN, new SetPriceAtivoAction(), 3, "Definir preço ativo"),
    INCREASE_PRICE_ATIVO(MenuLevel.ADMIN, new IncreasePriceAtivoAction(), 4, "Aumentar preço ativo"),
    DECREASE_PRICE_ATIVO(MenuLevel.ADMIN, new DecreasePriceAtivoAction(), 5, "Diminuir preço ativo"),

    BROKER_HOME(MenuLevel.BROKER, new GoToHomeAction(), 0, "Voltar"),
    BROKER_ACCOUNT(MenuLevel.BROKER, new GoToBrokerAccountAction(), 1, "Gerenciamento de conta"),
    BROKER_ACTIVE(MenuLevel.BROKER, new GoToBrokerActiveAction(), 2, "Gerenciamento de ativos"),

    ACCOUNT_BROKER(MenuLevel.BROKER_ACCOUNT, new GoToBrokerAction(), 0, "Voltar"),
    DEPOSIT(MenuLevel.BROKER_ACCOUNT, new DepositAction(), 1, "Depositar"),
    WITHDRAW(MenuLevel.BROKER_ACCOUNT, new WithdrawAction(), 2, "Sacar"),
    VIEW_WALLET(MenuLevel.BROKER_ACCOUNT, new ViewWalletAction(), 3, "Visualizar carteira"),
    VIEW_PRICE_HISTORY(MenuLevel.BROKER_ACCOUNT, new ViewPriceHistoryAction(), 4, "Visualizar histórico de preços"),
    VIEW_TRANSACTION_HISTORY(MenuLevel.BROKER_ACCOUNT, new ViewTransactionHistoryAction(), 5, "Visualizar histórico de transações"),
    VIEW_BALANCE(MenuLevel.BROKER_ACCOUNT, new ViewBalanceAction(), 6, "Visualizar saldo"),

    ACTIVE_BROKER(MenuLevel.BROKER_ACTIVE, new GoToBrokerAction(), 0, "Voltar"),
    VIEW_ACTIVES(MenuLevel.BROKER_ACTIVE, new ViewActivesAction(), 1, "Visualizar ativos"),
    BUY_ACTIVE(MenuLevel.BROKER_ACTIVE, new BuyActiveAction(), 2, "Comprar ativo"),
    SELL_ACTIVE(MenuLevel.BROKER_ACTIVE, new SellActiveAction(), 3, "Vender ativo"),
    LIQUIDATE_ACTIVE(MenuLevel.BROKER_ACTIVE, new LiquidateActiveAction(), 4, "Liquidar ativo"),
    ;

    private final MenuLevel menuLevel;
    private final AbstractAction action;
    private final int value;
    private final String label;
    private final boolean showOnLoggedOff;

    MenuAction(MenuLevel menuLevel, AbstractAction action, int value, String label) {
        this.menuLevel = menuLevel;
        this.action = action;
        this.value = value;
        this.label = label;
        this.showOnLoggedOff = true;
    }

    MenuAction(MenuLevel menuLevel, AbstractAction action, int value, String label, boolean showOnLoggedOff) {
        this.menuLevel = menuLevel;
        this.action = action;
        this.value = value;
        this.label = label;
        this.showOnLoggedOff = showOnLoggedOff;
    }

    public static List<MenuAction> getAllByMenuLevel(MenuLevel menuLevel) throws UnauthorizedException {
        ServiceContext context = ServiceContext.getInstance();
        if (menuLevel.isAuthOnly() && !context.isAuthenticated()) {
            throw new UnauthorizedException();
        }

        return Arrays.stream(MenuAction.values()).filter(level -> level.menuLevel.equals(menuLevel)).toList();
    }

    public static MenuAction fromValueAndLevel(int value, MenuLevel menuLevel) throws EnumValueNotFound {
        for (MenuAction act : MenuAction.values()) {
            if (value == act.value && act.menuLevel.equals(menuLevel)) {
                return act;
            }
        }

        throw new EnumValueNotFound();
    }

    public AbstractAction getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public boolean isShowOnLoggedOff() {
        return showOnLoggedOff;
    }
}
