package top.snowphonix.toolsetusercenter.model;

import lombok.Data;

import java.util.Date;

@Data
public class TokenExpireTuple {
    private String token;
    private Date expire;
}
