package service.messages;

import service.core.ClientInfo;

public class MenuRequest implements MySerializable {
    public ClientInfo client;
    public MenuRequest() {
    }

    public ClientInfo getClientInfo() {
        return client;
    }

    public void setClientInfo(ClientInfo client) {
        this.client = client;
    }

    public MenuRequest(ClientInfo client) {
        this.client = client;
    }
}
