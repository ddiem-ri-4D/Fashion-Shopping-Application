package team9.clover.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductModel implements Serializable {

    public static final String FIRESTORAGE = "Product";

    long screen, category, price;
    String title, cutPrice, description, id;
    ArrayList<Long> measure;
    ArrayList<String> size;
    ArrayList<String> bodyName;
    ArrayList<String> info;
    ArrayList<String> image = null;
    ArrayList<String> search;

    public ProductModel() {}

    public ProductModel(String id, long screen, long category, String title, long price, String cutPrice, String description, ArrayList<String> image, ArrayList<String> size, ArrayList<String> bodyName, ArrayList<Long> measure, ArrayList<String> info, ArrayList<String> search) {
        this.id = id;
        this.screen = screen;
        this.category = category;
        this.title = title;
        this.price = price;
        this.cutPrice = cutPrice;
        this.description = description;
        this.image = image;
        this.size = size;
        this.bodyName = bodyName;
        this.measure = measure;
        this.info = info;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getScreen() {
        return screen;
    }

    public void setScreen(long screen) {
        this.screen = screen;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(String cutPrice) {
        this.cutPrice = cutPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<String> getSize() {
        return size;
    }

    public void setSize(ArrayList<String> size) {
        this.size = size;
    }

    public ArrayList<String> getBodyName() {
        return bodyName;
    }

    public void setBodyName(ArrayList<String> bodyName) {
        this.bodyName = bodyName;
    }

    public ArrayList<Long> getMeasure() {
        return measure;
    }

    public void setMeasure(ArrayList<Long> measure) {
        this.measure = measure;
    }

    public ArrayList<String> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<String> info) {
        this.info = info;
    }

    public ArrayList<String> getSearch() {
        return search;
    }

    public void setSearch(ArrayList<String> search) {
        this.search = search;
    }

    public void addImage(String image) {
        if (this.image == null) this.image = new ArrayList<>();
        this.image.add(image);
    }
}

/*
public static void setData() {

        ArrayList<Long> categorys = new ArrayList<>();
        categorys.add((long) 5);
        categorys.add((long) 2);
        categorys.add((long) 2);
        categorys.add((long) 4);
        categorys.add((long) 4);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("midi Salci skirt");
        titles.add("Paradiso panelled blazer");
        titles.add("tied check blouse");
        titles.add("printed Arctia midi dress");
        titles.add("Concours long dress");


        for (int j = 4; j < 9; ++j) {
            String k = Integer.toString(j);

            long screen = 3;
            long category = categorys.get(j - 4);
            String title = titles.get(j - 4),
                    price = "550.000 ??",
                    cutPrice = "820.000 ??",
                    description = "Ti???n D??ng l?? tr??? c???t ??? h??ng th??? c???a Vi???t Nam, th?????ng xuy??n ???? c???p c??ng Qu??? Ng???c H???i v?? ????? Duy M???nh trong s?? ????? ba trung v???. Anh ???????c HLV Park tri???u t???p ??? m???i gi???i ?????u, g??p ph???n quan tr???ng l??m n??n chi???n t??ch ??? U23 ch??u ?? 2018, v?? ?????ch AFF Cup 2018 v?? v??o v??ng lo???i th??? ba World Cup 2022 - khu v???c ch??u ??.";
            ArrayList<String> image = new ArrayList<>();
            ArrayList<String> size = new ArrayList<>(Arrays.asList("XS", "S", "L"));
            ArrayList<String> bodyName = new ArrayList<>(Arrays.asList("Vai", "L??ng", "D??i ??o"));
            ArrayList<Long> measure = new ArrayList<>();
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);
            measure.add((long) 1);


            ArrayList<String> info = new ArrayList<>();
            info.add("Ch???t li??u: v??i flannel");
            info.add("Ch???t v???i m???m m???i, kh??ng qu?? d??y");
            info.add("Chi ti???t tr?????c & sau th??u KTS s???c n??t");
            info.add("M???c v???i ????? ???? ???????c may b??n h??ng t??i ??o");
            info.add("Zipper HKK 2 chi???u");
            ProductModel productModel = new ProductModel(screen, category, title, price, cutPrice, description, image, size, bodyName, measure, info);
            firebaseFirestore.collection(ProductModel.class.getSimpleName()).document(k).set(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        for (int i = 0; i < 5; ++i) {
                            int finalI = i;
                            firebaseStorage.getReference(ProductModel.FIRESTORAGE + "/" + k + "/" + i + ".jpg")
                                    .getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                firebaseFirestore.collection(ProductModel.class.getSimpleName()).document(k).update(String.format("image.%d", finalI), task.getResult().toString());
                                            }
                                        }
                                    });
                        }
                    }
                }
            });
        }
    }

    public static ProductModel castFromFirestore(QueryDocumentSnapshot snapshot) {
        long screen = (long) snapshot.get("screen");
        long category = (long) snapshot.get("category");
        String title = (String) snapshot.get("title");
        String price = (String) snapshot.get("price");
        String cutPrice = (String) snapshot.get("cutPrice");
        String description = (String) snapshot.get("description");
        Map<String, String> mapImage = (Map<String, String>) snapshot.get("image");
        ArrayList<String> size = (ArrayList<String>) snapshot.get("size");
        ArrayList<String> bodyName = (ArrayList<String>) snapshot.get("bodyName");
        ArrayList<Long> measure = (ArrayList<Long>) snapshot.get("measure");
        ArrayList<String> info = (ArrayList<String>) snapshot.get("info");
        ArrayList<String> image = new ArrayList<>();

        for (int i = 0; i < mapImage.size(); ++i) {
            image.add(mapImage.get(Integer.toString(i)));
        }

        return new ProductModel(screen, category, title, price, cutPrice, description, image, size, bodyName, measure, info);
    }

    public static ProductModel castFromFirestore2(DocumentSnapshot snapshot) {
        long screen = (long) snapshot.get("screen");
        long category = (long) snapshot.get("category");
        String title = (String) snapshot.get("title");
        String price = (String) snapshot.get("price");
        String cutPrice = (String) snapshot.get("cutPrice");
        String description = (String) snapshot.get("description");
        Map<String, String> mapImage = (Map<String, String>) snapshot.get("image");
        ArrayList<String> size = (ArrayList<String>) snapshot.get("size");
        ArrayList<String> bodyName = (ArrayList<String>) snapshot.get("bodyName");
        ArrayList<Long> measure = (ArrayList<Long>) snapshot.get("measure");
        ArrayList<String> info = (ArrayList<String>) snapshot.get("info");
        ArrayList<String> image = new ArrayList<>();

        for (int i = 0; i < mapImage.size(); ++i) {
            image.add(mapImage.get(Integer.toString(i)));
        }

        return new ProductModel(screen, category, title, price, cutPrice, description, image, size, bodyName, measure, info);
    }
* */