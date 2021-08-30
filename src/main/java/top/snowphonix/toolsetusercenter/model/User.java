package top.snowphonix.toolsetusercenter.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int uid;
    private String name;
    private int photo;
    private int auth;
    private LocalDate expire;
    private String email;
    private String bcrypt;
}
