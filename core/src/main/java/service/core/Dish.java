package service.core;

/**
 * @author Yuanyuan Lu
 * @create on 2022-11-14-11:25
 */
public class Dish {
    public String name;
    private double price;

    public Dish() {
    }

    public Dish(String name, double price) {
        this.price = price;
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
