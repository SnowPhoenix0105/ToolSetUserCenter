package top.snowphonix.toolsetusercenter.service;

import top.snowphonix.toolsetusercenter.mappers.UserMapper;
import top.snowphonix.toolsetusercenter.model.AuthLevel;
import top.snowphonix.toolsetusercenter.model.User;

import java.time.LocalDate;
import java.util.Date;

public class RegisterService {
    private UserMapper userMapper;

    /***
     * 创建一个临时账户，返回其uid，其有效期为24*3=72小时，权限为passerby
     *
     * @return 临时账户的uid
     */
    public int createTempAccount() {
        User tempUser = User.builder()
                .photo(0)
                .name("游客")
                .expire(LocalDate.now().plusDays(3))
                .email(null)
                .bcrypt("")
                .auth(AuthLevel.PASSERBY.toNum())
                .build();
        int uid = userMapper.insert(tempUser);
        return uid;
    }
}
