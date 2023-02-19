package service.seafood;

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
 * Implementation of the Seafood restaurant service.
 * 
 * @author Yuan
 *
 */
public class SeafoodService extends AbstractOrderService implements OrderService {
	private static String name = "Sea Lover";
	private static String license = "20822081234561";
	private static int openTime = 12;
	private static int closeTime = 20;
	private static String[] dishNames = new String[]{
			"Roast Joint Of The Day", "Roast Stuffed Chicken&Han","Traditional Irish Stew",
			"Golden Fried Fillet Of Place", "6oz Sirloin Steak", "Cottage Pie","Grilled Salmon"
	};
	private static double[] prices = new double[]{12.50, 10.50, 9.9, 11.50, 12.50, 10.50, 18.95};
	private static HashMap<Integer, Dish> menu = deviseMenu(new HashMap<>(), dishNames,prices);


	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		ActorRef ref = system.actorOf(Props.create(RestauActor.class), "restaurant01");
		ref.tell(new Init(new SeafoodService()), null); //Initialise the restaurant service actor
		ActorSelection selection =
				system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");

		RegistrationRequest brokerRegistration = new RegistrationRequest(
				new RestaurantInfo(name, license, openTime, closeTime, menu));
		selection.tell(brokerRegistration, ref);
	}

}