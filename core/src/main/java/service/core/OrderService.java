package service.core;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface to define the behaviour of an order service.
 * 
 * @author Yuan
 *
 */
public interface OrderService  {
	default String checkIfInStock(Order clientOrder, HashMap<Integer, Dish> menu) {
		ArrayList<Dish> clientSelectDishes = clientOrder.getOrderItems();
		ArrayList<Dish> allDishesInMenu = new ArrayList<>(menu.values());
		boolean findInMenu = false;
		for(Dish dish: clientSelectDishes){
			for(Dish dishInMenu: allDishesInMenu){
				if(dish.getName().equals(dishInMenu.getName())){
					findInMenu = true;
					break;
				}
			}
			if(!findInMenu) return "Sorry, " + dish.getName() + " has sold out.";
			findInMenu = false;
		}
		return "Thank you! Your order will be ready for collection in 45 minutes.";
	}


	default void printOrder(Order order, ClientInfo clientInfo){
		RestaurantInfo restaurant = order.restaurant;
		int reference = order.getOrderReference();
		String clientName = clientInfo.getName();
		double price = order.price;
		ArrayList<Dish> dishes = order.orderItems;
		System.out.println("===== " + restaurant + " =====");
		System.out.println("Reference No. " + reference);
		System.out.println("Client: " + clientName);
		System.out.println("==============================");
		for(int i = 0; i < dishes.size(); i++){
			Dish dish = dishes.get(i);
			System.out.println(dish.getName() + "\t" + dish.getPrice());
		}
		System.out.println("------------------------------");
		System.out.println("Total Amount:" + "\t" + String.format("%.2f", price));
	}

}
