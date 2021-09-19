package customerOrder.fromServer.controller;


import customerOrder.fromServer.dto.CompletedOrderDto;
import customerOrder.fromServer.controller.request.FinalRequest;
import customerOrder.fromServer.service.FinalOrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("store-order")
public class CompletedOrderController {
    @Autowired
    FinalOrderImpl finalOrderImpl;

    public void setFinalOrderImpl(FinalOrderImpl finalOrderImpl) {
        this.finalOrderImpl = finalOrderImpl;
    }

    @PostMapping("/process-order")
    public ResponseEntity<String> addCompletedOrder(@RequestBody FinalRequest finalRequest){
        finalOrderImpl.getCompletedOrderAfterCalculate(finalRequest.getCustomerPhone(), finalRequest.getOrderNumber());
        return ResponseEntity.ok("Done");
    }

    @GetMapping("/get-confirmed-order/{orderNumber}")
    public CompletedOrderDto getConfirmedOrder(@PathVariable String orderNumber){
        return finalOrderImpl.getCompletedOrder(orderNumber);
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<String> deleteCompletedOrder(@PathVariable String orderNumber){
        finalOrderImpl.deleteCompletedOrder(orderNumber);
        return ResponseEntity.ok("Deleted");
    }

}
