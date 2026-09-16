package br.com.mindshub.identity.infrastructure.persistence;

import br.com.mindshub.TestcontainersConfiguration;
import br.com.mindshub.identity.domain.model.User;
import br.com.mindshub.identity.domain.enums.Role;
import br.com.mindshub.identity.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
public abstract class PersistenceTestSupport {
    @Autowired protected UserRepository users;
    @Autowired protected EntityManager entityManager;

    protected User createUser(String name, Role role, boolean active) {
        User user = new User();
        user.setUsername(name);
        user.setEmail(name + "@example.com");
        user.setPassword("encoded-password");
        user.setRole(role);
        user.setActive(active);
        return users.save(user);
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
