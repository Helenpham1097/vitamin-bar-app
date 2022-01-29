package com.vitaminBar.customerOrder.ordersManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitaminBar.customerOrder.ordersManagement.controller.request.OrderRequest;
import com.vitaminBar.customerOrder.ordersManagement.dto.CartItemsDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CompletedOrderDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CustomerDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.OrderDto;
import com.vitaminBar.customerOrder.ordersManagement.service.CustomerOrderService;
import com.vitaminBar.customerOrder.ordersManagement.service.ItemChanges;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebAppConfiguration
//@ContextConfiguration alternative solution, custom @Configuration for test
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private CustomerOrderService service;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo(print()).build();
    }
    @Test
    public void givenWac_whenServletContext_thenItProvideController(){
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(webApplicationContext.getBean("CustomerOrder"));
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    void testingInsertNewOrder_returnExpectedValue_whenGivenNewOrder() throws Exception {
        String uri = "/store-order/add";
        String phone = "0212971569";
        CustomerDto customerDto = new CustomerDto("Helen", Date.valueOf("1997-10-10"),"0212971569","helenpham1097@gmail.com","29 Corricale Way",10 );
        OrderDto orderDto = new OrderDto("THTR10030810",100.0);
        Set<CartItemsDto> cartItemsDto = Set.of(new CartItemsDto("MT01","Milk Tea", 100.0,5));
        OrderRequest orderRequest = new OrderRequest(phone, customerDto,
                orderDto, cartItemsDto);
        String inputJson = mapToJson(orderRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status,200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualToIgnoringCase("added");
    }

    @Test
    void testingGetAllCustomer_returnExpectedCustomerList() throws Exception {
        String uri = "/store-order/customers";
        when(service.getAllCustomersInformation()).thenReturn(List.of(new CustomerDto(
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        CustomerDto[] expectedValue = mapFromJson(content,CustomerDto[].class);
        Optional<CustomerDto> real = Arrays.stream(expectedValue).findFirst();
        real.ifPresent(customerDto -> assertThat(customerDto.getPhone()).isEqualTo("0212971569"));
    }

    @Test
    void testingGetOrdersOfACustomer_returnListOfOrder_whenGivenAPhoneNumber() throws Exception {
        String uri = "/store-order/customer/0212971569";
        String phoneNumber = "0212971569";
        List<OrderDto> orders = List.of(new OrderDto( "THTR10030810", 100.0));
        when(service.findAllOrdersOfACustomer(phoneNumber)).thenReturn(orders);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        OrderDto[] foundOrders = mapFromJson(content,OrderDto[].class);
        Optional<OrderDto> expectedValues = Arrays.stream(foundOrders).findFirst();
        expectedValues.ifPresent(orderDto -> assertThat(orderDto.getOrderNumber()).isEqualTo("THTR10030810"));
    }

    @Test
    void testingGetItemsOfAnOrder_returnListOfItems_whenGivenAnOrderNumber() throws Exception {
        String uri = "/store-order/order/THTR10030810";
        String orderNumber = "THTR10030810";
        when(service.getItemsOfAOrder(orderNumber))
                .thenReturn(Set.of(new CartItemsDto("MT01","Chocolate Milk Tea",20.0,2)));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        CartItemsDto[] foundOrders = mapFromJson(content,CartItemsDto[].class);
        Optional<CartItemsDto> expectedValues = Arrays.stream(foundOrders).findFirst();
        expectedValues.ifPresent(cartItemsDto -> assertThat(cartItemsDto.getPrice()).isEqualTo(20.0));
    }

    @Test
    void testingUpdateItemsInCart_returnExpectedNewUpdateValue_whenGivenExistingItemWithNewUpdate() throws Exception {
        String uri = "/store-order/update-item";
        ItemChanges itemChanges = new ItemChanges("THTR10030810","MT02",3);

        String inputJson = mapToJson(itemChanges);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualToIgnoringCase("updated");
    }

    @Test
    void testingDeleteItemOfAnOrder_returnExpectedMessage() throws Exception {
        String uri = "/store-order/delete-item/MT02";
        ItemChanges delete = new ItemChanges("THTR10030810","MT01");
        String inputJson = mapToJson(delete);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualToIgnoringCase("Delete Item Successfully");
    }

    @Test
    void testingDeleteOrder_returnExpectedMessage() throws Exception {
        String uri = "/store-order/delete-order/THTR10030810";

        String inputJson = mapToJson("THTR10030810");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualToIgnoringCase("Delete Order Successfully");
    }

    @Test
    void testingAddCompletedOrder_returnExpectedValue_whenGivenCompletedOrder() throws Exception {
        String uri = "/store-order/process-order";
        String orderNumber = "THTR10030810";
        String inputJson = mapToJson(orderNumber);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualToIgnoringCase("done");
    }

    @Test
    public void testingGetCompletedOrder_returnExpectedOrder() throws Exception {
        String uri ="/store-order/get-confirmed-order/THTR10030810";
        String orderNumber = "THTR10030810";
        CompletedOrderDto completedOrderDto = new CompletedOrderDto(
                new OrderDto("THTR10030810",100.0),
                Set.of(new CartItemsDto("MT01","Chocolate Milk Tea",100.0,5)));

        when(service.findCompletedOrderByOrderNumber(orderNumber)).thenReturn(completedOrderDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        CompletedOrderDto expectedValue = mapFromJson(content, CompletedOrderDto.class);
        assertThat(expectedValue.getOrderDto().getTotalBill()).isEqualTo(100.0);
    }

    @Test
    void DeletedCompletedOrder_returnExpectedMessage() throws Exception {
        String uri = "/store-order/THTR10030810";
        String orderNumber = "THTR10030810";
        String inputJson = mapToJson(orderNumber);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualToIgnoringCase("deleted");
    }
}