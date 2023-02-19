package service.core;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractOrderService implements OrderService {
	private static int counter = 0;
	protected static HashMap<Integer,Dish> deviseMenu(HashMap<Integer,Dish> menu, String[] dishNames, double[] prices) {
		for(int i = 0; i < dishNames.length; i++){
			menu.put(counter++, new Dish(dishNames[i],prices[i]));
		}
		return menu;
	}
	protected void addDishToMenu(HashMap<Integer,Dish> menu, String dishName, double price) {
		menu.put(counter++, new Dish(dishName,price));
	}
	protected double generatePrice(ArrayList<Integer> dishNumberList, HashMap<Integer,Dish> menu) {
		double total = 0d;
		for(int i: dishNumberList){
			total += menu.get(i).getPrice();
		}
		return total;
	}
	protected String generateReference(String prefix) {
		String ref = prefix;
		int length = 100000;
		while (length > 1000) {
			if (counter / length == 0) ref += "0";
			length = length / 10;
		}
		return ref + counter++;
	}
}
