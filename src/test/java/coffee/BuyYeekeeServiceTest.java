package coffee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import coffee.buy.service.BuyYeekeeService;

@SpringBootTest
public class BuyYeekeeServiceTest {
    @Autowired
    private BuyYeekeeService buyYeekeeService;

    @Test
    public void buyYeekeeTest() throws Exception {
//        buyYeekeeService.buyYeekee();
    }

}