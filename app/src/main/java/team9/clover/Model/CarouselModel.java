package team9.clover.Model;

public class CarouselModel {

    public static final String FIRESTORAGE = "Carousel";

    String image, padding;

    public CarouselModel() {}

    public CarouselModel(String image, String padding) {
        this.image = image;
        this.padding = padding;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }
}
