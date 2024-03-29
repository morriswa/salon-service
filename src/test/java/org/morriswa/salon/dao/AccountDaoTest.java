package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.*;

@Transactional
public class AccountDaoTest extends DaoTest{

    private final AccountDao dao;
    private final PasswordEncoder encoder;

    @Autowired
    public AccountDaoTest(AccountDao dao, PasswordEncoder encoder) {
        this.dao = dao;
        this.encoder = encoder;
    }

    @Test
    void registerUserQuery() throws Exception {

        final String username = "username";
        final String password = "password";

        dao.register(username, password);

        var createdUser = dao.findUser(username);

        assertNotNull("registered user should be present", createdUser);
        assertNotNull("new user must have user id", createdUser.getUserId());
        assertNotNull("new user must have account creation date", createdUser.getDateCreated());
        assertEquals("registered user should have requested username", username, createdUser.getUsername());
        assertTrue("registerd user should have requested password", encoder.matches(password, createdUser.getPassword()));
        assertTrue(
                "registered user should have correct authorities",
                createdUser.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );
    }

    @Test
    void registerDuplicateUserQuery() throws Exception {

        final String username = "username";
        final String password = "password";

        dao.register(username, password);

        var exception = assertThrows(ValidationException.class, ()->dao.register(username, password));

        assertNotNull("should throw appropriate error", exception);
        assertTrue("exception should contain helpful info", exception.containsErrors());
    }
}
