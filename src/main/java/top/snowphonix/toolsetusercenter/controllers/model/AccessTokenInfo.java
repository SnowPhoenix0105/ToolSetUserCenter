package top.snowphonix.toolsetusercenter.controllers.model;

import lombok.Getter;
import lombok.Setter;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.utils.TimeUtil;

@Getter
@Setter
public class AccessTokenInfo {
    private String accessToken;
    private String accessExpireString;
    private long accessExpire;

    private String auth;

    public AccessTokenInfo(TokenExpireTuple accessTuple, AuthLevel auth) {
        accessToken = accessTuple.getToken();
        accessExpireString = TimeUtil.localDAteTimeToString(accessTuple.getExpire());
        accessExpire = TimeUtil.localDateTimeToTimestamp(accessTuple.getExpire());

        this.auth = auth.toString();
    }
}
