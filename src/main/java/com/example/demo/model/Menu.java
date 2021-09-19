package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table (name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    private String typeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "menu")
    List<TypeInformation> listBeverages = new ArrayList<>();

    public List<TypeInformation> getListBeverages() {
        return listBeverages;
    }

    public void setListBeverages(List<TypeInformation> listBeverages) {
        this.listBeverages = listBeverages;
    }

    //helper method to make parent and child entities become synchronization
    public void addBeverage(TypeInformation item){
        this.listBeverages.add(item);
        item.setMenu(this);
    }
    public void addBeverages(List<TypeInformation> items){
        this.listBeverages.addAll(items);
        items.forEach(item -> item.setMenu(this));
    }


    public void deleteBeverage(TypeInformation item){
        item.setMenu(null);
        this.listBeverages.remove(item);
    }

    public void removeBeverageItems(){
        Iterator<TypeInformation> items = this.listBeverages.iterator();
        while(items.hasNext()){
            TypeInformation specificBeverage = items.next();
            specificBeverage.setMenu(null);
            this.listBeverages.remove(specificBeverage);
        }
    }


}
