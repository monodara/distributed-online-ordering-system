package service.broker;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Broker;
import service.core.ClientInfo;

import java.util.LinkedList;
import java.util.List;

public class LocalBrokerService {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		ActorRef ref = system.actorOf(Props.create(Broker.class), "broker");

	}
}
