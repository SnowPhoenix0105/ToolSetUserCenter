package top.snowphonix.toolsetusercenter.utils;

import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.JwtPayload;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilGenerateAndValidateTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void withFullParam() throws JoseException {
        JwtUtil service = applicationContext.getBean(JwtUtil.class);
        int uid = 114514;
        String auth = AuthLevel.PASSERBY.toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plusMinutes(10);
        LocalDateTime notBefore = now.minusMinutes(2);
        JwtPayload old = JwtPayload.builder()
                .uid(uid)
                .auth(auth)
                .expire(expire)
                .issuedAt(now)
                .notBefore(notBefore)
                .build();
        String jwt = service.generateJwt(old);
        JwtPayload rlt = service.validateToken(jwt);

        assertEquals(old.getUid(), rlt.getUid());
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getExpire(), rlt.getExpire()));
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getNotBefore(), rlt.getNotBefore()));
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getIssuedAt(), rlt.getIssuedAt()));
        assertEquals(old.getAuth(), rlt.getAuth());
    }

    @Test
    void withoutIssuedAt() throws JoseException {
        JwtUtil service = applicationContext.getBean(JwtUtil.class);
        int uid = 114514;
        String auth = AuthLevel.PASSERBY.toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plusMinutes(10);
        LocalDateTime notBefore = now.minusMinutes(2);
        JwtPayload old = JwtPayload.builder()
                .uid(uid)
                .auth(auth)
                .expire(expire)
                .notBefore(notBefore)
                .build();
        LocalDateTime afterCreated = LocalDateTime.now();
        String jwt = service.generateJwt(old);
        JwtPayload rlt = service.validateToken(jwt);

        assertEquals(old.getUid(), rlt.getUid());
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getExpire(), rlt.getExpire()));
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getNotBefore(), rlt.getNotBefore()));
        assertEquals(old.getAuth(), rlt.getAuth());
        assertFalse(rlt.getIssuedAt().isBefore(now.minusSeconds(1)) || rlt.getIssuedAt().isAfter(afterCreated));
    }

    @Test
    void withoutNotBefore() throws JoseException {
        JwtUtil service = applicationContext.getBean(JwtUtil.class);
        int uid = 114514;
        String auth = AuthLevel.PASSERBY.toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plusMinutes(10);
        JwtPayload old = JwtPayload.builder()
                .uid(uid)
                .auth(auth)
                .expire(expire)
                .issuedAt(now)
                .build();
        String jwt = service.generateJwt(old);
        JwtPayload rlt = service.validateToken(jwt);

        assertEquals(old.getUid(), rlt.getUid());
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getExpire(), rlt.getExpire()));
        assertNull(old.getNotBefore());
        assertNull(rlt.getNotBefore());
        assertTrue(TimeUtil.localDateTimeSameSecond(old.getIssuedAt(), rlt.getIssuedAt()));
        assertEquals(old.getAuth(), rlt.getAuth());
    }
}