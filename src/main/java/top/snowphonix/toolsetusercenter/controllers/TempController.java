package top.snowphonix.toolsetusercenter.controllers;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.snowphonix.toolsetusercenter.controllers.model.DoubleTokenInfo;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.TokenExpireTuple;
import top.snowphonix.toolsetusercenter.service.LoginService;
import top.snowphonix.toolsetusercenter.service.RegisterService;

@RestController
@RequestMapping("/temp")
public class TempController {
    private final RegisterService registerService;
    private final LoginService loginService;

    public TempController(RegisterService registerService, LoginService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @PostMapping("/create")
    public DoubleTokenInfo create() throws JoseException {
        int uid = registerService.createTempAccount();
        TokenExpireTuple refreshToken = new TokenExpireTuple();
        TokenExpireTuple accessToken = new TokenExpireTuple();
        AuthLevel auth = loginService.generateDoubleToken(uid, refreshToken, accessToken);
        assert auth.equals(AuthLevel.PASSERBY);
        return new DoubleTokenInfo(refreshToken, accessToken, AuthLevel.PASSERBY);
    }
}
