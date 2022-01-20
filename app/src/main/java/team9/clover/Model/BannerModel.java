package team9.clover.Model;

public class BannerModel {
    String image, padding;

    public BannerModel() {}

    public BannerModel(String image, String padding) {
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
