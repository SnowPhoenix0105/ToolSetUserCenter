package top.snowphonix.toolsetusercenter.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.snowphonix.toolsetusercenter.model.User;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name, photo, auth, expire, email, bcrypt) values " +
            "(#{name}, #{photo}, #{auth}, #{expire}, #{email}, #{bcrypt})")
    @Options(useGeneratedKeys = true)
    int insert(User user);

    @Select("select uid, name, photo, auth, expire, email, bcrypt" +
            "from user where uid=#{uid}")
    User selectByUid(int uid);
}
