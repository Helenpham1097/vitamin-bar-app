package customerOrder.fromServer.service;


import com.example.demo.dao.TypeInformationRepository;
import com.example.demo.model.TypeInformation;
import customerOrder.fromCustomer.dao.CustomerRepository;
import customerOrder.fromCustomer.dao.OrderRepository;
import customerOrder.fromCustomer.dto.CartItemsDto;
import customerOrder.fromCustomer.dto.OrderDto;
import customerOrder.fromCustomer.model.Customer;
import customerOrder.fromCustomer.model.Item;
import customerOrder.fromCustomer.model.Order;
import customerOrder.fromServer.dto.CompletedOrderDto;
import customerOrder.fromServer.dao.CompletedOrderRepository;
import customerOrder.fromServer.model.CompletedItem;
import customerOrder.fromServer.model.CompletedOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FinalOrderImpl implements FinalOrder {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TypeInformationRepository typeInformationRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CompletedOrderRepository completedOrderRepository;


    private CompletedItem getCompletedItem(Item item) {
        String itemCode = item.getItemCode();
        TypeInformation type = typeInformationRepository.findSpecificItemByItsCode(itemCode);
        CompletedItem completedItem = new CompletedItem();
        completedItem.setItemCode(itemCode);
        completedItem.setItemName(type.getName());
        completedItem.setQuantity(item.getQuantity());
        completedItem.setPrice(type.getPrice()*item.getQuantity());
        return completedItem;
    }

    private CompletedOrder getCompletedOrder(Order order, Set<CompletedItem> items){
        CompletedOrder completedOrder = new CompletedOrder();
        completedOrder.setOrderDate(order.getOrderDate());
        completedOrder.setOrderNumber(order.getOrderNumber());
        Set<Double> totalPerItem = items
                .stream()
                .map(CompletedItem::getPrice)
                .collect(Collectors.toSet());
        double totalPay = totalPerItem
                .stream()
                .reduce(0.0,Double::sum);
        completedOrder.setTotalBill(totalPay);
        return completedOrder;
    }

    @Transactional
    public void getCompletedOrderAfterCalculate(String customerPhone, String orderNumber){
        Customer customer = customerRepository.getCustomerByPhone(customerPhone);
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        Set<Item> items = order.getItems();
        Set <CompletedItem> completedItems = items.stream()
                .map(this::getCompletedItem)
                .collect(Collectors.toSet());
        CompletedOrder completedOrder = getCompletedOrder(order, completedItems);
        completedOrder.addCompletedItems(completedItems);
        customer.addCompletedOrder(completedOrder);
        customer.setPoint((int) completedOrder.getTotalBill()/ 10000 + customer.getPoint());
        completedOrderRepository.save(completedOrder);

    }

    @Transactional
    public CompletedOrderDto getCompletedOrder(String orderNumber) {
        CompletedOrder completedOrder = completedOrderRepository.getCompletedOrder(orderNumber);
        return toCompletedOrderDto(completedOrder);
    }

    @Transactional
    public void deleteCompletedOrder(String orderNumber) {
        CompletedOrder completedOrder = completedOrderRepository.getCompletedOrder(orderNumber);
        completedOrder.removeCompletedItems(completedOrder.getCompletedItems());
        completedOrderRepository.deleteCompletedOrder(orderNumber);
    }

    public OrderDto toOrderDto(CompletedOrder order) {
        return new OrderDto(order.getOrderNumber(), order.getTotalBill());
    }

    public Set<CartItemsDto> toCartItemsDto(Set<CompletedItem> items) {
        return items.stream().map(item -> new CartItemsDto(item.getItemCode(), item.getItemName(), item.getPrice(), item.getQuantity())).collect(Collectors.toSet());
    }

    public CompletedOrderDto toCompletedOrderDto(CompletedOrder completedOrder) {
        return new CompletedOrderDto(toOrderDto(completedOrder),
                toCartItemsDto(completedOrder.getCompletedItems()));
    }


}
