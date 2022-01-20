package team9.clover.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserModel {

    List<String> favorite;
    List<String> address;
    String fullName, phone, order, email;

    public UserModel() { }

    public UserModel(String fullName, String email) {
        // dùng để tạo new user trong database
        this.fullName = fullName;
        this.favorite = new ArrayList<>();
        this.address = new ArrayList<>();
        this.order = "";
        this.phone = "";
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<String> favorite) {
        this.favorite = favorite;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public void addFavorite(String productId) {
        favorite.add(productId);
    }

    public void removeFavorite(String productId) {
        favorite.remove(productId);
    }

    public void updateAddress(String city, String district, String village, String street) {
        address = new ArrayList<>(Arrays.asList(street, village, district, city));
    }
}
