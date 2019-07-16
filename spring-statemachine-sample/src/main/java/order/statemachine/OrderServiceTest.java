package order.statemachine;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderServiceImpl orderService;
    @Test
    public void testMultThread(){
        orderService.query();

        orderService.creat(1);
        orderService.pay(1);
        orderService.deliver(1);
        orderService.receive(1);
//        new Thread(()->{
//
//        }).start();


        System.out.println(orderService.getOrders());
    }
}