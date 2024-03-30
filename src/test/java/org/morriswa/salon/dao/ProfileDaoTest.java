package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class ProfileDaoTest extends DaoTest {

    @Test
    public void getClientInfoQuery() throws Exception {
        var clientInfo = profileDao.getClientInfo(11L);

        assertEquals("client email is as expected",
                "client1@morriswa.org", clientInfo.getEmail());
        assertEquals("client pronouns are correct",
                "They/Them/Theirs", clientInfo.getPronouns());
    }
}
