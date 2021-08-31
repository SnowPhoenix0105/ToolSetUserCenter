package top.snowphonix.toolsetusercenter.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private int uid;
    private String name;
    private int photo;
    private int auth;
    private LocalDateTime expire;
    private String email;
    private String bcrypt;
}
