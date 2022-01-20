package team9.clover.Model;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team9.clover.R;

public class DatabaseModel {

    public static FirebaseUser firebaseUser = null;
    public static FirebaseFirestore firebaseFirestore = null;
    public static FirebaseAuth firebaseAuth = null;

    public static String masterUid = "";
    public static UserModel masterUser = null;
    public static OrderModel masterOrder = null;
    public static List<CartItemModel> masterCart = new ArrayList<>();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    public static List<HomePageModel> homePageModelList = new ArrayList<>();

    private static CollectionReference pirate = null;

    /*
     * Load người dùng hiện tại trên thiết bị
     * */
    public static void getCurrentUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            masterUid = firebaseUser.getUid();
        }
    }

    /*
     * Thêm thông tin new user vào Firestore với key là email
     * */
    public static Task addNewUser(UserModel newUser) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        masterUid = firebaseAuth.getUid();
        return firebaseFirestore.collection(UserModel.class.getSimpleName()).document(masterUid).set(newUser);
    }

    /*
     * Đăng kí new user này với Firebase authentication
     * */
    public static Task signUp(String email, String password) {
        if (firebaseAuth == null) firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    /*
     * Đăng nhập bằng Firebase authentication
     * */
    public static Task signIn(String email, String password) {
        if (firebaseAuth == null) firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    /*
     * Dùng để gửi email reset password cho user
     * */
    public static Task resetPassword(String email) {
        if (firebaseAuth == null) firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.sendPasswordResetEmail(email);
    }

    private static void loadCategoryData() {
        firebaseFirestore.collection(CategoryModel.class.getSimpleName())
                .orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                categoryModelList.add(snapshot.toObject(CategoryModel.class));
                            }
                        }
                    }
                });
    }

    private static void loadCarouselData() {
        firebaseFirestore.collection(CarouselModel.class.getSimpleName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<CarouselModel> carouselModelList = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                carouselModelList.add(snapshot.toObject(CarouselModel.class));
                            }
                            homePageModelList.add(new HomePageModel(carouselModelList));
                        }
                    }
                });
    }

    private static void loadBannerData() {
        firebaseFirestore.collection(BannerModel.class.getSimpleName())
                .document("0")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    BannerModel bannerModel = task.getResult().toObject(BannerModel.class);
                    homePageModelList.add(new HomePageModel(bannerModel));
                }
            }
        });
    }

    private static void loadHomePageProductData() {
        firebaseFirestore.collection(ProductModel.class.getSimpleName())
                .whereGreaterThan("screen", (long) HomePageModel.BANNER_VIEW_TYPE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ProductModel> newProducts = new ArrayList<>();
                            List<ProductModel> sliderProducts = new ArrayList<>();
                            List<ProductModel> gridProducts = new ArrayList<>();

                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                ProductModel productModel = snapshot.toObject(ProductModel.class);
                                if (productModel.getScreen() == HomePageModel.NEW_PRODUCT_VIEW_TYPE) {
                                    newProducts.add(productModel);
                                } else if (productModel.getScreen() == HomePageModel.SLIDER_PRODUCT_VIEW_TYPE) {
                                    sliderProducts.add(productModel);
                                } else if (productModel.getScreen() == HomePageModel.GRID_PRODUCT_VIEW_TYPE) {
                                    gridProducts.add(productModel);
                                }
                            }

                            homePageModelList.add(new HomePageModel(HomePageModel.NEW_PRODUCT_VIEW_TYPE, -1, null, newProducts));
                            homePageModelList.add(new HomePageModel(HomePageModel.SLIDER_PRODUCT_VIEW_TYPE, R.drawable.icon_cart_check, "Bán nhiều nhất", sliderProducts));
                            homePageModelList.add(new HomePageModel(HomePageModel.GRID_PRODUCT_VIEW_TYPE, R.drawable.icon_discount, "Khuyến mãi hôm nay", gridProducts));
                            homePageModelList.add(new HomePageModel(HomePageModel.FOOTER_VIEW_TYPE));
                        }
                    }
                });
    }

    public static void loadHomePageData() {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        if (categoryModelList.size() != 0) return;

        loadCategoryData();
        loadCarouselData();
        loadBannerData();
        loadHomePageProductData();
    }

//================================================================================================== PRODUCT
    public static Task<QuerySnapshot> loadProduct(String field, long value) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore.collection(ProductModel.class.getSimpleName())
                .whereEqualTo(field, (long) value).get();
    }

    public static Task<DocumentSnapshot> loadSingleProduct(String value) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore.collection(ProductModel.class.getSimpleName())
                .document(value).get();
    }

    public static Task<QuerySnapshot> loadProduct(String field, List<String> valueList) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore.collection(ProductModel.class.getSimpleName())
                .whereIn(field, valueList).get();
    }



//================================================================================================== USER
    /*
    * Load thông tin của user từ data base lên
    * */
    public static void loadMasterUser(MaterialTextView fullName, MaterialTextView email) {
        if (!masterUid.isEmpty()) {
            if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference dr = firebaseFirestore.collection(UserModel.class.getSimpleName()).document(masterUid);
            dr.get().addOnSuccessListener(task1 -> {
                masterUser = task1.toObject(UserModel.class);
                fullName.setText(masterUser.getFullName()); // set data cho navigation view
                email.setText(masterUser.getEmail()); // set data cho navigation view

                if (!masterUser.getOrder().isEmpty()) {
                    dr.collection(OrderModel.class.getSimpleName()).document(masterUser.getOrder())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                masterOrder = task.getResult().toObject(OrderModel.class);
                            }
                        }
                    });

                    dr.collection(OrderModel.class.getSimpleName()).document(masterUser.getOrder())
                            .collection(CartItemModel.class.getSimpleName())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                                for (QueryDocumentSnapshot snapshot : task.getResult())
                                    masterCart.add(snapshot.toObject(CartItemModel.class));
                        }
                    });
                } else {
                    masterOrder = new OrderModel(true);
                    dr.collection(OrderModel.class.getSimpleName()).add(masterOrder).addOnSuccessListener(task2 -> {
                        masterUser.setOrder(task2.getId());
                    });
                }
            });
        }
    }

    public static boolean updateMasterCart(String id, String size, long quantity, CartItemModel newCart) {
        if (newCart != null) {
            DatabaseModel.masterCart.add(newCart);
            return true;
        }

        for (CartItemModel cart : DatabaseModel.masterCart) {
            if (cart.getId().equals(id)) {
                cart.addCart(size, quantity);
                return true;
            }
        }

        return false;
    }

    /*
     * Đăng xuất khỏi current user (đăng nhập bằng email và password)
     * */
    public static void signOut() {
        // kiểm tra firebase authentication đã được khởi tạo chưa, nếu chưa thì khởi tạo
        if (firebaseAuth == null) firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

    public static Task<DocumentReference> addNewOrder() {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        masterOrder = new OrderModel(true);
        return firebaseFirestore.collection(UserModel.class.getSimpleName())
                .document(masterUid).collection(OrderModel.class.getSimpleName())
                .add(masterOrder);
    }

    public static void refreshMasterOrder() {
        long noProducts = 0, total = 0;
        for (CartItemModel cart : DatabaseModel.masterCart) {
            for (Long value : cart.getChoice().values()) {
                noProducts += value;
            }

            total += cart.getTotal();
        }

        masterOrder.setNoProducts(noProducts);
        masterOrder.setTotal(total);
    }

    public static void updateMasterUser() {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        if (masterUid.isEmpty()) return;
        firebaseFirestore.collection(UserModel.class.getSimpleName())
                .document(masterUid)
                .set(masterUser);
    }

    public static Task<Void> updateMasterOrder() {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore.collection(UserModel.class.getSimpleName())
                .document(masterUid).collection(OrderModel.class.getSimpleName())
                .document(masterUser.getOrder()).set(masterOrder);
    }

    public static void updateMasterCart() {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();

        if (masterUser.getOrder().isEmpty()) {
            firebaseFirestore.collection(UserModel.class.getSimpleName())
                    .document(masterUid).collection(OrderModel.class.getSimpleName())
                    .add(masterOrder).addOnSuccessListener(task -> {
                masterUser.setOrder(task.getId());
                updateMasterCart();
            });
        } else {
            CollectionReference reference = firebaseFirestore.collection(UserModel.class.getSimpleName())
                    .document(masterUid).collection(OrderModel.class.getSimpleName())
                    .document(masterUser.getOrder()).collection(CartItemModel.class.getSimpleName());

            for (CartItemModel cart : masterCart) {
                reference.document(cart.getId()).set(cart);
            }
        }
    }

    public static Task<Void> removeMasterCard(String documentId) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();

        return firebaseFirestore.collection(UserModel.class.getSimpleName())
                    .document(masterUid)
                    .collection(CartItemModel.class.getSimpleName())
                    .document(documentId).delete();
    }

    public static Task<QuerySnapshot> loadOrders() {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore.collection(UserModel.class.getSimpleName()).document(masterUid)
                .collection(OrderModel.class.getSimpleName()).orderBy("order",  Query.Direction.DESCENDING).get();
    }

    public static Task<QuerySnapshot> loadOrderDetail(String orderId) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        return firebaseFirestore.collection(UserModel.class.getSimpleName()).document(masterUid)
                .collection(OrderModel.class.getSimpleName()).document(orderId)
                .collection(CartItemModel.class.getSimpleName()).get();
    }

    public static Task<QuerySnapshot> searchProduct(List<String> keyword) {
        if (firebaseFirestore == null) firebaseFirestore = FirebaseFirestore.getInstance();
        if (pirate == null) pirate = firebaseFirestore.collection(ProductModel.class.getSimpleName());
        return pirate.whereArrayContainsAny("search", keyword).get();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////





    //______________________________________________________________________________________________________ DANGER FUNCTION




    public static void addProduct() {
        int folderStart = 20; // index của sản [hẩm bắt đầu ghi
        long screen = -1; // các sản phẩm này sẽ hiễn thị màn hình nào


        ArrayList<Long> categorys = new ArrayList<>();
        categorys.add((long) 7);
//        categorys.add((long) 1);
//        categorys.add((long) 1);
//        categorys.add((long) 1);
//        categorys.add((long) 1);
//        categorys.add((long) 1);

        ArrayList<Integer> noImages = new ArrayList<>();
        noImages.add(5);
//        noImages.add(4);
//        noImages.add(3);
//        noImages.add(4);
//        noImages.add(4);
//        noImages.add(4);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("One-Pocket logo-lettering phone case");
//        titles.add("Xuận hạ thu đông");
//        titles.add("Nắng");
//        titles.add("Rạng đông");
//        titles.add("Nhật thực");
//        titles.add("Bếp lửa");


        /* start */
        long price = 550000;
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String cutPrice = "820.000 đ",
                description = "Tiến Dũng là trụ cột ở hàng thủ của Việt Nam, thường xuyên đá cặp cùng Quế Ngọc Hải và Đỗ Duy Mạnh trong sơ đồ ba trung vệ. Anh được HLV Park triệu tập ở mọi giải đấu, góp phần quan trọng làm nên chiến tích ở U23 châu Á 2018, vô địch AFF Cup 2018 và vào vòng loại thứ ba World Cup 2022 - khu vực châu Á.";
        ArrayList<String> size = new ArrayList<>(Arrays.asList("XS", "S", "L"));
        ArrayList<String> bodyName = new ArrayList<>(Arrays.asList("Vai", "Lưng", "Dài áo"));
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
        info.add("Chất liêu: vài flannel");
        info.add("Chất vải mềm mại, không quá dày");
        info.add("Chi tiết trước & sau thêu KTS sắc nét");
        info.add("Mạc vải đỏ đô được may bên hông túi áo");
        info.add("Zipper HKK 2 chiều");
        /* end*/

        for (int i = 0; i < titles.size(); ++i) {
            int noImage = noImages.get(i);
            long category = categorys.get(i);
            String title = titles.get(i);
            ArrayList<String> image = new ArrayList<>();
            ProductModel productModel = new ProductModel(Integer.toString(folderStart + i), screen, category, title, price, cutPrice, description, image, size, bodyName, measure, info, new ArrayList<>());
            addDataRecursion(firebaseStorage, productModel, folderStart + i, 0, noImage, Integer.toString(folderStart + i));
        }
    }

    private static void addDataRecursion(FirebaseStorage firebaseStorage, ProductModel product, final int folder, int step, final int noImage, final String documentId) {
        if (step == noImage) {
            firebaseFirestore.collection(ProductModel.class.getSimpleName())
                    .document(documentId)
                    .set(product);
        } else if (step < noImage) {
            firebaseStorage.getReference(ProductModel.FIRESTORAGE + "/" + folder + "/" + step + ".jpg")
                    .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String image = task.getResult().toString();
                        product.addImage(image);
                        addDataRecursion(firebaseStorage, product, folder, step + 1, noImage, documentId);
                    }
                }
            });
        }
    }
}