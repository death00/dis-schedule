package top.death00;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author gyj
 * @date 2019/5/25
 */
@SpringBootApplication(
        exclude = {
                MongoDataAutoConfiguration.class,
                MongoReactiveDataAutoConfiguration.class
        }
)
@PropertySource(value = "file:${WS_SERV_BASE}/conf/schedule-service.conf")
public class StartServer {

    public static void main(String[] args) {
        // 加载日志相关配置
        String basePath = System.getenv("WS_SERV_BASE");
        PropertyConfigurator.configure(basePath + "/conf/schedule-service-log.properties");

        SpringApplication.run(StartServer.class, args);
    }
}
