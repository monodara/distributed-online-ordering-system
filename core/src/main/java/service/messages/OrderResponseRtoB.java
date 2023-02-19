package service.messages;

import service.core.Order;

/**
 * @author Yuanyuan Lu
 * @create on 2022-10-21-9:49
 */
public class OrderResponseRtoB implements MySerializable{
    private int id;
    private String message;
    public OrderResponseRtoB(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

