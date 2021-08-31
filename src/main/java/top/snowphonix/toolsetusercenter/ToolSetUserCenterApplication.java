package top.snowphonix.toolsetusercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
public class ToolSetUserCenterApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Beijing"));
        SpringApplication.run(ToolSetUserCenterApplication.class, args);
    }
}
