package br.com.mindshub.identity.domain;

import br.com.mindshub.identity.application.exception.InvalidUserRoleException;
import br.com.mindshub.identity.application.exception.PasswordAlreadyInUseException;
import br.com.mindshub.identity.application.exception.UserAlreadyAdminException;
import br.com.mindshub.identity.application.exception.UserAlreadyTeacherException;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.exception.UserAlreadyActivatedException;
import br.com.mindshub.identity.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldPromoteStudentToTeacher() {
        User user = new User();
        user.setRole(Role.STUDENT);

        user.promoteToTeacher();
        assertEquals(Role.TEACHER, user.getRole());
    }

    @Test
    void shouldThrowWhenPromotingTeacherToTeacher() {
        User user = new User();
        user.setRole(Role.TEACHER);

        assertThrows(UserAlreadyTeacherException.class, user::promoteToTeacher);
    }

    @Test
    void shouldThrowWhenNonStudentIsPromotedToTeacher() {
        User user = new User();
        user.setRole(Role.ADMIN);

        assertThrows(InvalidUserRoleException.class, user::promoteToTeacher);
    }

    @Test
    void shouldPromoteTeacherToAdmin() {
        User user = new User();
        user.setRole(Role.TEACHER);

        user.promoteToAdmin();
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void shouldThrowWhenPromotingAdminIsAdmin() {
        User user = new User();
        user.setRole(Role.ADMIN);

        assertThrows(UserAlreadyAdminException.class, user::promoteToAdmin);
    }

    @Test
    void shouldThrowWhenNonTeacherIsPromotedToAdmin() {
        User user = new User();

        assertThrows(InvalidUserRoleException.class, user::promoteToAdmin);
    }

    @Test
    void shouldReturnTrueWhenAccountIsActive() {
        User user = new User();

        user.activate();
        assertTrue(user.isActive(), "User is active");
    }

    @Test
    void shouldThrowWhenAccountIsAlreadyActive() {
        User user = new User();

        user.activate();
        assertThrows(UserAlreadyActivatedException.class, user::activate);
    }

    @Test
    void shouldUpdatePassword() {
        User user= new User();
        user.setPassword("Password@123");

        user.updatePassword("Pedrotomaz@132");
        assertEquals(("Pedrotomaz@132"), user.getPassword());
    }

    @Test
    void shouldThrowWhenNewPasswordIsCurrentPassword() {
        User user = new User();
        user.setPassword("Password@123");

        assertThrows(PasswordAlreadyInUseException.class, () -> {user.updatePassword("Password@123");
        });
    }

    @Test
    void shouldSoftDeleteUser() {
        User user = new User();

        user.delete();
        assertNotNull(user.getDeletedAt());
    }

    @Test
    void shouldReturnFalseWhenUserIsNotDeleted() {
        User user = new User();

        assertFalse(user.isDeleted());
    }

    @Test
    void shouldReturnTrueWhenUserIsDeleted() {
        User user = new User();

        user.delete();

        assertTrue(user.isDeleted());
    }
}
