package com.example.demo.controller;

import com.example.demo.controller.request.MenuRequest;
import com.example.demo.dto.MenuDto;
import com.example.demo.dto.TypeInformationDto;
import com.example.demo.service.ServiceMenuImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vitamin-bar")
public class DemoController {

    @Autowired
    ServiceMenuImpl serviceMenu;

    @Autowired
    public void setServiceMenuImpl(ServiceMenuImpl serviceMenu) {
        this.serviceMenu = serviceMenu;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewBeverage(@RequestBody MenuRequest menuRequest) {
        serviceMenu.addNewBeverageToMenu(menuRequest.getTypeName(),menuRequest.getList());
        return ResponseEntity.ok("Added");
    }
    @GetMapping()
    public List<MenuDto> getAllDetailMenu() {
        return serviceMenu.getAllBeverages();
    }

    @GetMapping("/get-details/{beverageType}")
    public List<TypeInformationDto> getDetailsBeverageByName(@PathVariable String beverageType){
        return serviceMenu.getAllItemsOfOneType(beverageType);
    }

    @GetMapping("/{name}")
    public TypeInformationDto getItemInformation(@PathVariable String name){
        return serviceMenu.getItemInformation(name);
    }
    @PostMapping("/update-main-beverage-name")
    public ResponseEntity<String> changeMainBeverageName(@RequestBody MenuRequest menuRequest){
        serviceMenu.updateMainBeverage(menuRequest.getTypeName(), menuRequest.getNewTypeName());
        return ResponseEntity.ok("updated");
    }
    @PostMapping("/change-information")
    public ResponseEntity<String> changeItemInformation(@RequestBody MenuRequest menuRequest){
        serviceMenu.updateAnItemInAType(menuRequest.getName(), menuRequest.getItem());
        return ResponseEntity.ok("updated");
    }
    @DeleteMapping("delete-item/{name}")
    public ResponseEntity<String> deleteItem(@PathVariable String name){
        serviceMenu.deleteAnItem(name);
        return ResponseEntity.ok("Deleted");
    }
    @DeleteMapping("delete-all/{name}")
    public ResponseEntity<String> deleteAll(@PathVariable String name){
        serviceMenu.deleteABeverageWithAllItems(name);
        return ResponseEntity.ok("Deleted all");
    }
}
