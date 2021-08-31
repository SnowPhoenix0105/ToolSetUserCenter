package top.snowphonix.toolsetusercenter.controllers.model;

import lombok.Getter;
import lombok.Setter;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.utils.TimeUtil;

@Getter
@Setter
public class RefreshTokenInfo {
    private String refreshToken;
    private String refreshExpireString;
    private long refreshExpire;

    private String auth;

    public RefreshTokenInfo(TokenExpireTuple refreshTuple, AuthLevel auth) {
        refreshToken = refreshTuple.getToken();
        refreshExpireString = TimeUtil.localDAteTimeToString(refreshTuple.getExpire());
        refreshExpire = TimeUtil.localDateTimeToTimestamp(refreshTuple.getExpire());

        this.auth = auth.toString();
    }
}
