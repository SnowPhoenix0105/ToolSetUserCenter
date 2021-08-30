package top.snowphonix.toolsetusercenter.service;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void first() throws JoseException, MalformedClaimException {
        TokenService service = applicationContext.getBean(TokenService.class);
        LocalDate now = LocalDate.now();
        System.out.println("now:" + now.format(DateTimeFormatter.ISO_DATE));
        String jwt = service.generateJwt(1, now);
        boolean ret = service.validateToken(jwt);
        assertTrue(ret);
    }

    @Test
    void generateJwt() {
    }

    @Test
    void validateToken() {
    }
}