package order.statemachine;

public enum States {
    // 查询，待支付，待发货，待收货，订单结束
    QUERY, WAIT_PAYMENT, WAIT_DELIVER, WAIT_RECEIVE, FINISH;

}
