package service.message;

import akka.actor.ActorRef;
import service.core.OrderService;

/**
 * @author Yuanyuan Lu
 * @create on 2022-10-21-10:00
 */
public class Init {
    private ActorRef ref;
    private OrderService service;
    public Init(ActorRef ref, OrderService service){
        this.ref = ref;
        this.service = service;
    }

    public Init(OrderService service) {
        this.service = service;
    }

    public OrderService getOrderService(){
        return service;
    }
}
