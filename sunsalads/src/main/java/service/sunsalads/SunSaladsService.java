package service.sunsalads;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.RestauActor;
import service.core.AbstractOrderService;
import service.core.Dish;
import service.core.OrderService;
import service.core.RestaurantInfo;
import service.message.Init;
import service.messages.RegistrationRequest;

import java.util.HashMap;

public class SunSaladsService extends AbstractOrderService implements OrderService {
        private static String name = "Sun Salads";
        private static String license = "2082208123448";
        private static int openTime = 10;
        private static int closeTime = 18;
        private static String[] dishNames = new String[]{
                "Lettuce, avocado and mango salad", "Crunchy noodle salads","Thai beef salad",
                "Haloumi, pomegranate and rocket salad", "Caesar salad",
                "Creamy potato salad with herbs","Potato salad with Dijon mustard mayonnaise"
        };
        private static double[] prices = new double[]{12.90, 15.50, 17.90, 15.90, 10.95, 9.95, 13.90};
        private static HashMap<Integer, Dish> menu = deviseMenu(new HashMap<>(), dishNames,prices);


        public static void main(String[] args) {
            ActorSystem system = ActorSystem.create();
            ActorRef ref = system.actorOf(Props.create(RestauActor.class), "restaurant01");
            ref.tell(new Init(new SunSaladsService()), null); //Initialise the restaurant service actor
            ActorSelection selection =
                    system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");

            RegistrationRequest brokerRegistration = new RegistrationRequest(
                    new RestaurantInfo(name, license, openTime, closeTime, menu));
            selection.tell(brokerRegistration, ref);
        }

    }
