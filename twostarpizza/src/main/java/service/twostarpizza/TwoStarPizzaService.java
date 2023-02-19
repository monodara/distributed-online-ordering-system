package service.twostarpizza;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.RestauActor;
import service.core.*;
import service.message.Init;
import service.messages.RegistrationRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of the Two-Star Pizza restaurant service.
 * 
 * @author Yuan, Turlough
 *
 */
public class TwoStarPizzaService extends AbstractOrderService implements OrderService {
	private static String name = "2-Star Pizza";
	private static String license = "20758361583295";
	private static int openTime = 10;
	private static int closeTime = 23;
	private static String[] dishNames = new String[]{
			"Pepperoni Pizza", "Margherita Pizza","Hawaiian Pizza", "Chicken & BBQ Pizza", "White Pizza with mushrooms",
			"Veggie Pizza", "Meat Lovers' Pizza", "2-Star Special Pizza","Anchovy & Olive Pizza", "Garlic Bread", "Jalapeño Poppers", "Potato Wedges"
	};
	private static double[] prices = new double[]{11.00, 7.50, 9.50, 12.00, 14.00, 10.50, 12.50, 13.00, 14.00, 5.00, 6.50, 3.50};
	private static HashMap<Integer, Dish> menu = deviseMenu(new HashMap<>(), dishNames,prices);


	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		ActorRef ref = system.actorOf(Props.create(RestauActor.class), "pizzaRestaurant");
		ref.tell(new Init(new TwoStarPizzaService()), null); //Initialise the restaurant service actor
		ActorSelection selection =
				system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");

		RegistrationRequest brokerRegistration = new RegistrationRequest(
				new RestaurantInfo(name, license, openTime, closeTime, menu));
		selection.tell(brokerRegistration, ref);
	}

}