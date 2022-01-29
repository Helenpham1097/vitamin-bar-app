package com.vitaminBar.customerOrder.menuManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitaminBar.customerOrder.menuManagement.controller.request.Changes;
import com.vitaminBar.customerOrder.menuManagement.controller.request.MenuRequest;
import com.vitaminBar.customerOrder.menuManagement.dao.MenuRepository;
import com.vitaminBar.customerOrder.menuManagement.dao.TypeInformationRepository;
import com.vitaminBar.customerOrder.menuManagement.dto.MenuDto;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import com.vitaminBar.customerOrder.menuManagement.service.ServiceMenuImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebAppConfiguration
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ContextConfiguration()
class DemoControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    ServiceMenuImpl serviceMenu;

    @MockBean
    MenuRepository menuRepository;
    @MockBean
    TypeInformationRepository typeInformationRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
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
    void testingGetAllMenuByDetailBeverageInformation_returnListOfBeverageAndItsDetails() throws Exception {
        String uri = "/vitamin-bar";
        when(serviceMenu.getAllBeverages())
                .thenReturn(List.of(new MenuDto(
                        "Milk Tea",
                        List.of(new TypeInformationDto("MT01", "Chocolate Milk Tea",10.0)))));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status, 200);
        String content = mvcResult.getResponse().getContentAsString();
        MenuDto[] menus = mapFromJson(content,MenuDto[].class);
        Optional<MenuDto> expectedValue = Arrays.stream(menus).findFirst();
        assertThat(expectedValue.get().getTypeName()).isEqualTo("Milk Tea");
    }
    @Test
    void testingGetDetailsOfABeverageByName_returnExpectedValue_whenGivenABeverageName() throws Exception {
        String uri = "/vitamin-bar/get-details/Milk Tea";
        List<TypeInformationDto> beverageItems =
                List.of(new TypeInformationDto("MT01","Chocolate Milk Tea",12.0));
        when(serviceMenu.getAllItemsOfOneType("Milk Tea"))
                .thenReturn(beverageItems);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);

        String content = mvcResult.getResponse().getContentAsString();
        TypeInformationDto[] expectedValues = mapFromJson(content, TypeInformationDto[].class);
        Optional<TypeInformationDto> real = Arrays.stream(expectedValues).findFirst();
        assertThat(real.get().getName()).isEqualTo("Chocolate Milk Tea");
    }
    @Test
    void testingGetItemInformation_returnExpectedValue_whenGivenABeverage() throws Exception {
        String uri = "/vitamin-bar/Chocolate Milk Tea";
        TypeInformationDto beverageItem =
                new TypeInformationDto("MT01","Chocolate Milk Tea",12.0);
        when(serviceMenu.getItemInformation("Chocolate Milk Tea"))
                .thenReturn(beverageItem);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);

        String content = mvcResult.getResponse().getContentAsString();
        TypeInformationDto expectedValue = mapFromJson(content,TypeInformationDto.class);
        assertThat(expectedValue.getItemCode()).isEqualTo("MT01");
    }

    @Test
    void testingAddNewBeverage_returnExpectedMessage_whenAddingNewBeverage() throws Exception {
        String uri = "/vitamin-bar/add";
        MenuDto menuDto =  new MenuDto(
                "Milk tea", List.of(new TypeInformationDto("MT01", "Chocolate Milk Tea", 12.0)));
        String inputJson = mapToJson(menuDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status,200);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Added");
    }

    @Test
    void testingChangeMainBeverageName_returnExpectedValue() throws Exception {
        String uri = "/vitamin-bar/update-main-beverage-name";
        Changes changes = new Changes("Milk Tea", "Cheesy Tea");

        String inputJson = mapToJson(changes);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200,status);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo("updated");
    }

    @Test
    void testingChangeItemInformation_returnExpectedValue_whenGivenNewTypeInformation() throws Exception {
        String uri = "/vitamin-bar/change-information";
        MenuRequest menuRequest = new MenuRequest("Chocolate Milk Tea", new TypeInformationDto("MT03","Mango Milk Tea",10.0));
        String inputJson = mapToJson(menuRequest);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo("updated");
    }
    @Test
    void testingDeleteItem_returnedExpectedMessage_whenDeleteAnItem() throws Exception {
        String uri = "/vitamin-bar/delete-item/Chocolate Milk Tea";
        String inputJson = mapToJson("Chocolate Milk Tea");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Deleted");
    }

    @Test
    void testingDeleteAllOfABeverageInMenu_returnedExpectedMessage_whenDeleteAnItem() throws Exception{
        String uri = "/vitamin-bar/delete-all/Milk Tea";
        String inputJson = mapToJson("Milk Tea");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo("Deleted all");
    }
}