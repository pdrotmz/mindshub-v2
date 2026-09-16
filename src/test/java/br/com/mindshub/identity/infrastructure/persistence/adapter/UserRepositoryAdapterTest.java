package br.com.mindshub.identity.infrastructure.persistence.adapter;

import br.com.mindshub.identity.infrastructure.persistence.PersistenceTestSupport;
import br.com.mindshub.identity.infrastructure.persistence.entity.UserEntity;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryAdapterTest extends PersistenceTestSupport {
    @ParameterizedTest
    @EnumSource(Role.class)
    void shouldPersistUserWithGeneratedIdentityAndTimestamps(Role role) {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        User saved = createUser("pedro", role, true);
        flushAndClear();
        User found = users.findByUuid(saved.getUuid()).orElseThrow();
        assertThat(found.getId()).isPositive();
        assertThat(found.getUuid()).isEqualTo(saved.getUuid());
        assertThat(found.getUsername()).isEqualTo("pedro");
        assertThat(found.getEmail()).isEqualTo("pedro@example.com");
        assertThat(found.getPassword()).isEqualTo("encoded-password");
        assertThat(found.getRole()).isEqualTo(role);
        assertThat(found.isActive()).isTrue();
        assertThat(found.getDeletedAt()).isNull();
        assertThat(found.getCreatedAt()).isBetween(before, LocalDateTime.now());
        assertThat(found.getUpdatedAt()).isEqualTo(found.getCreatedAt());
        assertThat(users.findByEmail(found.getEmail()).orElseThrow().getUuid()).isEqualTo(saved.getUuid());
    }

    @Test
    void shouldUpdateExistingUserWithoutChangingIdentityOrCreationTime() {
        User saved = createUser("pedro", Role.STUDENT, false);
        flushAndClear();
        User before = users.findByUuid(saved.getUuid()).orElseThrow();
        before.setUsername("updated");
        before.setEmail("updated@example.com");
        before.setPassword("new-encoded-password");
        before.setRole(Role.TEACHER);
        before.setActive(true);
        before.setUpdatedAt(LocalDateTime.of(2000, 1, 1, 0, 0));
        users.save(before);
        flushAndClear();
        User found = users.findByUuid(saved.getUuid()).orElseThrow();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getCreatedAt()).isEqualTo(before.getCreatedAt());
        assertThat(found.getUpdatedAt()).isAfter(before.getUpdatedAt());
        assertThat(found.getUsername()).isEqualTo("updated");
        assertThat(found.getEmail()).isEqualTo("updated@example.com");
        assertThat(found.getPassword()).isEqualTo("new-encoded-password");
        assertThat(found.getRole()).isEqualTo(Role.TEACHER);
        assertThat(found.isActive()).isTrue();
        assertThat(users.findByEmail("pedro@example.com")).isEmpty();
        assertThat(users.findAll()).hasSize(1);
    }

    @Test
    void shouldFilterByRoleAndAccountStatus() {
        User activeStudent = createUser("student", Role.STUDENT, true);
        User inactiveTeacher = createUser("teacher", Role.TEACHER, false);
        flushAndClear();
        assertThat(users.findAll()).extracting(User::getUuid)
                .containsExactlyInAnyOrder(activeStudent.getUuid(), inactiveTeacher.getUuid());
        assertThat(users.findByRole(Role.STUDENT)).extracting(User::getUuid).containsExactly(activeStudent.getUuid());
        assertThat(users.findByRole(Role.TEACHER)).extracting(User::getUuid).containsExactly(inactiveTeacher.getUuid());
        assertThat(users.findByRole(Role.ADMIN)).isEmpty();
        assertThat(users.findByActive(true)).extracting(User::getUuid).containsExactly(activeStudent.getUuid());
        assertThat(users.findByActive(false)).extracting(User::getUuid).containsExactly(inactiveTeacher.getUuid());
    }

    @Test
    void shouldExcludeSoftDeletedUsersFromEveryLookup() {
        User deleted = createUser("deleted", Role.STUDENT, true);
        User retained = createUser("retained", Role.STUDENT, true);
        deleted.delete();
        users.save(deleted);
        flushAndClear();
        assertThat(users.findByUuid(deleted.getUuid())).isEmpty();
        assertThat(users.findByEmail(deleted.getEmail())).isEmpty();
        assertThat(users.existsByEmail(deleted.getEmail())).isFalse();
        assertThat(users.existsByUsername(deleted.getUsername())).isFalse();
        assertThat(users.findAll()).extracting(User::getUuid).containsExactly(retained.getUuid());
        assertThat(users.findByRole(Role.STUDENT)).extracting(User::getUuid).containsExactly(retained.getUuid());
        assertThat(users.findByActive(true)).extracting(User::getUuid).containsExactly(retained.getUuid());
        UserEntity stored = entityManager.find(UserEntity.class, deleted.getId());
        assertThat(stored).isNotNull();
        assertThat(stored.getDeletedAt()).isNotNull();
    }

    @Test
    void shouldReportOnlyExistingUsernameAndEmail() {
        createUser("pedro", Role.STUDENT, false);
        flushAndClear();
        assertThat(users.existsByUsername("pedro")).isTrue();
        assertThat(users.existsByEmail("pedro@example.com")).isTrue();
        assertThat(users.existsByUsername("missing")).isFalse();
        assertThat(users.existsByEmail("missing@example.com")).isFalse();
        assertThat(users.findByUuid(UUID.randomUUID())).isEmpty();
        assertThat(users.findByEmail("missing@example.com")).isEmpty();
    }
}
