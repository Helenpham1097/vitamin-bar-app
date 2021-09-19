package customerOrder.fromServer;

public enum OrderStatus {
    RECEIVED ("We are received your order"),
    PROCESSING("We are processing on your order"),
    SHIPPING ("Your order is already sent out"),
    DELIVERED ("Your order is already delivered"),
    READYTOPICKUP("Your order is already to pick up. Your drink will be best taste in 30 minutes"),
    CANCEL("Your order is cancelled");
    private String message;
    OrderStatus(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderStatus getStatus(String currentStatus) throws IllegalArgumentException{
        for(OrderStatus status:OrderStatus.values()){
            if(status.name().equals(currentStatus)){
                return status;
            }else {
                throw new IllegalArgumentException("Invalid status");
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return this.name() + this.getMessage();
    }
}
