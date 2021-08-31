package top.snowphonix.toolsetusercenter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jwt")
@Component
@Getter
@Setter
public class JwtConfig {
    private String publicKeyPath;
    private String privateKeyPath;
}
