package framework.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@PropertySources({ 
	@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),//for dev only
    @PropertySource(value = "file:${external.config.path}/application-external.yml", ignoreResourceNotFound = true)// for deploy in sever
	
})
@EnableAsync
public class AppConfig {

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		logger.info("placeHolderConfigurer");
		return new PropertySourcesPlaceholderConfigurer();
	}

}
