package o.statemachine;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;


@SpringBootApplication
public class StateMachineApplication implements CommandLineRunner {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    public static void main(String[] args) {

        SpringApplication.run(StateMachineApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        stateMachine.start();
        System.out.println("当前状态："+stateMachine.getState().getId()+ "即将输入条件 coin ---");
        stateMachine.sendEvent(Events.COIN);
        System.out.println("当前状态："+stateMachine.getState().getId()+"即将输入条件 coin ---");
        stateMachine.sendEvent(Events.COIN);
        System.out.println("--- push ---");
        stateMachine.sendEvent(Events.PUSH);
        System.out.println("--- push ---");
        stateMachine.sendEvent(Events.PUSH);
        stateMachine.stop();
    }
}

