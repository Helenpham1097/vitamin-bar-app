package com.vitaminBar.customerOrder.ordersManagement.controller;


import com.vitaminBar.customerOrder.ordersManagement.controller.request.FinalRequest;
import com.vitaminBar.customerOrder.ordersManagement.controller.request.OrderRequest;
import com.vitaminBar.customerOrder.ordersManagement.dto.CartItemsDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CompletedOrderDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CustomerDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.OrderDto;
import com.vitaminBar.customerOrder.ordersManagement.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/store-order")
public class Controller {

    private final CustomerOrderService customerOrder;

//    @RequestMapping("/")
//    public String home(){
//        return "Hello World!";
//    }
    @Autowired
    public Controller(CustomerOrderService customerOrder){
        this.customerOrder = customerOrder;
    }

    @PostMapping("/add")
    public ResponseEntity<String> insertNewOrder(@RequestBody OrderRequest request){
        customerOrder.getNewOrder(request.getPhone(),request.getCustomer(),
                request.getOrder(),request.getItems());
        return ResponseEntity.ok("Thank you for shopping with us");
    }
//    @PostMapping(path = "/add", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//    public ResponseEntity<String> insertNewOrder( OrderRequest request){
//        customerOrder.getNewOrder(request.getPhone(),request.getCustomer(),
//                request.getOrder(),request.getItems());
//        return ResponseEntity.ok("added");
//    }
    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomer(){
        return customerOrder.getAllCustomersInformation();
    }

    @GetMapping("/customer/{phoneNumber}")
    public List<OrderDto> getOrders(@PathVariable String phoneNumber){
        return customerOrder.findAllOrdersOfACustomer(phoneNumber);
    }
    @GetMapping("/order/{orderNumber}")
    public Set<CartItemsDto> getItemsOfAnOrder(@PathVariable String orderNumber){
        return customerOrder.getItemsOfAOrder(orderNumber);
    }

    @PostMapping("/update-item")
    public ResponseEntity<String> updateItemsInCart(@RequestBody OrderRequest orderRequest){
        customerOrder.updateQuantityOfItemsInAnOrder(orderRequest.getOrderNumber(), orderRequest.getItemCode() ,orderRequest.getQuantity());
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/delete-item/{itemCode}")
    public ResponseEntity<String> deleteItemOfAnOrder(String orderNumber,@PathVariable String itemCode){
        customerOrder.deleteAnItemOfOrder(orderNumber,itemCode);
        return ResponseEntity.ok("Delete Item Successfully");
    }

    @DeleteMapping("/delete-order/{orderNumber}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderNumber){
        customerOrder.deleteOrder(orderNumber);
        return ResponseEntity.ok("Delete Order Successfully");
    }
    //new
    @PostMapping("/process-order")
    public ResponseEntity<String> addCompletedOrder(@RequestBody FinalRequest finalRequest){
        customerOrder.addNewCompletedOrder(finalRequest.getOrderNumber());
        return ResponseEntity.ok("Done");
    }

    @GetMapping("/get-confirmed-order/{orderNumber}")
    public CompletedOrderDto getConfirmedOrder(@PathVariable String orderNumber){
        return customerOrder.findCompletedOrderByOrderNumber(orderNumber);
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<String> deleteCompletedOrder(@PathVariable String orderNumber){
        customerOrder.deleteCompletedOrder(orderNumber);
        return ResponseEntity.ok("Deleted");
    }
}
