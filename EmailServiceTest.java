package co.interleap.controllers;

import co.interleap.Application;
import co.interleap.email.EmailService;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application-test.properties"},properties = "management.port=0")
@ActiveProfiles("test")
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;
    private GreenMail greenMail;

    @Before
    public void setup(){
        ServerSetup serverSetup = new ServerSetup(3025,"localhost","smtp");
        greenMail = new GreenMail(serverSetup);
        greenMail.start();
    }

    @After
    public void stop(){
        greenMail.stop();
    }

    @Test
    public void shouldSendAnEmail() {
        GreenMailUser user = greenMail.setUser("user@localhost.com", "password");
        emailService.send(new EmailTemplate("Learning something interesting", "Looks great!"), "coolDeveloper@interleap.co");
        assertTrue(greenMail.waitForIncomingEmail(1000, 1));
    }
}
