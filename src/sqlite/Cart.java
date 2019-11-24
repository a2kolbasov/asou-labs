package sqlite;

import java.util.Arrays;

class Cart {
    private int Id;
    private String Name;
    private int[] Items;
    private int Price;
    private String Status;

    @Override
    public String toString() {
        return "Id:" + getId() + ", Name:" + getName() + ", Items:" + Arrays.toString(getItems()) +
                ", Price:" + getPrice() + ", Status:" + getStatus();
    }

    Cart(Integer id, String name, int[] items, Integer price, String status) {
        Id = id;
        Name = name;
        Items = items;
        Price = price;
        Status = status;
    }

    Integer getId() {
        return Id;
    }

    String getName() {
        return Name;
    }

    int[] getItems() {
        return Items;
    }

    Integer getPrice() {
        return Price;
    }

    String getStatus() {
        return Status;
    }
}
