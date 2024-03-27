package coffee.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class LastListYeekeeRedisConfig {
    private final int index = 1;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;
    final static String stc = "redisLastListYeekeeMessage";
    final static String factory = "redisLastListYeekeeFactory";
    final static String template = "redisLastListYeekeeTemplate";

    @Bean(name = stc)
    public RedisStandaloneConfiguration standaloneConfig() {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(host);
        standaloneConfig.setPort(port);
        return standaloneConfig;
    }

    @Primary
    @Bean(name = factory)
    public RedisConnectionFactory connectionFactory(
            @Qualifier("clientConfig") LettuceClientConfiguration clientConfiguration) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(standaloneConfig(), clientConfiguration);
        factory.setShareNativeConnection(true);
        factory.setDatabase(index);
        factory.afterPropertiesSet();
        return factory;
    }

    @Primary
    @Bean(name = template)
    public StringRedisTemplate redisTemplate(@Qualifier(factory) RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
}