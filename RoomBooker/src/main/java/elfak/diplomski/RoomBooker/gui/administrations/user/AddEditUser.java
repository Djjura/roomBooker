package elfak.diplomski.RoomBooker.gui.administrations.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import database.tables.pojos.User;
import elfak.diplomski.RoomBooker.enums.UserRoleEnum;

public class AddEditUser extends FormLayout {
    TextField name = new TextField("Name");
    ComboBox<UserRoleEnum> role = new ComboBox("Role");
    EmailField email = new EmailField("Email");
    TextField mobileNumber = new TextField("Mobile number");
    PasswordField password = new PasswordField("Password");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    boolean isNewUser = true;


    public AddEditUser() {
        add(createFieldsLayout(), createButtonsLayout());
    }

    private Component createFieldsLayout() {
        role.setItems(UserRoleEnum.values());
        name.setRequired(true);
        email.setRequired(true);
        password.setRequired(true);
        return new VerticalLayout(name, role, email, password, mobileNumber);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> saveUser());

        return new HorizontalLayout(save, close);
    }

    private void saveUser() {
        User user = new User();
        user.setName(name.getValue());
        user.setEmail(email.getValue());
        user.setPassword(password.getValue());
        user.setRole(role.getValue().getValue());
        user.setMobileNumber(mobileNumber.getValue());
        if (isNewUser) {
            fireEvent(new SaveEvent(this, user));
        } else {
            fireEvent(new EditEvent(this, user));
        }
    }

    public void setUser(User user) {
        if (user == null) {
            clearFormFields();
            isNewUser = true;
            return;
        }
        isNewUser = false;
        name.setValue(user.getName());
        role.setValue(UserRoleEnum.fromValue(user.getRole()));
        email.setValue(user.getEmail());
        mobileNumber.setValue(user.getMobileNumber());
        password.setValue(user.getPassword());
    }

    public void clearFormFields() {
        name.clear();
        role.clear();
        email.clear();
        mobileNumber.clear();
        password.clear();
    }

    public abstract static class UserFormEvent extends ComponentEvent<AddEditUser> {
        private User user;

        protected UserFormEvent(AddEditUser source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(AddEditUser source, User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(AddEditUser source) {
            super(source, null);
        }
    }

    public static class EditEvent extends UserFormEvent {
        public EditEvent(AddEditUser source, User user) {
            super(source, user);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }
}

