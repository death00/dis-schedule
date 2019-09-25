package top.death00.dis.schedule.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * @author death00
 * @date 2019/9/24 18:56
 */
@Configuration
public class MongodbConfig {

    private final Environment env;

    public MongodbConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public MongoClientOptions mongoClientOptions() {
        MongoClientOptions.Builder builder = MongoClientOptions.builder();
        builder.connectionsPerHost(1000);
        builder.threadsAllowedToBlockForConnectionMultiplier(1000);
        builder.maxWaitTime(180000);
        builder.connectTimeout(2000);
        builder.socketTimeout(120000);
        builder.writeConcern(new WriteConcern(1, 10000));

        return builder.build();
    }

    @Bean
    public MongoClient mongoClient() {
        String host = env.getProperty("mongoDB.host");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(host));
        String port = env.getProperty("mongoDB.port");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(port));
        String username = env.getProperty("mongoDB.username");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username));
        String password = env.getProperty("mongoDB.password");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(password));
        String authDB = env.getProperty("mongoDB.authDB");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(authDB));

        return new MongoClient(
                new ServerAddress(
                        host,
                        Integer.valueOf(port.trim())
                ),
                MongoCredential.createCredential(
                        username,
                        authDB,
                        password.trim().toCharArray()
                ),
                mongoClientOptions()
        );
    }

    @Bean
    public SimpleMongoDbFactory mongoDbFactory() {
        String database = env.getProperty("mongoDB.database");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(database));
        return new SimpleMongoDbFactory(mongoClient(), database);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}
