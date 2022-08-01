package com.vitaminBar.customerOrder.menuManagement.controller;

import com.vitaminBar.customerOrder.menuManagement.controller.request.MenuRequest;
import com.vitaminBar.customerOrder.menuManagement.dto.MenuDto;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import com.vitaminBar.customerOrder.menuManagement.service.ServiceMenuImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/vitamin-bar")
//@CrossOrigin(origins = "http://localhost:63343")
public class DemoController {

    ServiceMenuImpl serviceMenu;

    @Autowired
    public void setServiceMenuImpl(ServiceMenuImpl serviceMenu) {
        this.serviceMenu = serviceMenu;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewBeverage(@RequestBody MenuRequest menuRequest) {
        serviceMenu.addNewBeverageToMenu(new MenuDto(menuRequest.getTypeName(), menuRequest.getList()));
        return ResponseEntity.ok("Added");
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PreAuthorize("hasAuthority('USER')")
//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/allMainBeverageName")
    public List<String> getAllMainBeverageNameOnly() {
        return serviceMenu.getAllMainBeveragesName();
    }

//        @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping()
    public List<MenuDto> getAllDetailMenu() {
        return serviceMenu.getAllBeverages();
    }

//        @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/get-details/{beverageType}")
    public List<TypeInformationDto> getDetailsBeverageByName(@PathVariable String beverageType) {
        return serviceMenu.getAllItemsOfOneType(beverageType);
    }

    @GetMapping("/{name}")
    public TypeInformationDto getItemInformation(@PathVariable String name) {
        return serviceMenu.getItemInformation(name);
    }

    @PutMapping("/update-main-beverage-name")
    public ResponseEntity<String> changeMainBeverageName(@RequestBody MenuRequest menuRequest) {
        serviceMenu.updateMainBeverage(menuRequest.getTypeName(), menuRequest.getNewTypeName());
        return ResponseEntity.ok("updated");
    }

    @PutMapping("/update-item-information")
    public ResponseEntity<String> changeItemInformation(@RequestBody MenuRequest menuRequest) {
        serviceMenu.updateAnItemInAType(menuRequest.getName(), menuRequest.getItem());
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("delete-item/{name}")
    public ResponseEntity<String> deleteItem(@PathVariable String name) {
        serviceMenu.deleteAnItem(name);
        return ResponseEntity.ok("Deleted");
    }

    @DeleteMapping("delete-all/{name}")
    public ResponseEntity<String> deleteAll(@PathVariable String name) {
        serviceMenu.deleteABeverageWithAllItems(name);
        return ResponseEntity.ok("Deleted all");
    }
}
