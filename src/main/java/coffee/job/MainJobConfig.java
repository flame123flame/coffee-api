package coffee.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import coffee.buy.service.BuyYeekeeService;

@Configuration
public class MainJobConfig {

    @Bean
    public void onStart() {
        // System.out.println("=========> on start");
        // resultYeeKeeJob.getClassYeeKee();
        // setTimeYeeKeeJob.addTaskToScheduler("YeeKee", () -> {
        // System.out.println("Start ===============================");
        // resultYeeKeeJob.getClassYeeKee();
        // });
    }
}