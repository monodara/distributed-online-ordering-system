package service.actor;

import akka.actor.AbstractActor;
import service.core.ClientInfo;
import service.core.Order;
import service.core.OrderService;
import service.core.RestaurantInfo;
import service.message.Init;
import service.messages.OrderRequestBtoR;
import service.messages.OrderResponseRtoB;

/**
 * @author Yuanyuan Lu
 * @create on 2022-10-21-9:51
 */
public class RestauActor extends AbstractActor {
    private OrderService service;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderRequestBtoR.class, msg -> {
                    Order order = msg.getClientOrder();
                    RestaurantInfo restaurantInfo = order.getRestaurant();
                    ClientInfo clientInfo = msg.getClientInfo();
                    service.printOrder(order, clientInfo);
                    String message = service.checkIfInStock(order, restaurantInfo.getMenu());
                    getSender().tell(
                            new OrderResponseRtoB(order.getOrderReference(), message), getSelf());
                })
                .match(Init.class,
                        msg -> {
                            service = msg.getOrderService();
                        })
                .build();
    }
}
