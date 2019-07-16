package o.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StatemachineConfig extends EnumStateMachineConfigurerAdapter<States,Events> {
    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                // 初识状态：Locked
                .initial(States.Locked)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.Unlocked).target(States.Locked)
                .event(Events.PUSH).action(customerPassAndLock())
                .and()
                .withExternal()
                .source(States.Locked).target(States.Unlocked)
                .event(Events.COIN).action(turnstileUnlock())
        ;
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config.withConfiguration()
                .machineId("StateMachine")
                .autoStartup(true)
                .listener(listener());
        ;
    }

    public Action<States, Events> turnstileUnlock() {
        return context -> System.out.println("解锁旋转门，以便游客能够通过" );
    }

    public Action<States, Events> customerPassAndLock() {
        return context -> System.out.println("当游客通过，锁定旋转门" );
    }
}

//public class Config1Strings
//        extends StateMachineConfigurerAdapter<String, String> {
//
//    @Override
//    public void configure(StateMachineStateConfigurer<String, String> states)
//            throws Exception {
//        states
//                .withStates()
//                .initial("S1")
//                .end("SF")
//                .states(new HashSet<String>(Arrays.asList("S1","S2","S3","S4")));
//    }
//
//}
