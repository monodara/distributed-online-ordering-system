package service.messages;

import service.core.RestaurantInfo;
import java.util.HashMap;
import java.util.LinkedList;

public class MenuResponse implements MySerializable {
    public HashMap<Integer, RestaurantInfo> restaurantMenus;
    public LinkedList<Integer> keys;
    public LinkedList<RestaurantInfo> values;

    public MenuResponse(HashMap<Integer, RestaurantInfo> restaurantMenus, LinkedList<Integer> keys, LinkedList<RestaurantInfo> values) {
        this.restaurantMenus = restaurantMenus;
        this.keys = keys;
        this.values = values;
    }

    public MenuResponse() {
    }

    public HashMap<Integer, RestaurantInfo> getRestaurantMenus() {
        return restaurantMenus;
    }

    public void setRestaurantMenus(HashMap<Integer, RestaurantInfo> restaurantInfo) {
        this.restaurantMenus = restaurantMenus;
    }

    public LinkedList<Integer> getKeys() {
        return this.keys;
    }

    public void setKeys(LinkedList<Integer> keys) {
        this.keys = keys;
    }

    public LinkedList<RestaurantInfo> getValues() {
        return this.values;
    }

    public void setValues(LinkedList<RestaurantInfo> values) {
        this.values = values;
    }


}
