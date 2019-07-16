package order.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;

@Configuration
//@EnableStateMachine(name = "orderStateMachine")
@EnableStateMachineFactory(name = "orderStateMachineFactory")
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States,Events> {

    /**订单状态机ID*/
    public static final String orderStateMachineId = "orderStateMachineId";

    /**
     * 配置状态
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                // 初识状态：Locked
                .initial(States.QUERY)
                .states(EnumSet.allOf(States.class));
    }

    /**
     * 配置状态转换事件关系
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.QUERY).target(States.WAIT_PAYMENT)
                .event(Events.CREAT).action(create())
                .and()
                .withExternal()
                .source(States.WAIT_PAYMENT).target(States.WAIT_DELIVER)
                .event(Events.PAYED).action(payed())
                .and()
                .withExternal()
                .source(States.WAIT_DELIVER).target(States.WAIT_RECEIVE)
                .event(Events.DELIVERY).action(delivery())
                .and()
                .withExternal()
                .source(States.WAIT_RECEIVE).target(States.FINISH)
                .event(Events.RECEIVED).action(received())
        ;
    }

    /**
     * 持久化配置
     * 实际使用中，可以配合redis等，进行持久化操作
     * @return
     */
    @Bean
    public StateMachinePersister<States, Events, Order> persister(){
        return new DefaultStateMachinePersister<>(new StateMachinePersist<States, Events, Order>() {
            @Override
            public void write(StateMachineContext<States, Events> context, Order order) throws Exception {
                //此处并没有进行持久化操作
                order.setStatus(context.getState());
            }
            @Override
            public StateMachineContext<States, Events> read(Order order) throws Exception {
                //此处直接获取order中的状态，其实并没有进行持久化读取操作
                StateMachineContext<States, Events> result =new DefaultStateMachineContext<>(order.getStatus(), null, null, null, null, orderStateMachineId);
                return result;
            }
        });
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
                .listener(listener());
        ;
    }

    public Action<States, Events> create() {
        return context -> System.out.println("action: 查到货物，创建订单，预定商品" );
    }
    public Action<States, Events> payed() {
        return context -> System.out.println("action: 支付订单，等待发货" );
    }
    public Action<States, Events> delivery() {
        return context -> System.out.println("action: 订单发货，等待收货" );
    }
    public Action<States, Events> received() {
        return context -> System.out.println("action: 收货确认" );
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
