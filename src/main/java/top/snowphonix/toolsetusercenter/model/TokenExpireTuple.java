package top.snowphonix.toolsetusercenter.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenExpireTuple {
    private String token;
    private LocalDateTime expire;
}
