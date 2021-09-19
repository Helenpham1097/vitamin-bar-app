package customerOrder.fromServer.controller.request;

import lombok.Data;

@Data
public class FinalRequest {
    String customerPhone;
    String orderNumber;
}
