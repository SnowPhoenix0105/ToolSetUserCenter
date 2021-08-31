package top.snowphonix.toolsetusercenter.controllers;

import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.snowphonix.toolsetusercenter.controllers.model.AccessTokenInfo;
import top.snowphonix.toolsetusercenter.controllers.model.RefreshTokenInfo;
import top.snowphonix.toolsetusercenter.controllers.model.RefreshTokenSimpleInfo;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.service.RefreshService;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final RefreshService refreshService;

    public TokenController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenInfo> refresh(@RequestBody RefreshTokenSimpleInfo refreshToken) throws JoseException {
        TokenExpireTuple refreshTokenTuple = new TokenExpireTuple();
        AuthLevel auth = refreshService.getNewRefreshToken(refreshToken.getRefreshToken(), refreshTokenTuple);

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new RefreshTokenInfo(refreshTokenTuple, auth));
    }

    @PostMapping("/access")
    public ResponseEntity<AccessTokenInfo> access(@RequestBody RefreshTokenSimpleInfo refreshToken) throws JoseException {
        TokenExpireTuple refreshTokenTuple = new TokenExpireTuple();
        AuthLevel auth = refreshService.getNewRefreshToken(refreshToken.getRefreshToken(), refreshTokenTuple);

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new AccessTokenInfo(refreshTokenTuple, auth));
    }
}
