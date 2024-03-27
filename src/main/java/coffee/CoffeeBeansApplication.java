package coffee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import coffee.buy.service.BuyYeekeeService;
import coffee.buy.service.RunCustomerYeeKee;
import lombok.extern.slf4j.Slf4j;

@EnableAsync
@ComponentScan({ "coffee", "framework" })
@EntityScan(basePackages = { "coffee", "framework" })
@EnableJpaRepositories(basePackages = { "coffee", "framework" })
@EnableTransactionManagement
@SpringBootApplication
@Slf4j
public class CoffeeBeansApplication {

	public static void main(final String[] args) {
		SpringApplication.run(CoffeeBeansApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://49.0.80.15", "https://finnbet.com",
						"http://172.19.93.12:8080", "http://172.19.93.12:8083", "http://172.19.50.118:4200",
						"https://americanodrift.com");
			}
		};

	}

}
