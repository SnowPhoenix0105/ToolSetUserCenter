package top.snowphonix.toolsetusercenter.controllers.model;

import lombok.Getter;
import lombok.Setter;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;

import java.text.SimpleDateFormat;

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

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DoubleTokenInfo(TokenExpireTuple refreshTuple, TokenExpireTuple accessTuple, AuthLevel auth) {
        refreshToken = refreshTuple.getToken();
        refreshExpireString = dateFormat.format(refreshTuple.getExpire());
        refreshExpire = refreshTuple.getExpire().getTime();

        accessToken = refreshTuple.getToken();
        accessExpireString = dateFormat.format(accessTuple.getExpire());
        accessExpire = accessTuple.getExpire().getTime();

        this.auth = auth.toString();
    }
}
