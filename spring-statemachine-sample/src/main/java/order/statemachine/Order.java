package order.statemachine;

public class Order {

    private Integer id;
    private Integer orderId;
    private States states;


    public Order() {
    }

    public Order(Integer orderId, States status) {
        this.orderId = orderId;
        this.states = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public States getStatus() {
        return states;
    }

    public void setStatus(States status) {
        this.states = status;
    }

    @Override
        public String toString() {
            return "Order{" +
                    "orderId=" + orderId +
                    ", status=" + states +
                    '}';
        }

}
