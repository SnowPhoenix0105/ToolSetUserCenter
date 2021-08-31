package top.snowphonix.toolsetusercenter.service;

import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import top.snowphonix.toolsetusercenter.mappers.UserMapper;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.JwtPayload;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.model.User;
import top.snowphonix.toolsetusercenter.utils.JwtUtil;
import top.snowphonix.toolsetusercenter.utils.TimeUtil;

import java.time.LocalDateTime;

@Service
public class LoginService {
    public LoginService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthLevel generateDoubleToken(int uid, TokenExpireTuple outRefreshToken, TokenExpireTuple outAccessToken) throws JoseException {
        User user = userMapper.selectByUid(uid);
        AuthLevel auth = AuthLevel.ofNum(user.getAuth());
        if (user.getAuth() == AuthLevel.PASSERBY.toNum()) {
            LocalDateTime now = LocalDateTime.now();
            JwtPayload refreshPayload = JwtPayload.builder()
                    .uid(uid)
                    .issuedAt(now)
                    .notBefore(now.minusMinutes(2))
                    .expire(user.getExpire())
                    .build();
            String refreshToken = jwtUtil.generateJwt(refreshPayload);
            outRefreshToken.setToken(refreshToken);
            outRefreshToken.setExpire(user.getExpire());

            LocalDateTime accessExpire = TimeUtil.earliest(now.plusMinutes(10), user.getExpire());
            JwtPayload accessPayload = JwtPayload.builder()
                    .uid(uid)
                    .issuedAt(now)
                    .notBefore(now.minusMinutes(2))
                    .expire(accessExpire)
                    .auth(auth.toString())
                    .build();
            String accessToken = jwtUtil.generateJwt(accessPayload);
            outAccessToken.setToken(accessToken);
            outAccessToken.setExpire(accessExpire);

            return auth;
        }
        // TODO
        throw new NotImplementedException();
    }
}
