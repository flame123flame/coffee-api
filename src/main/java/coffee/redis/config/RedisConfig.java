package coffee.redis.config;

import java.time.Duration;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Primary
@Configuration
@EnableTransactionManagement
public class RedisConfig {
	public static PropertiesUtil propertiesUtil = new PropertiesUtil("redis.properties");

	static int minIdle = propertiesUtil.getIntegerProperty("redis.pool.minIdle", 10);
	static int maxIdle = propertiesUtil.getIntegerProperty("redis.pool.maxIdle", 50);
	static int maxTotal = propertiesUtil.getIntegerProperty("redis.pool.maxTotal", 100);
	static int maxWaitMillisec = propertiesUtil.getIntegerProperty("redis.pool.maxWaitMillisec", 3000);
	static int commandTimeout = propertiesUtil.getIntegerProperty("redis.client.command.timeout.millisec", 10000);

	@SuppressWarnings("rawtypes")
	@Bean(name = "genericObjectPoolConfig")
	public GenericObjectPoolConfig genericObjectPoolConfig() {
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMinIdle(minIdle);
		genericObjectPoolConfig.setMaxIdle(maxIdle);
		genericObjectPoolConfig.setMaxTotal(maxTotal);
		genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillisec);
		return genericObjectPoolConfig;
	}

	@Bean(name = "clientResources")
	public ClientResources clientResources() {
		return DefaultClientResources.create();
	}

	@Lazy
	@SuppressWarnings("rawtypes")
	@Bean(name = "clientConfig")
	public LettuceClientConfiguration connectionFactory(
			@Qualifier("genericObjectPoolConfig") GenericObjectPoolConfig genericObjectPoolConfig,
			@Qualifier("clientResources") ClientResources clientResources) {
		LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
				.builder().clientResources(clientResources).commandTimeout(Duration.ofMillis(commandTimeout))
				.poolConfig(genericObjectPoolConfig)
				.clientOptions(ClientOptions.builder().cancelCommandsOnReconnectFailure(true).build());

		LettuceClientConfiguration clientConfiguration = builder.build();
		return clientConfiguration;
	}

}
