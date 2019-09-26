package top.death00.dis.schedule;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author death00
 * @date 2019/9/24
 */
@SpringBootApplication(
	exclude = {
		MongoDataAutoConfiguration.class,
		MongoReactiveDataAutoConfiguration.class
	}
)
@PropertySource(value = "file:${EXAMPLE_BASE}/example.conf")
public class StartServer {

	public static void main(String[] args) {
		// 加载日志相关配置
		String basePath = System.getenv("EXAMPLE_BASE");
		PropertyConfigurator.configure(basePath + "/log.properties");

		SpringApplication.run(StartServer.class, args);
	}
}
