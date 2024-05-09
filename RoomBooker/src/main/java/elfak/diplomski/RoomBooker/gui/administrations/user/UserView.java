package elfak.diplomski.RoomBooker.gui.administrations.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import database.tables.pojos.User;
import elfak.diplomski.RoomBooker.gui.MainView;
import elfak.diplomski.RoomBooker.query.impl.UserQuery;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@Route(value = "user", layout = MainView.class)
@PageTitle("User")
public class UserView extends VerticalLayout {

    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    AddEditUser addEditUser;
    UserQuery userQuery;

    public UserView(UserQuery userQuery) {
        this.userQuery = userQuery;
        setSizeFull();
        configureGird();
        createAddEditUserDialog();
        add(toolBarLayout(), getContent());
        closeAddEditForm();

    }


    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, addEditUser);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, addEditUser);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void createAddEditUserDialog() {
        addEditUser = new AddEditUser();
        addEditUser.setWidth("25em");
        addEditUser.addSaveListener(this::saveNewUser);
        addEditUser.addCloseListener(e -> closeEditor());
        addEditUser.addEditListener(this::saveEditedUser);
    }

    private void saveNewUser(AddEditUser.SaveEvent saveEvent) {
        userQuery.insertUser(saveEvent.getUser());
        refreshUsers();
    }

    private void saveEditedUser(AddEditUser.EditEvent editEvent) {
        userQuery.updateUser(editEvent.getUser());
        refreshUsers();
    }

    private Component toolBarLayout() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addUserBtn = new Button("Add user");
        addUserBtn.addClickListener(click -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserBtn);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        addEditUser.setVisible(true);
        addEditUser.setUser(null);
        addClassName("editing");
    }

    private void configureGird() {
        grid.setSizeFull();
        grid.setColumns("name", "role", "email", "mobileNumber");
        refreshUsers();

        grid.asSingleSelect().addValueChangeListener(event ->
                editUser(event.getValue()));
    }

    private void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            addEditUser.setUser(user);
            addEditUser.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        addEditUser.clearFormFields();
        addEditUser.setVisible(false);
        removeClassName("editing");
    }

    private void refreshUsers() {
        List<User> users = userQuery.getUsers();
        grid.setItems(users);
    }

    private void closeAddEditForm() {
        addEditUser.setUser(null);
        addEditUser.setVisible(false);
        removeClassName("editing");
    }
}

