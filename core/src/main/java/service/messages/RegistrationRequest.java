package service.messages;

import service.core.RestaurantInfo;

public class RegistrationRequest implements MySerializable {
    private RestaurantInfo restaurantInfo;

    public RegistrationRequest(RestaurantInfo restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
    }

    public RegistrationRequest() {
    }

    public RestaurantInfo getRestaurantInfo() {
        return this.restaurantInfo;
    }

    public void setRestaurantInfo(RestaurantInfo restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
    }

}
