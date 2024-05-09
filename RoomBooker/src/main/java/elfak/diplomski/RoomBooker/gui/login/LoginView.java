package elfak.diplomski.RoomBooker.gui.login;

import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import database.tables.pojos.User;
import elfak.diplomski.RoomBooker.gui.administrations.user.UserView;
import elfak.diplomski.RoomBooker.query.IUserQuery;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    LoginForm loginForm;

    @Autowired
    IUserQuery userQuery;

    public LoginView() {
        configureLoginForm();
        add(loginForm);
        loginForm.setAction("login");
    }

    private void configureLoginForm() {
        loginForm = new LoginForm();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        loginForm.addLoginListener(event -> loginUser(event));
    }

    private void loginUser(AbstractLogin.LoginEvent event) {
        User user = userQuery.loginUser(event.getUsername(), event.getPassword());
        if (user != null) {
            event.getSource().getUI().ifPresent(ui -> ui.navigate(UserView.class));
        }

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
