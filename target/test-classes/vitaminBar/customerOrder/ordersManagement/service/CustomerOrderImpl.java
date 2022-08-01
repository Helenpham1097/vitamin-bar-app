package com.vitaminBar.customerOrder.ordersManagement.service;

import com.vitaminBar.customerOrder.menuManagement.dao.TypeInformationRepository;
import com.vitaminBar.customerOrder.menuManagement.model.TypeInformation;
import com.vitaminBar.customerOrder.ordersManagement.dto.CompletedOrderDto;
import com.vitaminBar.customerOrder.ordersManagement.exceptionHandler.*;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import com.vitaminBar.customerOrder.ordersManagement.dao.CustomerRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.ItemRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.OrderRepository;
import com.vitaminBar.customerOrder.ordersManagement.dto.CartItemsDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CustomerDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.OrderDto;
import com.vitaminBar.customerOrder.ordersManagement.model.*;
import com.vitaminBar.customerOrder.ordersManagement.dao.CompletedItemRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.CompletedOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerOrderImpl implements CustomerOrderService {

    private final CustomerRepository customerRepository;
    private final TypeInformationRepository typeInformationRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CompletedOrderRepository completedOrderRepository;
    private final CompletedItemRepository completedItemRepository;


    @Autowired
    public CustomerOrderImpl(CustomerRepository customerRepository,
                             TypeInformationRepository typeInformationRepository,
                             OrderRepository orderRepository,
                             ItemRepository itemRepository,
                             CompletedOrderRepository completedOrderRepository,
                             CompletedItemRepository completedItemRepository) {
        this.customerRepository = customerRepository;
        this.typeInformationRepository = typeInformationRepository;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.completedOrderRepository = completedOrderRepository;
        this.completedItemRepository = completedItemRepository;
    }

    @Transactional
    public List<CustomerDto> getAllCustomersInformation() throws CustomerNotFoundException {
        List<CustomerDto> customers = toCustomersDto(customerRepository.findAll());
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("No Customers");
        }
        return customers;
    }

    @Transactional
    public List<OrderDto> findAllOrdersOfACustomer(String phoneNumber) throws CustomerNotFoundException {
        Customer customer = customerRepository.getCustomerByPhone(phoneNumber);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with this phone number: " + phoneNumber + " is not valid");
        }
        return toOrdersDto(customer.getOrderList());
    }

    @Transactional
    public Set<CartItemsDto> getItemsOfAOrder(String orderNumber) throws OrderNotFoundException {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException("This order: " + orderNumber + " is not valid");
        }
        return toItemsDto(order.getItems());
    }

    @Transactional
    public void getNewOrder(String customerPhone, CustomerDto customerInfo, OrderDto orderDto, Set<CartItemsDto> itemDtos) {
        System.out.println(customerPhone);
        Customer foundCustomer = customerRepository.getCustomerByPhone(customerPhone);

        if (foundCustomer == null) {
            foundCustomer = toCustomer(customerInfo);
            customerRepository.save(foundCustomer);
        }
        Order order = toOrder(orderDto);

        foundCustomer.addOrder(order);
        Set<Item> items = itemDtos
                .stream()
                .map(this::toItem)
                .collect(Collectors.toSet());
        order.addItems(items);
        orderRepository.save(order);
        addNewCompletedOrder(orderDto.getOrderNumber());
    }

    @Transactional
    public void updateQuantityOfItemsInAnOrder(String orderNumber, String itemCode, int quantity) throws OrderNotFoundException {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException("This order: " + orderNumber + " is not available");
        }
        Set<Item> foundItems = order.getItems();
        for (Item item : foundItems) {
            if (Objects.equals(item.getItemCode(), itemCode)) {
                double pricePerUnit = item.getPrice() / item.getQuantity();
                item.setQuantity(quantity);
                item.setPrice(pricePerUnit * quantity);
                itemRepository.save(item);
            }
        }
        double total = billCalculator(order);
        order.setTotalBill(total);
        orderRepository.save(order);
        addNewCompletedOrder(orderNumber);
    }

    public double billCalculator(Order order) {
        Set<Double> itemsPrice = order.getItems().stream()
                .map(Item::getPrice)
                .collect(Collectors.toSet());
        return itemsPrice.stream().reduce(0.0, Double::sum);
    }

    @Transactional
    public void deleteAnItemOfOrder(String orderNumber, String itemCode) throws OrderNotFoundException, ItemNotFoundException {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException("This order " + orderNumber + " is not found");
        }
        Set<Item> items = order.getItems();
        Optional <Item> foundItem = items.stream()
                .filter(item -> Objects.equals(item.getItemCode(), itemCode))
                .findAny();
        if(foundItem==null){
            throw new ItemNotFoundException("This item is not found");
        }
        order.removeItem(foundItem.get());
        double newToTalBill = billCalculator(order);
        order.setTotalBill(newToTalBill);
        orderRepository.save(order);
        deleteAnItemOfCompletedOrder(orderNumber,itemCode);
    }

    @Transactional
    public void deleteOrder(String orderNumber) throws OrderNotFoundException {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException("Order number: " + orderNumber + " is not found");
        }
        order.removeItems();
        orderRepository.delete(order);
    }

    @Transactional
    public void addNewCompletedOrder(String orderNumber) throws OrderNotFoundException{
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        if(order == null){
            throw new OrderNotFoundException("This order is not found");
        }
        Customer customer = order.getCustomer();
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
    public CompletedOrderDto findCompletedOrderByOrderNumber(String orderNumber) {
        CompletedOrder completedOrder = completedOrderRepository.getCompletedOrder(orderNumber);
        return toCompletedOrderDto(completedOrder);
    }


    @Transactional
    public void deleteCompletedOrder(String orderNumber) throws CompletedOrderNotFoundException{
        CompletedOrder completedOrder = completedOrderRepository.getCompletedOrder(orderNumber);
        if(completedOrder == null){
            throw new CompletedOrderNotFoundException("Completed order is not found");
        }
        completedOrder.removeCompletedItems(completedOrder.getCompletedItems());
        completedOrderRepository.deleteCompletedOrder(orderNumber);
    }

    @Transactional
    public void deleteAnItemOfCompletedOrder(String orderNumber, String itemCode) throws CompletedOrderNotFoundException, CompletedItemNotFoundException{
        CompletedOrder completedOrder = completedOrderRepository.getCompletedOrder(orderNumber);
        if(completedOrder == null){
            throw new CompletedOrderNotFoundException("This order is not found");
        }
        Set<CompletedItem> completedItems = completedOrder.getCompletedItems();
        Optional<CompletedItem> deleteItem = completedItems.stream()
                .filter(item -> item.getItemCode().equals(itemCode))
                .findAny();
        if(deleteItem.isEmpty()){
            throw new CompletedItemNotFoundException("This item is not found");
        }
        completedOrder.removeCompletedItem(deleteItem.get());
        double newTotalPay = completedItems.stream()
                .map(CompletedItem::getPrice)
                .reduce(0.0,Double::sum);
        completedOrder.setTotalBill(newTotalPay);
        completedOrderRepository.save(completedOrder);
    }

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

    private Customer toCustomer(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setDeliveryAddress(dto.getDeliveryAddress());
        customer.setPoint(dto.getPoint());
        return customer;
    }
    private Order toOrder(OrderDto dto) {
        Order order = new Order();
        order.setOrderDate();
        order.setOrderNumber(dto.getOrderNumber());
        order.setTotalBill(dto.getTotalBill());
        return order;
    }

    private Item toItem(CartItemsDto dto) {
        Item newItem = new Item();
        newItem.setItemCode(dto.getItemCode());
        newItem.setItemName(dto.getItemName());
        newItem.setQuantity(dto.getQuantity());
        newItem.setPrice(dto.getPrice());

        return newItem;
    }

    public Set<CartItemsDto> toItemsDto(Set<Item> items) {
        return items.stream().map(item -> new CartItemsDto(item.getItemCode(), item.getItemName(), item.getPrice(), item.getQuantity())).collect(Collectors.toSet());
    }

    public CartItemsDto toItemOfCartDto(TypeInformationDto item, int quantity) {
        double totalPrice = item.getPrice() * quantity;
        return new CartItemsDto(item.getItemCode(), item.getName(), totalPrice, quantity);
    }

    public CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getName(), customer.getDateOfBirth(), customer.getPhone(),
                customer.getEmail(), customer.getDeliveryAddress(), customer.getPoint());
    }

    public List<CustomerDto> toCustomersDto(List<Customer> customers) {
        return customers.stream().map(customer -> new CustomerDto(customer.getName(), customer.getDateOfBirth(), customer.getPhone(),
                customer.getEmail(), customer.getDeliveryAddress(), customer.getPoint())).collect(Collectors.toList());
    }

    public List<OrderDto> toOrdersDto(List<Order> orders) {
        return orders.stream().map(order -> new OrderDto(order.getOrderNumber(), order.getTotalBill())).collect(Collectors.toList());
    }
}
