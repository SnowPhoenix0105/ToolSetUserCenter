package top.snowphonix.toolsetusercenter.mappers;

import org.apache.ibatis.annotations.*;
import top.snowphonix.toolsetusercenter.model.User;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name, photo, auth, expire, email, bcrypt) values " +
            "(#{name}, #{photo}, #{auth}, #{expire}, #{email}, #{bcrypt})")
    @Options(useGeneratedKeys = true)
    int insert(User user);

    @Select("select uid, name, photo, auth, expire, email, bcrypt " +
            "from user where uid=#{uid}")
    User selectByUid(int uid);

    @Update("update user set expire=#{expire} where uid=#{uid}")
    void updateExpire(int uid, LocalDateTime expire);
}
