package sqlite;

import com.google.gson.Gson;

class Json {
    private static Gson gson = new Gson();

    static String toJson(Cart cart) {
        return gson.toJson(cart);
    }

    static Cart fromJson(String json) {
        return gson.fromJson(json, Cart.class);
    }
}
