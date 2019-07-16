package order.statemachine;


import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component("stateListener")
@WithStateMachine(id = StateMachineConfig.orderStateMachineId)
public class StateListenerImpl {

    @OnTransition(source = "QUERY", target = "WAIT_PAYMENT")
    public boolean createTransition(Message<Events> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(States.WAIT_PAYMENT);
        System.out.println("创建 headers=" + message.getHeaders().toString() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }
    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public boolean payTransition(Message<Events> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(States.WAIT_DELIVER);
        System.out.println("支付 headers=" + message.getHeaders().toString() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }
    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public boolean deliverTransition(Message<Events> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(States.WAIT_RECEIVE);
        System.out.println("发货 headers=" + message.getHeaders().toString() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }
    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public boolean receiveTransition(Message<Events> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(States.FINISH);
        System.out.println("收货 headers=" + message.getHeaders().toString() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }
}
