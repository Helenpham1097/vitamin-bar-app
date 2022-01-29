package com.vitaminBar.customerOrder.ordersManagement.service;

import com.vitaminBar.customerOrder.menuManagement.dao.TypeInformationRepository;
import com.vitaminBar.customerOrder.menuManagement.model.Menu;
import com.vitaminBar.customerOrder.menuManagement.model.TypeInformation;
import com.vitaminBar.customerOrder.ordersManagement.dto.CompletedOrderDto;
import com.vitaminBar.customerOrder.ordersManagement.model.*;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import com.vitaminBar.customerOrder.ordersManagement.dao.CustomerRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.ItemRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.OrderRepository;
import com.vitaminBar.customerOrder.ordersManagement.dto.CartItemsDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CustomerDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.OrderDto;
import com.vitaminBar.customerOrder.ordersManagement.exceptionHandler.CustomerNotFoundException;
import com.vitaminBar.customerOrder.ordersManagement.exceptionHandler.OrderNotFoundException;
import com.vitaminBar.customerOrder.ordersManagement.dao.CompletedItemRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.CompletedOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerOrderImplTest {

    private CustomerOrderImpl customerOrderService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TypeInformationRepository typeInformationRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CompletedOrderRepository completedOrderRepository;
    @Mock
    private CompletedItemRepository completedItemRepository;

    @BeforeEach
    void setUp() {
        this.customerOrderService = new CustomerOrderImpl(
                customerRepository, typeInformationRepository, orderRepository,
                itemRepository, completedOrderRepository, completedItemRepository);
    }

    @Test
    void testingGetAllCustomersInformation_returnExpectedCustomers_whenAListIsPresent() {
        when(customerRepository.findAll()).thenReturn(List.of(new Customer()));
        customerOrderService.getAllCustomersInformation();
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testingGetAllCustomersInformation_returnException_whenAListIsEmpty() {
        when(customerRepository.findAll()).thenReturn(List.of());
        assertThatThrownBy(() -> customerOrderService.getAllCustomersInformation())
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("No Customers");
    }

    @Test
    void testFindAllOrdersOfACustomer_returnExpectedOrderList_whenGivenExistingCustomer() {
        Customer customer = new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                List.of(new Order()),
                List.of(new CompletedOrder()));
        when(customerRepository.getCustomerByPhone(customer.getPhone())).thenReturn(customer);
        List<OrderDto> orders = customerOrderService.findAllOrdersOfACustomer(customer.getPhone());
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    void testFindAllOrdersOfACustomer_returnException_whenGivenNonExistingCustomer() {
        String phoneNumber = "0212971569";
        when(customerRepository.getCustomerByPhone(phoneNumber)).thenReturn(null);
        assertThatThrownBy(() -> customerOrderService.findAllOrdersOfACustomer(phoneNumber))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer with this phone number: " + phoneNumber + " is not valid");
    }

    @Test
    void testGetItemsOfAnOrder_returnListItem_whenGivenExistingOrderNumber() {
        Order order = new Order(1L, "THTR10030810",
                100.0, new Customer(), Timestamp.valueOf(LocalDateTime.now()), Set.of(new Item()));
        when(orderRepository.getOrderByOrderNumber(order.getOrderNumber())).thenReturn(order);
        Set<CartItemsDto> items = customerOrderService.getItemsOfAOrder(order.getOrderNumber());
        assertThat(items.size()).isEqualTo(1);
    }

    @Test
    void testGetItemsOfAnOrder_returnException_whenGivenNonExistingOrderNumber() {
        String orderNumber = "THTR10030810";
        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(null);
        assertThatThrownBy(() -> customerOrderService.getItemsOfAOrder(orderNumber))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("This order: " + orderNumber + " is not valid");
    }

    @Test
    void testingGetOrderedReceiveFromCustomer_returnExpectedValue_whenGivenOldCustomer() {
        String phoneNumber = "0212971569";
        List<Order> orders = new ArrayList<>();
        Customer customer = new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                orders,
                List.of(new CompletedOrder()));
        OrderDto orderDto = new OrderDto("THTR10030810", 50.0);

        Order order = new Order(1L, "THTR10030810",
                50.0, new Customer(), Timestamp.valueOf(LocalDateTime.now()), Set.of(new Item(1L, "MT01", "Chocolate Milk Tea", 50.0, 3, Set.of(new Order()))));
        Set<CartItemsDto> itemsDtos = Set.of(new CartItemsDto("MT01", "Chocolate Milk Tea", 50.0, 3));
        when(customerRepository.getCustomerByPhone(phoneNumber)).thenReturn(customer);
        when(orderRepository.getOrderByOrderNumber(orderDto.getOrderNumber())).thenReturn(order);
        when(typeInformationRepository.findSpecificItemByItsCode(any())).thenReturn(new TypeInformation(1L, "MT01", "Chocolate Milk Tea", 25.0, new Menu()));
        customerOrderService.getNewOrder(phoneNumber, new CustomerDto(), orderDto, itemsDtos);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        ArgumentCaptor<CompletedOrder> completedOrderArgumentCaptor = ArgumentCaptor.forClass(CompletedOrder.class);
        verify(completedOrderRepository).save(completedOrderArgumentCaptor.capture());
        assertThat(orderArgumentCaptor.getValue().getTotalBill()).isEqualTo(completedOrderArgumentCaptor.getValue().getTotalBill() - 25);
    }

    @Test
    void testingGetOrderedReceiveFromCustomer_returnExpectedValue_whenGivenNewCustomer() {
        String phoneNumber = "0212971569";
        CustomerDto customerDto = new CustomerDto(
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                phoneNumber,
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0);
        Set<Item> items = new HashSet<>();
        OrderDto orderDto = new OrderDto("THTR10030810", 100.0);
        Set<CartItemsDto> itemsDto = Set.of(new CartItemsDto("MT01", "Chocolate Milk Tea", 50.0, 2));
        Order order = new Order(1L, orderDto.getOrderNumber(),
                orderDto.getTotalBill(), new Customer(), Timestamp.valueOf(LocalDateTime.now()), items);

        when(customerRepository.getCustomerByPhone(phoneNumber)).thenReturn(null);
        when(orderRepository.getOrderByOrderNumber(orderDto.getOrderNumber())).thenReturn(order);
        customerOrderService.getNewOrder(phoneNumber, customerDto, orderDto, itemsDto);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        ArgumentCaptor<CompletedOrder> completedOrderArgumentCaptor = ArgumentCaptor.forClass(CompletedOrder.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        verify(completedOrderRepository).save(completedOrderArgumentCaptor.capture());
        String emailExpected = customerArgumentCaptor.getValue().getEmail();
        String completedOrderNumber = completedOrderArgumentCaptor.getValue().getOrderNumber();
        assertThat(emailExpected).isEqualTo(customerDto.getEmail());
        assertThat(completedOrderNumber).isEqualToIgnoringCase(orderDto.getOrderNumber());
    }

    @Test
    void testingUpdateQuantityOfItemsInAnOrder_returnExpectedValue_whenGivenExistingItem() {
        List<Order> customerOrders = new ArrayList<>();
        List<CompletedOrder> completedOrders = new ArrayList<>();
        Customer customer = new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                customerOrders,
                completedOrders);
        String orderNumber = "THTR10030810";
        String itemCode = "MT02";
        Set<Item> items = new HashSet<>();
        Set<Order> orders = new HashSet<>();
        Order order = new Order(1L, orderNumber, 44.0, customer, Timestamp.valueOf(LocalDateTime.now()), items);
        orders.add(order);
        items.add(new Item(1L, "MT01",
                "Chocolate Milk Tea", 20.0, 2, orders));
        items.add((new Item(2L, "MT02",
                "Brown Sugar Milk Tea", 24.0, 2, orders)));
        int newQuantity = 3;

        TypeInformation typeInformation = new TypeInformation(1L, "MT01", "Chocolate Milk Tea", 10.0, new Menu());


        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(order);
        when(typeInformationRepository.findSpecificItemByItsCode(any())).thenReturn(typeInformation);
        customerOrderService.updateQuantityOfItemsInAnOrder(orderNumber, itemCode, newQuantity);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        ArgumentCaptor<CompletedOrder> completedOrderArgumentCaptor = ArgumentCaptor.forClass(CompletedOrder.class);
        verify(completedOrderRepository).save(completedOrderArgumentCaptor.capture());
        double expectedNewTotalBill = orderArgumentCaptor.getValue().getTotalBill();
        assertThat(expectedNewTotalBill).isEqualTo(56);
        assertThat(completedOrderArgumentCaptor.getValue().getOrderNumber()).isEqualToIgnoringCase(orderNumber);
    }

    @Test
    void testingDeleteAnItemOfOrder_returnExpectedNewValueOfOrder_whenGivenExistingOrderNumber() {
        String orderNumber = "THTR10030810";
        String itemCode = "MT02";
        Set<Item> items = new HashSet<>();
        Set<Order> orders = new HashSet<>();
        Set<CompletedItem> completedItems = new HashSet<>();
        Set<CompletedOrder> completedOrders = new HashSet<>();
        Order order = new Order(1L, orderNumber, 44.0, new Customer(), Timestamp.valueOf(LocalDateTime.now()), items);
        orders.add(order);
        items.add(new Item(1L, "MT01",
                "Chocolate Milk Tea", 20.0, 2, orders));
        items.add((new Item(2L, "MT02",
                "Brown Sugar Milk Tea", 24.0, 2, orders)));
        CompletedOrder completedOrder = new CompletedOrder(1L,
                orderNumber, 44, Timestamp.valueOf(LocalDateTime.now()), new Customer(), completedItems);
        completedOrders.add(completedOrder);
        completedItems.add(new CompletedItem(1L, "MT01",
                "Chocolate Milk Tea", 20.0, 2, completedOrders));
        completedItems.add(new CompletedItem(2L, "MT02",
                "Brown Sugar Milk Tea", 24.0, 2, completedOrders));
        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(order);
        when(completedOrderRepository.getCompletedOrder(orderNumber)).thenReturn(completedOrder);
        customerOrderService.deleteAnItemOfOrder(orderNumber, itemCode);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        ArgumentCaptor<CompletedOrder> completedOrderArgumentCaptor = ArgumentCaptor.forClass(CompletedOrder.class);
        verify(completedOrderRepository).save(completedOrderArgumentCaptor.capture());

        Order expectedOrder = orderArgumentCaptor.getValue();
        Double newToTalBillAfterDeleteAnItem = expectedOrder.getTotalBill();
        Double expectedValue = 20.0;
        assertThat(newToTalBillAfterDeleteAnItem).isEqualTo(expectedValue);
        assertThat(completedOrderArgumentCaptor.getValue().getTotalBill()).isEqualTo(expectedValue);
    }

    @Test
    void testingDeleteAnItemOfOrder_returnException_whenGivenNonExistingOrderNumber() {
        String orderNumber = "THTR10030810";

        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(null);

        assertThatThrownBy(() -> customerOrderService.deleteAnItemOfOrder(orderNumber, anyString()))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("This order " + orderNumber + " is not found");
    }

    @Test
    void testingDeleteOrder_returnExpectedValue_whenGivenExistingOrderNumber() {
        String orderNumber = "THTR10030810";
        Set<Item> items = new HashSet<>();
        Order order = new Order(1L, orderNumber, 44.0, new Customer(), Timestamp.valueOf(LocalDateTime.now()), items);
        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(order);
        customerOrderService.deleteOrder(orderNumber);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testingDeleteOrder_returnException_whenGivenExistingOrderNumber() {
        String orderNumber = "THTR10030810";
        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(null);
        assertThatThrownBy(() -> customerOrderService.deleteOrder(orderNumber))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order number: " + orderNumber + " is not found");
    }

    @Test
    void testingAddNewCompletedOrder_returnExpectedValue_whenGivenOrderNumber() {
        String orderNumber = "THTR10030810";
        Order order = new Order(1L, orderNumber, 44.0, new Customer(), Timestamp.valueOf(LocalDateTime.now()), Set.of(new Item(1L, "MT01",
                "Chocolate Milk Tea", 20.0, 2, Set.of(new Order()))));
        TypeInformation typeInformation = new TypeInformation(1L, "MT01", "Chocolate Milk Tea", 10.0, new Menu());
        when(orderRepository.getOrderByOrderNumber(orderNumber)).thenReturn(order);
        when(typeInformationRepository.findSpecificItemByItsCode(any())).thenReturn(typeInformation);
        customerOrderService.addNewCompletedOrder(orderNumber);
        ArgumentCaptor<CompletedOrder> completedOrderDtoArgumentCaptor = ArgumentCaptor.forClass(CompletedOrder.class);
        verify(completedOrderRepository).save(completedOrderDtoArgumentCaptor.capture());
        assertThat(completedOrderDtoArgumentCaptor.getValue().getTotalBill()).isEqualTo(20.0);
    }

    @Test
    void testingFindACompletedOrder_returnExpectedValue_whenGivenOrderNumber(){
        String orderNumber = "THTR10030810";
        CompletedOrder completedOrder = new CompletedOrder(1L,
                orderNumber, 44, Timestamp.valueOf(LocalDateTime.now()), new Customer(), Set.of( new CompletedItem()));
        when(completedOrderRepository.getCompletedOrder(orderNumber)).thenReturn(completedOrder);
        CompletedOrderDto completedOrderDto = customerOrderService.findCompletedOrderByOrderNumber(orderNumber);
        assertThat(completedOrderDto.getOrderDto().getOrderNumber()).isEqualTo(orderNumber);
    }

    @Test
    void testingDeleteAnItemOfCompletedOrder_returnExpectValue_whenDeletingAnItem(){
        String orderNumber = "THTR10030810";
        String itemCode = "MT01";
        Set<CompletedItem> completedItems = new HashSet<>();
        Set<CompletedOrder> completedOrders = new HashSet<>();
        CompletedOrder completedOrder = new CompletedOrder(1L,
                orderNumber, 44, Timestamp.valueOf(LocalDateTime.now()), new Customer(), completedItems);
        completedOrders.add(completedOrder);
        completedItems.add(new CompletedItem(1L, "MT01",
                "Chocolate Milk Tea", 20.0, 2, completedOrders));
        completedItems.add(new CompletedItem(2L, "MT02",
                "Brown Sugar Milk Tea", 24.0, 2, completedOrders));
        when(completedOrderRepository.getCompletedOrder(orderNumber)).thenReturn(completedOrder);
        customerOrderService.deleteAnItemOfCompletedOrder(orderNumber, itemCode);
        ArgumentCaptor<CompletedOrder> completedOrderArgumentCaptor = ArgumentCaptor.forClass(CompletedOrder.class);
        verify(completedOrderRepository).save(completedOrderArgumentCaptor.capture());
        assertThat(completedOrderArgumentCaptor.getValue().getTotalBill()).isEqualTo(24.0);
    }

    @Test
    void testingDeleteACompletedOrder_returnExpectedMessage_whenGivenOrderNumber(){
        String orderNumber = "THTR10030810";
        when(completedOrderRepository.getCompletedOrder(orderNumber)).thenReturn(new CompletedOrder());
        customerOrderService.deleteCompletedOrder(orderNumber);
        verify(completedOrderRepository,times(1)).deleteCompletedOrder(orderNumber);
    }


    @Test
    void testingToItemsDto_returnExpectedDtoValue_whenGivenAnItem() {
        Set<Item> items = Set.of(new Item(1L, "MT01",
                "Chocolate Milk Tea", 20.0, 2, Set.of(new Order())));
        Set<CartItemsDto> itemsDto = customerOrderService.toItemsDto(items);
        assertThat(items.size()).isEqualTo(itemsDto.size());
    }

    @Test
    void testingToItemOfCartDto_returnExpectedValue_whenGivenItemInformationAndQuantity() {
        TypeInformationDto typeInformation = new TypeInformationDto("MT01", "Chocolate Milk Tea", 10.0);
        int quantity = 2;
        CartItemsDto itemDto = customerOrderService.toItemOfCartDto(typeInformation, quantity);
        assertThat(itemDto.getPrice()).isEqualTo(20.0);
    }

    @Test
    void toCustomerDto() {
        Customer customer = new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                List.of(new Order()),
                List.of(new CompletedOrder()));
        CustomerDto customerDto = customerOrderService.toCustomerDto(customer);
        assertThat(customer.getPhone()).isEqualTo(customerDto.getPhone());
    }

    @Test
    void testingToCustomersDto_returnExpectedValue_whenGivenCustomerList() {
        List<Customer> customers = List.of(new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                List.of(new Order()),
                List.of(new CompletedOrder())));
        List<CustomerDto> customersDto = customerOrderService.toCustomersDto(customers);
        assertThat(customersDto.size()).isEqualTo(customers.size());
    }

    @Test
    void testingToOrderDto_returnExpectedValue_whenGivenOrder() {
        List<Order> orders = List.of(new Order(1L, "THTR10030810", 44.0, new Customer(), Timestamp.valueOf(LocalDateTime.now()), Set.of(new Item())));
        List<OrderDto> ordersDto = customerOrderService.toOrdersDto(orders);
        assertThat(orders.size()).isEqualTo(ordersDto.size());
    }
}