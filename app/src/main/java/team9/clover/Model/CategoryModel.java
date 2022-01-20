package team9.clover.Model;

public class CategoryModel {
    String image, title;
    long index;

    public CategoryModel() {}

    public CategoryModel(String image, String title, long index) {
        this.image = image;
        this.title = title;
        this.index = index;
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

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
