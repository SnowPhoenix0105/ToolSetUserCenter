package top.snowphonix.toolsetusercenter.controllers.model;

import lombok.Getter;
import lombok.Setter;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.utils.TimeUtil;

@Setter
@Getter
public class DoubleTokenInfo {
    private String refreshToken;
    private String refreshExpireString;
    private long refreshExpire;

    private String accessToken;
    private String accessExpireString;
    private long accessExpire;

    private String auth;

    public DoubleTokenInfo(TokenExpireTuple refreshTuple, TokenExpireTuple accessTuple, AuthLevel auth) {
        refreshToken = refreshTuple.getToken();
        refreshExpireString = TimeUtil.localDAteTimeToString(refreshTuple.getExpire());
        refreshExpire = TimeUtil.localDateTimeToTimestamp(refreshTuple.getExpire());

        accessToken = accessTuple.getToken();
        accessExpireString = TimeUtil.localDAteTimeToString(accessTuple.getExpire());
        accessExpire = TimeUtil.localDateTimeToTimestamp(accessTuple.getExpire());

        this.auth = auth.toString();
    }
}
