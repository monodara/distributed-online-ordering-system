package service.core;

import java.util.ArrayList;

import service.core.Dish;
import service.core.RestaurantInfo;
import service.core.ClientInfo;

/**
 * Class to store the quotations returned by the quotation services
 * 
 * @author Yuan
 *
 */
public class Order {
	public Order(RestaurantInfo restaurant, double price) {
		this.restaurant = restaurant;
		this.price = price;
		this.orderItems = new ArrayList<Dish>();
		orderReference++;
	}
	public Order(){}
	public RestaurantInfo restaurant;
	public double price;
	public ArrayList<Dish> orderItems;
	public static int orderReference = 1000000000;

	public RestaurantInfo getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantInfo restaurant) {
		this.restaurant = restaurant;
	}

//	public ClientInfo getClient() {
//		return client;
//	}
//
//	public void setClient(ClientInfo client) {
//		this.client = client;
//	}

	public static int getOrderReference() {
		return orderReference;
	}

	public static void setOrderReference(int orderReference) {
		Order.orderReference = orderReference;
	}

	public void addToOrder(Dish menuItem) {
		this.orderItems.add(menuItem);
		this.addDishToPrice(menuItem);
	}

	public void removeFromOrder(Dish menuItem) {
		this.orderItems.remove(menuItem);
		this.subtractDishFromPrice(menuItem);
	}

	public ArrayList<Dish> getOrderItems() {
		return this.orderItems;
	}

	public void addDishToPrice(Dish menuItem) {
		this.setPrice(this.getPrice()+menuItem.getPrice());
	}

	public void subtractDishFromPrice(Dish menuItem) {
		this.setPrice(this.getPrice() - menuItem.getPrice());
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	


}
