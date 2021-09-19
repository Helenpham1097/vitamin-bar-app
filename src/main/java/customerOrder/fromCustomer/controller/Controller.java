package customerOrder.fromCustomer.controller;


import customerOrder.fromCustomer.controller.request.OrderRequest;
import customerOrder.fromCustomer.dto.CartItemsDto;
import customerOrder.fromCustomer.dto.CustomerDto;
import customerOrder.fromCustomer.dto.OrderDto;
import customerOrder.fromCustomer.service.CustomerOrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("store-order")
public class Controller {
    @Autowired
    CustomerOrderImpl customerOrderImpl;

    @Autowired
    public void setCustomerOrderImpl(CustomerOrderImpl customerOrder){
        this.customerOrderImpl = customerOrder;
    }

    @PostMapping("/add")
    public ResponseEntity<String> insertNewOrder(@RequestBody OrderRequest request){
        customerOrderImpl.orderedReceiveFromCustomer(request.getPhone(),request.getCustomer(),
                request.getOrder(),request.getItems());
        return ResponseEntity.ok("added");
    }
    @GetMapping("/customer")
    public List<CustomerDto> getAllCustomer(){
        return customerOrderImpl.getAllCustomersInformation();
    }

    @GetMapping("/customer/{phoneNumber}")
    public List<OrderDto> getOrders(@PathVariable String phoneNumber){
        return customerOrderImpl.findAllOrdersOfACustomer(phoneNumber);
    }
    @GetMapping("/order/{orderNumber}")
    public Set<CartItemsDto> getItemsOfAnOrder(@PathVariable String orderNumber){
        return customerOrderImpl.getItemsOfAOrder(orderNumber);
    }

    @PostMapping("/update-item")
    public ResponseEntity<String> updateItemsInCart(@RequestBody OrderRequest orderRequest){
        customerOrderImpl.updateQuantityOfItemsInAnOrder(orderRequest.getOrderNumber(), orderRequest.getItemCode() ,orderRequest.getQuantity());
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/delete-item/{itemName}")
    public ResponseEntity<String> deleteItemOfAnOrder(@PathVariable String itemName){
        customerOrderImpl.deleteAnItemOfOrder(itemName);
        return ResponseEntity.ok("Delete Item Successfully");
    }

    @DeleteMapping("/delete-order/{orderNumber}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderNumber){
        customerOrderImpl.deleteOrder(orderNumber);
        return ResponseEntity.ok("Delete Order Successfully");
    }
}
