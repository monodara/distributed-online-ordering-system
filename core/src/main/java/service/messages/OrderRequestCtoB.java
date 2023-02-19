package service.messages;

import service.core.ClientInfo;
import service.core.Order;

// Note from Turlough - every Order item contains a ClientInfo, so it's redundant for OrderRequestCtoB to require a second one (I'm probably responsible for this mistake)

/**
 * @author Yuanyuan Lu
 * @create on 2022-10-21-9:49
 */
public class OrderRequestCtoB implements MySerializable{
    private ClientInfo clientInfo;
    private Order clientOrder;
    private double brokerCommission;
    public OrderRequestCtoB(ClientInfo clientInfo, Order clientOrder, double brokerCommission) {
        this.clientInfo = clientInfo;
        this.clientOrder = clientOrder;
        this.brokerCommission = brokerCommission;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public Order getClientOrder() {
        return this.clientOrder;
    }

    public void setClientOrder(Order clientOrder) {
        this.clientOrder = clientOrder;
    }

    public double getBrokerCommission() {
        return this.brokerCommission;
    }

    public void setBrokerCommission(double brokerCommission) {
        this.brokerCommission = brokerCommission;
    }

}
