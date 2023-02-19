package service.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import service.core.ClientInfo;
import service.messages.*;

import java.util.HashMap;
import java.util.LinkedList;

import service.core.RestaurantInfo;

/**
 * @author Yuanyuan Lu
 * @create on 2022-10-21-20:27
 */
public class Broker extends AbstractActor {
    private HashMap<String,ActorRef> actorRefs = new HashMap<>();
    private static int SEED_ID;
    private HashMap<Integer, ActorRef> orderMap = new HashMap<>();
    private HashMap<Integer, RestaurantInfo> allRestaurants = new HashMap<>();
    private static double profits = 0;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(RegistrationRequest.class, msg -> {
                allRestaurants.put(SEED_ID++, msg.getRestaurantInfo());
                actorRefs.put(msg.getRestaurantInfo().getLicenseNumber(), getSender());
            })
             .match(OrderResponseRtoB.class, msg -> {
                 System.out.println("response from restaurant...");
                 int orderId = msg.getId();
                 if(orderMap.containsKey(orderId)){
                    ActorRef clientRef = orderMap.get(orderId);
                    clientRef.tell(msg.getMessage(), getSelf());
                 }else
                     System.out.println("Order doesn't exist.");
             })

            .match(MenuRequest.class, msg -> {
                LinkedList<Integer> keys = new LinkedList();
                LinkedList<RestaurantInfo> values = new LinkedList();
                for (int eachKey : allRestaurants.keySet()) {
                    keys.add(eachKey);
                    values.add(allRestaurants.get(eachKey));
                }
                MenuResponse menuResponse = new MenuResponse(allRestaurants, keys, values); // Package array of RestaurantInfo objects into a MenuResponse message
                getSender().tell(menuResponse, null); // Send all restaurant infos to the client
            })
            .match(OrderRequestCtoB.class, msg -> {
                String selectedRest = msg.getClientOrder().getRestaurant().getLicenseNumber();
                ActorRef restaurantRef = actorRefs.get(selectedRest);
                orderMap.put(msg.getClientOrder().getOrderReference(), getSender());
                profits += msg.getBrokerCommission();
                msg.getClientOrder().setPrice(msg.getClientOrder().getPrice()-msg.getBrokerCommission());
                restaurantRef.tell(new OrderRequestBtoR(msg.getClientInfo(), msg.getClientOrder()), getSelf());//send order request to restaurant
                System.out.println("Current profits: €" + String.format("%.2f", profits));
            })
            .build();

    }
}
