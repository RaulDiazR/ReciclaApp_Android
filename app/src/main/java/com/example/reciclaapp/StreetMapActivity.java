package com.example.reciclaapp;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class StreetMapActivity extends AppCompatActivity {

    StreetMapActivity father = this;
    Button ActiveButtomSheet;
    MapView MapOS = null;
    GeoPoint StartPoint;
    Marker Mark = null;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    InfoWindow customInfoWindow;
    final Context context = this;
    final Activity activity = this;
    List<Category> categoryList = new ArrayList<>();
    List<Center> centerList = new ArrayList<>();
    Map<String, ArrayList<Integer>> FavoriteMap = new HashMap<>();
    Map<String, ArrayList<Integer>> CategoryCenterMap = new HashMap<>(); //Mapa con la posición de los materiales según categoría
    Dialog BottomDialog;
    View BottomDialogView;
    FirebaseFirestore db;
    Map<String, Integer> MaterialMap = new HashMap<>(); //Mapa con la posición del material en la lista de materiales
    ArrayList<MaterialModel> Materials = new ArrayList<>();
    FavoriteModel Favorites;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_map);

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        // Set item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId != R.id.mapa) {
                // Navigate to the corresponding activity
                navigateToActivity(itemId);
            }
            return true;
        });

        // Initialize the selected item based on the current activity
        int currentItemId = getCurrentItemIdForActivity();
        bottomNavigationView.setSelectedItemId(currentItemId);

        try {
            //ProgressBar
            progressBar = findViewById(R.id.ProgressBar);

            // Load Resources from Firebase
            db = FirebaseFirestore.getInstance();
            getFavorites();
            getMaterials();
            createCategoryList();


            // Bottom Sheet Dialog Implementations
            ActiveButtomSheet = findViewById(R.id.activate_bottom_sheet);
            ActiveButtomSheet.setOnClickListener(v -> showDialog());

            // Ubication
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            // Map Implementation
            Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
            MapOS = findViewById(R.id.osmap);
            MapOS.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
            MapOS.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
            MapOS.setMultiTouchControls(true);
            MapOS.setHorizontalMapRepetitionEnabled(true);
            MapOS.setVerticalMapRepetitionEnabled(false);
            MapOS.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(), MapView.getTileSystem().getMinLatitude(), 0);
            MapOS.getController().setZoom(20.0);
            MapOS.setMinZoomLevel(5.0);
            StartPoint = new GeoPoint(19.041345, -98.206119);
            MapOS.getController().setCenter(StartPoint);

            checkLocationPermission();

            createCenter();

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception in Logcat
            String errorMessage = e.getMessage(); // Get the error message
            runOnUiThread(() -> {
                Toast.makeText(this, "An error occurred: " + errorMessage, Toast.LENGTH_LONG).show();
            });
        }


    }

    private void navigateToActivity(int itemId) {
        Intent intent = null;

        if (itemId == R.id.inicio) {
            intent = new Intent(this, VerNoticiasActivity.class);
        } else if (itemId == R.id.reciclaje) {
            intent = new Intent(this, HistorialRecoleccionesActivity.class);
        } else if (itemId == R.id.ajustes) {
            intent = new Intent(this, SettingsActivity.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0,0);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Update the selected item in the bottom navigation view
        int currentItemId = getCurrentItemIdForActivity();
        bottomNavigationView.setSelectedItemId(currentItemId);
        overridePendingTransition(0,0);
    }

    private int getCurrentItemIdForActivity() {
        Class<?> currentClass = this.getClass();

        if (currentClass == VerNoticiasActivity.class) {
            return R.id.inicio;
        } else if (currentClass == HistorialRecoleccionesActivity.class) {
            return R.id.reciclaje;
        } else if (currentClass == SettingsActivity.class) {
            return R.id.ajustes;
        } else {
            return R.id.mapa;
        }
    }

    public void updateFavorites() {

        for (String name : Favorites.getCenterNames()) {
            System.out.println(name);
            if (FavoriteMap.containsKey(name)) {
                FavoriteMap.get(name).set(1, 1);
                centerList.get(FavoriteMap.get(name).get(0)).setFavorite(true);
            }
        }

    }

    public void udpateCollectionFavorites() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference to the Users collection
            DocumentReference userRef = db.collection("usuarios").document(user.getUid());
            // Get user information

        }

        // Get uid of the user
        String uid = String.valueOf(user);
        //String user = "prueba";


        DocumentReference favoritesReferenceUser = db.collection("usuario_favoritos").document(uid);

        ArrayList<String> oldFavorites = Favorites.getCenterNames();
        ArrayList<DocumentReference> newFavorites = new ArrayList<>();
        DocumentReference ref;
        List<Task<DocumentReference>> tasks = new ArrayList<>(); // Lista para almacenar las tareas

        for (Map.Entry<String, ArrayList<Integer>> entry : FavoriteMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();

            if (oldFavorites.contains(key)) {// Old Favorites

                ref = Favorites.getCentros().get(oldFavorites.indexOf(key));
                if (value.get(1) == 1) {
                    newFavorites.add(ref);
                }



            } else if ( !oldFavorites.contains(key) && value.get(1) == 1) { // New Added Favorites


                Task<QuerySnapshot> center = db.collection("centros").whereEqualTo("nombre", key).get();
                Task<DocumentReference> task = center.continueWithTask(taskk -> {
                    if (taskk.isSuccessful()) {
                        for (QueryDocumentSnapshot document : taskk.getResult()) {
                            DocumentReference reference = db.collection("centros").document(document.getId());
                            newFavorites.add(reference);
                        }
                    } else {
                        System.out.println("Error retrieving document");
                    }
                    return null;
                });

                tasks.add(task); // Agrega cada tarea a la lista de tareas

            }

        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<List<Task<?>>> task) {
                favoritesReferenceUser.update("centros" , newFavorites ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        getFavorites();
                    }
                });
            }
        });

    }

    public void createCenter() {

        db.collection("centros").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            //loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            int con = 0;
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.

                                CenterModel centerModel = new CenterModel(Objects.requireNonNull(d.getData()));

                                if (centerModel.isValid()) {

                                    ArrayList<MaterialModel> mate = new ArrayList<>();

                                    for ( String j : centerModel.getMateriales()) {
                                        if (MaterialMap.containsKey(j)) {
                                            mate.add(Materials.get(MaterialMap.get(j)));
                                        }
                                    }

                                    Center center = new Center(centerModel.getNombre(), centerModel.getDireccion(), centerModel.getLatitud(), centerModel.getLongitud(), centerModel.getNum_telefonico(), centerModel.getHora_apertura(), centerModel.getHora_cierra(), centerModel.getImagen(), MapOS, context, centerModel.getCategoria(), activity, mate, centerModel.getDias(), father);
                                    centerList.add(center);
                                    FavoriteMap.put(center.getNombre(), new ArrayList<>(Arrays.asList(con, 0)));
                                    if (CategoryCenterMap.containsKey(centerModel.getCategoria())) {
                                        CategoryCenterMap.get(centerModel.getCategoria()).add(con);
                                    } else {
                                        CategoryCenterMap.put(centerModel.getCategoria(), new ArrayList<>());
                                        CategoryCenterMap.get(centerModel.getCategoria()).add(con);
                                    }
                                    MapOS.getOverlays().add(center.getMark());
                                    con++;
                                }

                            }

                            Toast.makeText( context, "Centros Creados", Toast.LENGTH_SHORT).show();

                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText( context, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(context, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });


        progressBar.setVisibility(View.GONE);
    }

    private void createCategoryList() {

        db.collection("categorias").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            //loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            int con = 0;
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                CategoryModel categoryModel = new CategoryModel(Objects.requireNonNull(d.getData()));

                                if (categoryModel.isValid()) {
                                    Category category = new Category((String) d.getId(),categoryModel.getImageURL());
                                    categoryList.add(category);
                                    if (!CategoryCenterMap.containsKey((String) d.getId())) {
                                        CategoryCenterMap.put( (String) d.getId(), new ArrayList<>());
                                    }
                                    con++;
                                }


                            }

                            Toast.makeText( context, "Categorias Creadas", Toast.LENGTH_SHORT).show();

                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText( context, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(context, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void getFavorites() {

        //Get the user UID
        String user = "toncUoDSNGceFkUvLmEcE0vB6Mf2";
        //String user = "prueba";

        DocumentReference favoritesReferenceUser = db.collection("usuario_favoritos").document(user);
        favoritesReferenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    System.out.println("Obtuve el documento");
                    Favorites = new FavoriteModel(documentSnapshot.getData());
                    for (DocumentReference documentReference: Favorites.getCentros()) {
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Favorites.getCenterNames().add( (String) documentSnapshot.getData().get("nombre"));
                                    System.out.println(Favorites.getCenterNames());
                                } else {
                                    System.out.println("El centro no existe");
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                updateFavorites();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                System.out.println("Error al obtener nombre del centro");
                            }
                        });
                    }

                } else {
                    System.out.println("Agregue el documento");
                    Map<String, Object> data = new HashMap<>();
                    data.put("centros", new ArrayList<>());
                    data.put("recolectores", new ArrayList<>());

                    favoritesReferenceUser.set(data);
                    Favorites = new FavoriteModel(data);
                }
                Toast.makeText(context, "Centros Favoritos", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(context, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategories(View view) {

        //Get layout where is placed the categories
        LinearLayout filter_layout = view.findViewById(R.id.filter_layout);

        // Get All Categories from Firestore

        // Once we get categories add them to a list to save them


        Category category = null;
        int h = categoryList.size() % 3;
        int size = categoryList.size() / 3;
        for (int i = 0; i < size; i++) {
            LinearLayout categoryLayout = new LinearLayout(this);
            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
            categoryLayout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho MATCH_PARENT
                    LinearLayout.LayoutParams.WRAP_CONTENT   // Alto WRAP_CONTENT
            );
            categoryLayout.setLayoutParams(layoutParams);


            for (int j = i*3; j < i*3+3; j++) {

                LinearLayout optionCategoryInLayout = new LinearLayout(this);
                optionCategoryInLayout.setOrientation(LinearLayout.VERTICAL);
                optionCategoryInLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParamsInOption = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho MATCH_PARENT
                        LinearLayout.LayoutParams.WRAP_CONTENT   // Alto WRAP_CONTENT
                );
                layoutParamsInOption.weight = 1.0f;

                optionCategoryInLayout.setLayoutParams(layoutParamsInOption);

                // Get Category from Category List by Index
                category = categoryList.get(j);

                // Establish Image Button from Category
                ImageButton imageButton = new ImageButton(this);
                imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                        200,
                        200
                ));
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageButton.setBackground(ContextCompat.getDrawable(context, R.color.blue));
                Picasso.get().load(category.getImageResourceId()).into(imageButton);

                // Establish Text View from Category
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                textView.setText(category.getTitle());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);

                optionCategoryInLayout.addView(imageButton);
                optionCategoryInLayout.addView(textView);
                Category finalCategory = category;
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        putCentersCategoryInMap(finalCategory.getTitle());
                        BottomDialog.dismiss();
                    }
                });
                categoryLayout.addView(optionCategoryInLayout);

            }
            filter_layout.addView(categoryLayout);
        }

        if (h > 0) {
            size = categoryList.size() - h ;
            LinearLayout categoryLayout = new LinearLayout(this);
            categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
            categoryLayout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho MATCH_PARENT
                    LinearLayout.LayoutParams.WRAP_CONTENT   // Alto WRAP_CONTENT
            );
            categoryLayout.setLayoutParams(layoutParams);

            for (int j = size; j < size + h; j++) {

                LinearLayout optionCategoryInLayout = new LinearLayout(this);
                optionCategoryInLayout.setOrientation(LinearLayout.VERTICAL);
                optionCategoryInLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParamsInOption = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho MATCH_PARENT
                        LinearLayout.LayoutParams.WRAP_CONTENT   // Alto WRAP_CONTENT
                );
                layoutParamsInOption.weight = 1.0f;

                optionCategoryInLayout.setLayoutParams(layoutParamsInOption);

                // Get Category from Category List by Index
                category = categoryList.get(j);

                // Establish Image Button from Category
                ImageButton imageButton = new ImageButton(this);
                imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                        200,
                        200
                ));
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageButton.setBackground(ContextCompat.getDrawable(context, R.color.blue));
                Picasso.get().load(category.getImageResourceId()).into(imageButton);

                // Establish Text View from Category
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                textView.setText(category.getTitle());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);

                optionCategoryInLayout.addView(imageButton);
                optionCategoryInLayout.addView(textView);

                Category finalCategory = category;
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        putCentersCategoryInMap(finalCategory.getTitle());
                        BottomDialog.dismiss();
                    }
                });

                categoryLayout.addView(optionCategoryInLayout);

            }

            filter_layout.addView(categoryLayout);
        }
    }

    private void putCentersCategoryInMap(String category) {
        ArrayList<Integer> aux;
        putCentersHidden();
        if (CategoryCenterMap.containsKey(category)) {
            aux = CategoryCenterMap.get(category);
            for (int i = 0; i < CategoryCenterMap.get(category).size(); i++) {
                centerList.get(aux.get(i)).getMark().setVisible(true);
            }
        }

        for (Map.Entry<String, ArrayList<Integer>> entry : CategoryCenterMap.entrySet()) {
            String clave = entry.getKey();
            ArrayList<Integer> valores = entry.getValue();

            System.out.println("Clave: " + clave);
            System.out.println("Valores: " + valores);
            System.out.println("---");
        }

    }

    private void putCentersHidden() {
        Marker marker = null;
        for(int i = 0; i < centerList.size(); i++) {
            marker = centerList.get(i).getMark();
            marker.setVisible(false);

            if (marker.isInfoWindowOpen()) {
                marker.closeInfoWindow();
            }
        }
    }

    private void putCentersVisible() {
        for(int i = 0; i < centerList.size(); i++) {
            centerList.get(i).getMark().setVisible(true);
        }
    }

    private void showDialog() {
        if (isFinishing()) {
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Inflar el layout personalizado dentro del diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null);
        dialog.setContentView(dialogView);

        BottomDialog = dialog;
        BottomDialogView = dialogView;

        Button showAllButton = dialogView.findViewById(R.id.show_all);
        if (showAllButton != null) {
            showAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    putCentersVisible();
                }
            });
        }

        // Configuración del diseño y estilo del diálogo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            int dialogAnimationStyle = R.style.DialogAnimation;
            if (dialogAnimationStyle != 0) {
                dialog.getWindow().getAttributes().windowAnimations = dialogAnimationStyle;
            }

            dialog.getWindow().setGravity(Gravity.BOTTOM);

            try {
                getCategories(dialogView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.show();
        }
    }

    private void getMaterials() {

        db.collection("materiales").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            int con = 0;
                            for (DocumentSnapshot d : list) {
                                MaterialModel materialModel = new MaterialModel(Objects.requireNonNull(d.getData()));

                                if (materialModel.isValid()) {
                                    materialModel.setName((String) d.getId());

                                    Materials.add(materialModel);
                                    MaterialMap.put((String) d.getId(), con);
                                    con++;
                                }
                            }

                            Toast.makeText( context, "Materiales Listos", Toast.LENGTH_SHORT).show();

                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText( context, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(context, "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    LocationCallback mLocationCallback = new LocationCallback() {

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;

                if (customInfoWindow == null) {
                     customInfoWindow = new InfoWindow(R.layout.custom_window_self_ubication, MapOS) {
                        @Override
                        public void onOpen(Object item) {

                        }
                        @Override
                        public void onClose() {

                        }
                    };
                }

                if (StartPoint == null) {
                    StartPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                } else {
                    StartPoint.setLatitude(location.getLatitude());
                    StartPoint.setLongitude(location.getLongitude());
                }

                if (Mark == null) {
                    Mark = new Marker(MapOS);
                    Mark.setPosition(StartPoint);
                    Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    Mark.setIcon(ContextCompat.getDrawable(context, R.drawable.icon_pin_self));
                    Mark.setInfoWindow(customInfoWindow);
                    MapOS.getController().setCenter(StartPoint);

                } else {
                    Mark.setPosition(StartPoint);
                    Mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                }

                Mark.setOnMarkerClickListener((marker, mapView) -> {
                    if (marker.isInfoWindowOpen()) {
                        marker.closeInfoWindow();
                    } else {
                        marker.showInfoWindow();
                    }

                    return true;
                });


                MapOS.getOverlays().add(Mark);
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Se necesita permiso de ubicación")
                        .setMessage("Esta aplicación necesita el permiso de ubicación, por favor acepta para utilizar la funcionalidad de ubicación")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(StreetMapActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            requestLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    requestLocationUpdates();

                } else {
                    Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(true)
                .build();

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
}