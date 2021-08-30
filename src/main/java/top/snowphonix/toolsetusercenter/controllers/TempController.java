package top.snowphonix.toolsetusercenter.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.snowphonix.toolsetusercenter.controllers.model.DoubleTokenInfo;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.service.RegisterService;

@RestController
@RequestMapping("/temp")
public class TempController {
    private RegisterService registerService;

    @PostMapping("/create")
    public DoubleTokenInfo create() {
        int uid = registerService.createTempAccount();
        return new DoubleTokenInfo(null, null, AuthLevel.PASSERBY);
    }
}
