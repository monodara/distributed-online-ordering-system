package service.messages;

import service.core.ClientInfo;
import service.core.Order;

/**
 * @author Yuanyuan Lu
 * @create on 2022-12-26-20:47
 */
public class OrderRequestBtoR implements MySerializable{
    private ClientInfo clientInfo;
    private Order clientOrder;
    public OrderRequestBtoR(ClientInfo clientInfo, Order clientOrder) {
        this.clientInfo = clientInfo;
        this.clientOrder = clientOrder;
    }

    public Order getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(Order clientOrder) {
        this.clientOrder = clientOrder;
    }


    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
