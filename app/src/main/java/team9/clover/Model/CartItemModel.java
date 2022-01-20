package team9.clover.Model;

import android.annotation.SuppressLint;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemModel {
    String id, image, title;
    Map<String, Long> choice;
    long price, total;

    public CartItemModel() { }

    public CartItemModel(String id, String title, String image, long price, String size, long quantity) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.choice = new HashMap<>();
        this.choice.put(size, quantity);
        this.total = price * quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Long> getChoice() {
        return choice;
    }

    public void setChoice(Map<String, Long> choice) {
        this.choice = choice;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @SuppressLint("DefaultLocale")
    public Pair<String, Long> takeSize() {
        List<String> arr = new ArrayList<>();
        long quantity = 0;

        for (Map.Entry<String, Long> entry : choice.entrySet()){
            arr.add(String.format("%s (x%d) ", entry.getKey(), entry.getValue()));
            quantity += entry.getValue();
        }

        return new Pair<String, Long>(String.join(", ", arr), quantity);
    }

    public void addCart(String size, long quantity) {
        total = 0;
        choice.put(size, choice.getOrDefault(size, (long) 0) + quantity);
        choice.forEach((key, value) -> total += (price * value));
    }
}
