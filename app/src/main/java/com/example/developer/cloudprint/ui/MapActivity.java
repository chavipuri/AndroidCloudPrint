package com.example.developer.cloudprint.ui;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferProgress;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.regions.Regions;
import com.dropbox.chooser.android.DbxChooser;
import com.example.developer.cloudprint.Manifest;
import com.example.developer.cloudprint.R;
import com.example.developer.cloudprint.model.Customer;
import com.example.developer.cloudprint.model.Document;

import com.example.developer.cloudprint.model.User;

import com.example.developer.cloudprint.services.CustomerServiceImpl;
import com.example.developer.cloudprint.services.DocServiceImpl;
import com.example.developer.cloudprint.services.MapAdapter;
import com.example.developer.cloudprint.services.MapContent;
import com.example.developer.cloudprint.services.MapServiceImpl;
import com.example.developer.cloudprint.services.UserServiceImpl;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.Wearable;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.DataOutputStream;

import com.example.developer.cloudprint.services.CloudPrint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import com.example.developer.cloudprint.model.User;
import com.example.developer.cloudprint.model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapActivity extends FragmentActivity implements LocationListener {
    LinearLayout mapBelowLinearLayout;
    Button uploadButton;
    private static final int REQUEST_CODE = 6384;
    static final int DBX_CHOOSER_REQUEST = 0;
    private static final String TAG = "FileChooserActivity";
    String upLoadServerUri = null;
    MapContent mc = new MapContent();

    Document doc1 = new Document();

    int serverResponseCode = 0;
    EditText Copies;
    Button printButton, listButton;

    boolean flag1 = false;
    boolean flag2 = false;
    public static final String BUCKET_NAME = "cloudprint";
    public FloatingActionButton profile, payment, history, logout = null;
    public JSONObject printerDetails;
    MapFragment mapFragment;
    ArrayList<Object> arrayList = new ArrayList<>();
    private Customer cs;
    double own_lat, own_long;
    ListView listView;
    MapAdapter itemsAdapter;
    ArrayList<MapContent> sendList = new ArrayList<>();
    GoogleMap googleMap;
    SupportMapFragment fm;

    //DocumentAPI
    private User user1;
    private User user2;
    private Document doc;
    private DocServiceImpl docService;
    private MapServiceImpl mapService;
    private UserServiceImpl userService;
    SQLiteDatabase mysql;
    String DOC_CREATE = "CREATE TABLE IF NOT EXISTS DOCUMENTS(ID TEXT PRIMARY KEY, DOCNAME TEXT, DOCURL TEXT, USER_ID TEXT)";
    Context context;
    public static final String BASE_FILE_URL = "https://s3.amazonaws.com/cloudprint/";
    private static final int LONG_DELAY = 4500;
    private String document_id;
    //save marker snippet on click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // setUpMapIfNeeded();
        context = this.getApplicationContext();


        mysql = this.openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        mysql.execSQL(DOC_CREATE);
        document_id = CloudPrint.docID;
        System.out.println("document id from history :" + document_id);
        Intent i = this.getIntent();
        user1 = (User) i.getSerializableExtra("User");
        Log.i("LoggedActivity User", user1.getFirstName() + "!!!!!!!!!!!!!!!!");


        CustomerServiceImpl csi = new CustomerServiceImpl();
        cs = new Customer();
        try {
            csi.getMapDetails(user1, context, cs);
            System.out.println("inside map :" + cs.getMapDetails());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        listView = (ListView) findViewById(R.id.tweetList);
        listButton = (Button) findViewById(R.id.listButton);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listButton.getText().toString().equalsIgnoreCase("List")) {
                    fm.getView().setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listButton.setText("Map");
                    Collections.sort(sendList, MapContent.COMPARE_BY_DIST);

                    itemsAdapter = new MapAdapter(MapActivity.this, sendList);


                    //
                    itemsAdapter.notifyDataSetChanged();
                    //
                    listView.setAdapter(itemsAdapter);
                    //
                    itemsAdapter.notifyDataSetChanged();
                    //
                } else if (listButton.getText().toString().equalsIgnoreCase("Map")) {
                    fm.getView().setVisibility(View.VISIBLE);

                    listView.setVisibility(View.GONE);
                    itemsAdapter.notifyDataSetChanged();
                    listButton.setText("List");
                    itemsAdapter.notifyDataSetChanged();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapContent mcont = new MapContent();
                mcont = (MapContent) parent.getItemAtPosition(position);
                //
                itemsAdapter.notifyDataSetChanged();
                //
                System.out.println("Clicked on List Printer" + mcont.getPrinterId());
                System.out.println(mcont.getPrinterSelectedCustomer());

                System.out.println("Clicked on List Printer" + mcont.getPrinterName());

                System.out.println("After select  " + mcont.getPrinterSelectedId());
                System.out.println("After select  " + mcont.getPrinterSelectedCustomer());
                mcont.setprinterSelectedId(mcont.getPrinterId());
                mcont.setPrinterSelectedCustomer(mcont.getCustomerId());
                mcont.setPrinterSelectedPrice(mcont.getPrice());

                mcont.setPrinterSelectedName2(mcont.getCustomerName());

                mc.setprinterSelectedId(mcont.getPrinterId());
                mc.setPrinterSelectedCustomer(mcont.getCustomerId());
                mc.setPrinterSelectedPrice(mcont.getPrice());
                mc.setprinterSelectedPrinterName(mcont.getPrinterName());

                mc.setPrinterSelectedName1(mcont.getPrinterName());
                mc.setPrinterSelectedName2(mcont.getCustomerName());

                //   System.out.println("After select customer " + mcont.getPrinterSelectedCustomer());
                //  System.out.println("After select printer " + mcont.getPrinterSelectedId());
                //  System.out.println("After select price " + mcont.getPrinterSelectedPrice());

                Toast.makeText(MapActivity.this, "Printer " + mc.getprinterSelectedPrinterName() + " is selected", Toast.LENGTH_SHORT).show();
                mapBelowLinearLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        mapBelowLinearLayout = (LinearLayout) findViewById(R.id.linearLayoutButton);
        mapBelowLinearLayout.setVisibility(LinearLayout.GONE);


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.MAPS_RECEIVE);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);

            printButton = (Button) findViewById(R.id.printButton);
            printButton.setVisibility(Button.GONE);

            Copies = (EditText) findViewById(R.id.copiesId);
            Copies.setVisibility(Button.GONE);

            if (document_id != null) {
                printButton.setVisibility(View.VISIBLE);
                Copies.setVisibility(View.VISIBLE);
            }

            uploadButton = (Button) findViewById(R.id.leftButton);
            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChooser();

                }
            });

            final FloatingActionsMenu mFabMenu = (FloatingActionsMenu) findViewById(R.id.floatingActionsMenu);

            profile = (FloatingActionButton) findViewById(R.id.profile);
            payment = (FloatingActionButton) findViewById(R.id.payment);
            history = (FloatingActionButton) findViewById(R.id.history);
            logout = (FloatingActionButton) findViewById(R.id.logout);

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, Profile.class);
                    intent.putExtra("User", user1);
                    startActivity(intent);
                }
            });

            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, Payment.class);
                    intent.putExtra("User", user1);
                    startActivity(intent);
                }
            });

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, History.class);
                    intent.putExtra("User", user1);
                    intent.putExtra("PrinterId", mc.getPrinterSelectedId());
                    intent.putExtra("CustomerId", mc.getPrinterSelectedCustomer());
                    intent.putExtra("Price", mc.getPrinterSelectedPrice());
                    intent.putExtra("PrinterName", mc.getPrinterSelectedName1());
                    intent.putExtra("CustomerName", mc.getPrinterSelectedName2());

                    startActivity(intent);
                }
            });


            printButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("in map activity " + user1.getHistoryDoc());
                    System.out.println("in map activity " + user1.getFirstName());

                    if (Copies.getText().toString().trim().equals("")) {
                        Toast.makeText(MapActivity.this, "Copies can not be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        mc.setPrinterSelectedCopy(Integer.parseInt(Copies.getText().toString().trim()));
                        Log.i("copies", "no of copies" + Copies.getText().toString().trim());
                        Log.i("userId", user1.get_id());
                        Log.i("documentId", doc.get_id());
                        Log.i("printerId", mc.getPrinterSelectedId());
                        Log.i("customerId", mc.getPrinterSelectedCustomer());
                        System.out.println("Copies " + mc.getPrinterSelectedCopy());
                        int totalamount = mc.getPrinterSelectedCopy() * mc.getPrinterSelectedPrice();
                        String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.

                        String alertMesssage = new String("Customer Name : " + mc.getPrinterSelectedName2() + newLine + newLine + "Printer Name : " + mc.getPrinterSelectedName1() + newLine + newLine + "Print Name : " + doc.getName() + newLine + newLine + "Copies : " + mc.getPrinterSelectedCopy() + newLine + newLine + "Price : " + mc.getPrinterSelectedPrice() + " $" + newLine + newLine + "Total Amount : " + totalamount + " $");


                        AlertDialog dialog = new AlertDialog.Builder(MapActivity.this)
                                .setTitle("Print Summary")
                                .setMessage(alertMesssage)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
//                                                    NoOfPage.setText(" ");
                                        mapBelowLinearLayout.setVisibility(LinearLayout.GONE);
                                        mapService = new MapServiceImpl();
                                        mapService.postPrinter(user1, doc, context, mc);

                                        mapService.postPrinter(user1, doc1, context, mc);
                                        System.out.println(mc.getResponse().toString() + " Response");
                                        if (mc.getResponse().contains("Success")) {
                                            //   CloudPrint.docID = null;
                                        }
                                        System.out.println(CloudPrint.docID + " Static variable");
                                        Log.i("Response", "Response  " + mc.getResponse());
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                    }


                }

            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, LoginUser.class);
                    startActivity(intent);
                }
            });

        }
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // setUpMapIfNeeded();
    }

    // on Activity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                Log.d("main", "Link to selected file: " + result.getLink());

                // Handle the result
            } else {
                // Failed or was cancelled by the user.
            }
        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Get the URI of the selected file
                    final Uri uri = data.getData();
                    Log.i(TAG, "Uri = " + uri.toString());
                    try {
                        // Get the file path from the URI
                        final String path = FileUtils.getPath(this, uri);
                        System.out.println("Path " + path);
                        Toast.makeText(MapActivity.this,
                                "File Selected: " + path, Toast.LENGTH_LONG).show();

                        upLoadServerUri = "http://www.chavisjsu.com/UploadToServer.php";

                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MapActivity.this, "uploading started.....", Toast.LENGTH_LONG).show();
                                        try {
                                            uploadFile(path);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            }
                        }).start();

                    } catch (Exception e) {
                        Log.e("FileSelectorTestAcivity", "File select error", e);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void uploadFile(String sourceFileUri) throws IOException {

        final String fileName = sourceFileUri;
        File sourceFile = new File(sourceFileUri);
        //Looper.prepare();
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :"
                    + fileName);
        } else {
            // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:0b413e9f-7a1b-44b7-adac-9ee2d229da56", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            Log.i("uploadFile", "CRED DONE");

            TransferManager transferManager = new TransferManager(credentialsProvider);

            Upload upload = transferManager.upload(BUCKET_NAME, sourceFile.getName(), sourceFile);
            AmazonS3Client client = new AmazonS3Client(credentialsProvider);

            URL fileURL = client.getUrl(BUCKET_NAME, sourceFile.getName());

            Log.i("uploadFile", sourceFile.getName());
            Log.i("uploadFile", BASE_FILE_URL + sourceFile.getName());
            Log.i("uploadFile URL", fileURL.toString());
            Log.i("uploadFile", user1.getFirstName());


            while (!upload.isDone()) {
                //Show a progress bar...
                TransferProgress transferred = upload.getProgress();
                // Toast.makeText(MapActivity.this, "Uploading... ", Toast.LENGTH_LONG).show();
                Log.i("Percentage", "" + transferred.getPercentTransferred());
                // Toast.makeText(MapActivity.this, "Uploading... " +transferred.getPercentTransferred(), Toast.LENGTH_LONG).show();
            }

            if (upload.isDone()) {
                // Toast.makeText(MapActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                //     printButton.setVisibility(Button.VISIBLE);
                doc = new Document();
                doc.setName(sourceFile.getName());
                doc.setUrl(fileURL.toString());

                docService = new DocServiceImpl();
                docService.postDocument(user1, doc, context);

                Log.i("Document", "ID is " + user1.get_id());
                Log.i("Document", "DocID is " + doc.get_id());
                Log.i("Document", "Name is " + doc.getName());
                Log.i("Document", "URL is " + doc.getUrl());
                //
                Log.i("uploadFile", user1.get_id());
                Log.i("uploadFile", BASE_FILE_URL + sourceFile.getName());
                Log.i("printerId", mc.getPrinterSelectedId());
                Log.i("customerId", mc.getPrinterSelectedCustomer());

                if (doc.get_id() != null) {
                    mysql.execSQL("INSERT INTO DOCUMENTS(ID,DOCNAME,DOCURL,USER_ID) VALUES('" + doc.get_id() + "','" + doc.getName() + "' ,'" + doc.getUrl() + "','" + user1.get_id() + "')");
                }
            }

            System.out.println("DocID is " + doc.get_id());

            File path1 = context.getFilesDir();

            String msg = doc.get_id() + ".txt";
            System.out.println("path    " + path1);
            String msg1 = "User Email: " + user1.getEmail() + "   " + " Receipt Code: " + doc.get_id() + " Doc Name: " + doc.getName();
            File file1 = new File(path1, msg);
            FileOutputStream stream = new FileOutputStream(file1);
            try {
                stream.write(msg1.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            System.out.println("Absolute path   " + file1.getAbsolutePath());
            final String path2 = "/data/data/com.example.developer.cloudprint/files/" + msg;

            uploadFileId(path2);

            printButton.setVisibility(Button.VISIBLE);
            Copies.setVisibility(Button.VISIBLE);


        } // End else block
    }


    public void uploadFileId(String sourceFileUri) {

        final String fileName = sourceFileUri;
        File sourceFile = new File(sourceFileUri);
        //Looper.prepare();
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :"
                    + fileName);
        } else {
            // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:0b413e9f-7a1b-44b7-adac-9ee2d229da56", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            //Toast.makeText(LoggedUser.this, "CRED DONE", Toast.LENGTH_LONG).show();
            Log.i("uploadFile", "CRED DONE");

            TransferManager transferManager = new TransferManager(credentialsProvider);

            Upload upload = transferManager.upload(BUCKET_NAME, sourceFile.getName(), sourceFile);
            AmazonS3Client client = new AmazonS3Client(credentialsProvider);

            URL fileURL = client.getUrl(BUCKET_NAME, sourceFile.getName());

            Log.i("uploadFile", sourceFile.getName());
            Log.i("uploadFile", BASE_FILE_URL + sourceFile.getName());
            Log.i("uploadFile URL", BASE_FILE_URL + sourceFile.getName());
            Log.i("uploadFile", user1.getFirstName());

            System.out.println("Check11" + sourceFile.getName() + "" + BASE_FILE_URL + "" + sourceFile.getName() + "" + BASE_FILE_URL + "" + sourceFile.getName() + "");


            while (!upload.isDone()) {
                //Show a progress bar...
                TransferProgress transferred = upload.getProgress();
                // Toast.makeText(MapActivity.this, "Uploading... ", Toast.LENGTH_LONG).show();
                Log.i("Percentage", "" + transferred.getPercentTransferred());
//                 Toast.makeText(MapActivity.this, "Uploading... " +transferred.getPercentTransferred(), Toast.LENGTH_LONG).show();
            }

            if (upload.isDone()) {
                // Toast.makeText(MapActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                //     printButton.setVisibility(Button.VISIBLE);
                // doc1 = new Document();
                doc1.setName(sourceFile.getName());
                doc1.setUrl(fileURL.toString());

                docService = new DocServiceImpl();
                docService.postDocument(user1, doc1, context);

                Log.i("Document1", "ID is " + user1.get_id());
                Log.i("Document1", "DocID is " + doc1.get_id());
                Log.i("Document1", "Name is " + doc1.getName());
                Log.i("Document1", "URL is " + doc1.getUrl());
                //
                Log.i("uploadFile1", user1.get_id());
                Log.i("uploadFile1", BASE_FILE_URL + sourceFile.getName());
                Log.i("printerId1", mc.getPrinterSelectedId());
                Log.i("customerId1", mc.getPrinterSelectedCustomer());

            }
            Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "DocID is " + doc.get_id(), Toast.LENGTH_LONG).show();

            printButton.setVisibility(Button.VISIBLE);
            Copies.setVisibility(Button.VISIBLE);


        } // End else block
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        own_lat = location.getLatitude();
        own_long = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        String newLine = System.getProperty("line.separator");
        printerDetails = cs.getMapDetails();
        try {
            // Debug
            sendList.clear();
            //Debug
            JSONArray jobj = printerDetails.getJSONArray("customers");
            for (int k = 0; k < jobj.length(); k++) {
                JSONObject jo = jobj.getJSONObject(k);
                JSONArray jarr = jo.getJSONArray("printers");

                for (int l = 0; l < jarr.length(); l++) {
                    JSONObject finaljob = jarr.getJSONObject(l);
                    if (finaljob.has("lat") && finaljob.has("long") && finaljob.has("printerId") && finaljob.has("printerName")) {

                        String lat1 = finaljob.getString("lat");
                        String long1 = finaljob.getString("long");
                        String printerId = finaljob.getString("printerId");
                        String printerName1 = finaljob.getString("printerName");
                        String customerId = finaljob.getString("customer_id");

                        String customerName1 = finaljob.getString("customer_name");
                        String price1 = finaljob.getString("price");

                        Location selected_location = new Location("locationA");
                        selected_location.setLatitude(own_lat);
                        selected_location.setLongitude(own_long);
                        Location near_locations = new Location("locationA");
                        near_locations.setLatitude(Double.parseDouble(lat1));
                        near_locations.setLongitude(Double.parseDouble(long1));

                        double distance = selected_location.distanceTo(near_locations);
                        double dist1 = distance / 1000;
                        String dist = String.format("%.2f", dist1);

                        MapContent mapContent = new MapContent();
                        //Adding to map content

                        mapContent.setPrinterId(printerId);
                        mapContent.setCustomerId(customerId);
                        mapContent.setPrinterName(printerName1);
                        mapContent.setPrice(Integer.parseInt(price1));
                        mapContent.setDist(Double.parseDouble(dist));
                        mapContent.setCustomerName(customerName1);

                        //sendList.clear();
                        sendList.add(mapContent);
                        // sendList.notifyAll();
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(lat1), Double.parseDouble(long1)))
                                        //.title("Printer Name:" + printerName1 + "Customer Name:" + customerName1 + "Price" + price1)
                                .title("Printer:" + printerName1 + "," + "Price:" + price1 + "$" + "," + "Customer:" + customerName1)
                                .snippet("cust:" + customerId + "," + "printer:" + printerId + ",price:" + price1)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.print1)));
                        ;
                    } else {
                        System.out.println("error");
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Log.v("Marker Error", "Set near Marker");


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mapBelowLinearLayout.setVisibility(LinearLayout.VISIBLE);
                printButton.setVisibility(View.GONE);
                Copies.setVisibility(View.GONE);
                //  Toast.makeText(MapActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
                mc.setprinterSelectedId(marker.getSnippet());
                mc.setPrinterSelectedCustomer(marker.getTitle());
                mc.getPrinterSelectedId();
                System.out.println("a" + "  " + marker.getSnippet());
                System.out.println("printerid" + "" + mc.getPrinterSelectedId());
                System.out.println("_id" + "" + mc.getPrinterSelectedCustomer());


                String[] names = marker.getTitle().split(",");
                for (int x = 0; x < names.length; x++) {
                    String[] d = names[x].split(":");
                    if (d.length == 2) {
                        String key = d[0];
                        String val = d[1];
                        if (key.equals("Printer")) {
                            System.out.println("PrinterName:  " + val);
                            mc.setPrinterSelectedName1(val);
                        }
                        if (key.equals("Customer")) {
                            System.out.println("CustomerName:  " + val);
                            mc.setPrinterSelectedName2(val);
                        }

                    }
                }
                Toast.makeText(MapActivity.this, "Printer :" + mc.getPrinterSelectedName1() + " is selected", Toast.LENGTH_LONG).show();

                String[] details = marker.getSnippet().split(",");
                for (int x = 0; x < details.length; x++) {
                    String[] d = details[x].split(":");
                    if (d.length == 2) {
                        String key = d[0];
                        String val = d[1];
                        if (key.equals("cust")) {
                            System.out.println("customerId:  " + val);
                            mc.setPrinterSelectedCustomer(val);
                        }
                        if (key.equals("printer")) {
                            System.out.println("printerId:  " + val);
                            mc.setprinterSelectedId(val);
                        }
                        if (key.equals("price")) {
                            System.out.println("price:  " + val);
                            mc.setPrinterSelectedPrice(Integer.parseInt(val));
                        }

                    }
                }

                return false;
            }
        });


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}