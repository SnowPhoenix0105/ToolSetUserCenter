package top.snowphonix.toolsetusercenter.service;

import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;
import top.snowphonix.toolsetusercenter.mappers.UserMapper;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.JwtPayload;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.model.User;
import top.snowphonix.toolsetusercenter.utils.JwtUtil;

import java.time.LocalDateTime;

@Service
public class RefreshService {
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public RefreshService(JwtUtil jwtUtil, UserMapper userMapper) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    /***
     * 通过旧的refreshToken获取新的refreshToken。
     * 若{@code outNewToken.getExpire() != null}时，refreshToken的有效期将以该值为准，否则为10天后。
     * 当expire大于账户有效期时，将延长账户有效期。
     * 成功时，返回用户的权限等级，并将新的refreshToken及其过期时间保存在outNewToken中。
     * 当旧refreshToken过期或不正确时，将会返回null。
     *
     * @param oldToken 旧的token
     * @param outNewToken 新的token信息将会保存在此对象中
     * @return 用户的权限等级，若失败则为null
     */
    public AuthLevel getNewRefreshToken(String oldToken, TokenExpireTuple outNewToken) throws JoseException {
        JwtPayload oldTokenPayload = jwtUtil.validateToken(oldToken);
        if (oldTokenPayload == null) {
            return null;
        }
        User user = userMapper.selectByUid(oldTokenPayload.getUid());
        LocalDateTime expire = outNewToken.getExpire();
        LocalDateTime now = LocalDateTime.now();
        if (expire == null) {
            expire = now.plusDays(10);
            outNewToken.setExpire(expire);
        }
        if (user.getAuth() == AuthLevel.PASSERBY.toNum() && user.getExpire().isBefore(expire)) {
            user.setExpire(expire);
            userMapper.updateExpire(user.getUid(), expire);
        }
        JwtPayload newTokenPayload = JwtPayload.builder()
                .uid(user.getUid())
                .expire(expire)
                .issuedAt(now)
                .notBefore(now.minusMinutes(2))
                .build();
        String newToken = jwtUtil.generateJwt(newTokenPayload);
        outNewToken.setToken(newToken);
        return AuthLevel.ofNum(user.getAuth());
    }

    /***
     * 通过旧的refreshToken获取新的refreshToken。
     * 若{@code outNewToken.getExpire() != null}时，refreshToken的有效期将以该值为准，否则为10分钟后。
     * 当expire大于账户有效期时，将取账户有效期作为expire。
     * 成功时，返回用户的权限等级，并将新的refreshToken及其过期时间保存在outNewToken中。
     * 当旧refreshToken过期或不正确时，将会返回null。
     *
     * @param refreshToken 旧的token
     * @param outAccessToken 新的token信息将会保存在此对象中
     * @return 用户的权限等级，若失败则为null
     */
    public AuthLevel getAccessToken(String refreshToken, TokenExpireTuple outAccessToken) throws JoseException {
        JwtPayload oldTokenPayload = jwtUtil.validateToken(refreshToken);
        if (oldTokenPayload == null) {
            return null;
        }
        User user = userMapper.selectByUid(oldTokenPayload.getUid());
        AuthLevel auth = AuthLevel.ofNum(user.getAuth());
        LocalDateTime expire = outAccessToken.getExpire();
        LocalDateTime now = LocalDateTime.now();
        if (expire == null) {
            expire = now.plusDays(10);
            outAccessToken.setExpire(expire);
        }
        if (user.getAuth() == AuthLevel.PASSERBY.toNum() && user.getExpire().isBefore(expire)) {
            expire = user.getExpire();
        }
        if (expire.isBefore(now)) {
            return null;
        }
        JwtPayload newTokenPayload = JwtPayload.builder()
                .uid(user.getUid())
                .auth(auth.toString())
                .expire(expire)
                .issuedAt(now)
                .notBefore(now.minusMinutes(2))
                .build();
        String newToken = jwtUtil.generateJwt(newTokenPayload);
        outAccessToken.setToken(newToken);
        return AuthLevel.ofNum(user.getAuth());
    }
}
