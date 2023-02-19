package service.thaistyle;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.RestauActor;
import service.core.*;
import service.message.Init;
import service.messages.RegistrationRequest;

import java.util.HashMap;

/**
 * Implementation of the Thailand restaurant service.
 * 
 * @author Yuan
 *
 */
public class ThaiFoodService extends AbstractOrderService implements OrderService {
	private static String name = "Thai Style";
	private static String license = "20822081237802";
	private static int openTime = 12;
	private static int closeTime = 22;
	private static String[] dishNames = new String[]{
		"Duck Spring Rolls", "Thai Spicy Chicken Wings","Honey Crumbed Prawns",
		"Prawn, Chicken or Veg Tom Kha Soup", "Phad Thai", "Duck Salad","Steamed Fish With Ginger Soya Sauce"
	};
	private static double[] prices = new double[]{9.95, 10.50, 12.90, 8.90, 21.95, 29.95, 23.90};
	private static HashMap<Integer, Dish> menu = deviseMenu(new HashMap<>(), dishNames,prices);


	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		ActorRef ref = system.actorOf(Props.create(RestauActor.class), "restaurant01");
		ref.tell(new Init(new ThaiFoodService()), null); //Initialise the restaurant service actor
		ActorSelection selection =
				system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");

		RegistrationRequest brokerRegistration = new RegistrationRequest(
				new RestaurantInfo(name, license, openTime, closeTime, menu));
		selection.tell(brokerRegistration, ref);
	}

}

