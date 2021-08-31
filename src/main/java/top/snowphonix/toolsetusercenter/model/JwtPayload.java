package top.snowphonix.toolsetusercenter.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JwtPayload {
    /***
     * 用户UID
     */
    private int uid;
    /***
     * JWT失效时间
     */
    private LocalDateTime expire;
    /***
     * JWT签发时间，可能为null
     */
    private LocalDateTime issuedAt;
    /***
     * JWT生效时间，可能为null
     */
    private LocalDateTime notBefore;
    /***
     * 用户权限等级，可能为null
     */
    private String auth;
}
