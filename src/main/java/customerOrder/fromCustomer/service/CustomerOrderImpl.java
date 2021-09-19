package customerOrder.fromCustomer.service;


import com.example.demo.dto.TypeInformationDto;
import customerOrder.fromCustomer.dao.CustomerRepository;
import customerOrder.fromCustomer.dao.ItemRepository;
import customerOrder.fromCustomer.dao.OrderRepository;
import customerOrder.fromCustomer.dto.CartItemsDto;
import customerOrder.fromCustomer.dto.CustomerDto;
import customerOrder.fromCustomer.dto.OrderDto;
import customerOrder.fromCustomer.model.Customer;
import customerOrder.fromCustomer.model.Item;
import customerOrder.fromCustomer.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerOrderImpl implements CustomerOrder {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public List<CustomerDto> getAllCustomersInformation() {
        return toCustomersDto(customerRepository.findAll());
    }

    @Transactional
    public List<OrderDto> findAllOrdersOfACustomer(String phoneNumber) {
        Customer customer = customerRepository.getCustomerByPhone(phoneNumber);
        return toOrderDto(customer.getOrderList());
    }

    @Transactional
    public Set<CartItemsDto> getItemsOfAOrder(String orderNumber) {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        return toItemsDto(order.getItems());
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
// change in date time
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

    @Transactional
    public void orderedReceiveFromCustomer(String customerPhone, CustomerDto customerInfo, OrderDto orderDto, Set<CartItemsDto> itemDtos) {
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
    }

    @Transactional
    public void updateQuantityOfItemsInAnOrder(String orderNumber, String itemCode, int quantity) {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        Set<Item> foundItems = order.getItems();
        for (Item item : foundItems) {
            if (item.getItemCode() == itemCode) {
                item.setQuantity(quantity);
            }
        }
    }

    @Transactional
    public void deleteAnItemOfOrder(String itemName){
        itemRepository.deleteItemByName(itemName);
    }

    @Transactional
    public void deleteOrder(String orderNumber) {
        Order order = orderRepository.getOrderByOrderNumber(orderNumber);
        order.removeItems();
        orderRepository.delete(order);

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

    public List<OrderDto> toOrderDto(List<Order> orders) {
        return orders.stream().map(order -> new OrderDto(order.getOrderNumber(), order.getTotalBill())).collect(Collectors.toList());
    }
}
