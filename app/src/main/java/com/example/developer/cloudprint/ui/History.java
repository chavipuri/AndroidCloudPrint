package com.example.developer.cloudprint.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.developer.cloudprint.R;
import com.example.developer.cloudprint.model.Document;
import com.example.developer.cloudprint.model.User;
import com.example.developer.cloudprint.services.DocServiceImpl;
import com.example.developer.cloudprint.services.HistoryAdapter;
import com.example.developer.cloudprint.services.HistoryContent;
import com.example.developer.cloudprint.services.MapAdapter;
import com.example.developer.cloudprint.services.MapContent;
import com.example.developer.cloudprint.services.MapServiceImpl;
import com.example.developer.cloudprint.services.UserServiceImpl;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.example.developer.cloudprint.services.CloudPrint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class History extends AppCompatActivity {


    private UserServiceImpl userService;
    public JSONObject historyDetails;
    ListView listViewH;
    HistoryAdapter itemsAdapter;
    ArrayList<HistoryContent> sendHList = new ArrayList<>();
    HistoryContent hc = new HistoryContent();
    String dc_id;
    public ImageLoader imageLoader = ImageLoader.getInstance();


    private DocServiceImpl docService;
    private MapServiceImpl mapService;
    MapContent mc1 = new MapContent();
    Document doc = new Document();

    EditText Copies;
    Button printButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent i = this.getIntent();
        final User user1 = (User) i.getSerializableExtra("User");

        final String PrinterId1 = i.getExtras().getString("PrinterId");
        final String CustomerId1 = i.getExtras().getString("CustomerId");
        final String Name1 = i.getExtras().getString("PrinterName");
        final String Name2 = i.getExtras().getString("CustomerName");
        int Price = i.getExtras().getInt("Price");


        Log.i("LoggedActivity User", user1.getFirstName() + "!");

        System.out.println("History User Name" + user1.getFirstName());
        System.out.println("pid :" + PrinterId1);
        System.out.println("did :" + CustomerId1);
        System.out.println("Price :" + Price);


        mc1.setprinterSelectedId(PrinterId1);
        mc1.setPrinterSelectedCustomer(CustomerId1);
        mc1.setPrinterSelectedPrice(Price);
        mc1.setPrinterSelectedName1(Name1);
        mc1.setPrinterSelectedName2(Name2);


        final Context context = this.getApplicationContext();
// custom list
        listViewH = (ListView) findViewById(R.id.tweetListH);
        itemsAdapter = new HistoryAdapter(context, sendHList);
        listViewH.setAdapter(itemsAdapter);

        userService = new UserServiceImpl();

        userService.history(user1, context);

        historyDetails = user1.getUserDetails();
        user1.setHistoryDoc(dc_id);
        user1.getHistoryDoc();
        System.out.println("checkk " + user1.getHistoryDoc());

        try {
            JSONArray jobj = historyDetails.getJSONArray("document");
            System.out.println("document array : " + jobj.toString());

            for (int k = 0; k < jobj.length(); k++) {
                JSONObject jo = jobj.getJSONObject(k);
                JSONArray jarr = jo.getJSONArray("transaction");
                if (jarr.length() != 0) {

                    String DocUrl = jo.getString("doc_url");
                    String DocId = jo.getString("_id");
                    System.out.println(DocUrl);

                    for (int l = 0; l < jarr.length(); l++) {
                        JSONObject finaljob = jarr.getJSONObject(l);
                        if (finaljob.has("print_date") && finaljob.has("copies")) {

                            String printerId = finaljob.getString("printerId");
                            String customerId = finaljob.getString("customerId");
                            String printDate = finaljob.getString("print_date");
                            System.out.println("PrintDate :" + printDate);
                            System.out.println("DocumentId :" + DocId);

                            HistoryContent historyContent = new HistoryContent();

                            historyContent.setDocUrl(DocUrl);
                            historyContent.setPrinterId(printerId);
                            historyContent.setCustomerId(customerId);
                            historyContent.setDocId(DocId);
                            historyContent.setDate(printDate);


                            sendHList.add(historyContent);

                        }

                    }
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("check sendlist content  :" + sendHList);

        printButton = (Button) findViewById(R.id.printButton);
        printButton.setVisibility(Button.GONE);

        Copies = (EditText) findViewById(R.id.copiesId);
        Copies.setVisibility(Button.GONE);

        if (CloudPrint.docID != null) {
            printButton.setVisibility(View.VISIBLE);
            Copies.setVisibility(View.VISIBLE);
        }


        listViewH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryContent hcont = new HistoryContent();
                hcont = (HistoryContent) parent.getItemAtPosition(position);
                dc_id = hcont.getDocId();

                //System.out.println(" dc_id" + dc_id);
                //System.out.println("checkk dc_id" + hcont.getDocId());
                user1.setHistoryDoc(dc_id);
                // System.out.println("check dc_id GET :" + user1.getHistoryDoc());

                itemsAdapter.notifyDataSetChanged();

                System.out.println("Clicked on List Printer" + hcont.getPrinterId());
                System.out.println("Clicked on List Printer" + hcont.getCustomerId());
                // System.out.println(hcont.getPrinterSelectedCustomer());

                hc.setprinterSelectedId(hcont.getPrinterId());
                hc.setPrinterSelectedCustomer(hcont.getCustomerId());
                // mc.setPrinterSelectedPrice(mcont.getPrice());

                System.out.println("After select customer " + hc.getPrinterSelectedCustomer());
                System.out.println("After select printer " + hc.getPrinterSelectedId());
                System.out.println("After select doc ID " + hcont.getDocId());
                System.out.println("After select copies " + hcont.getPrice());

                // Setting doc id

                doc.set_id(hcont.getDocId());
                CloudPrint.docID = doc.get_id();
                System.out.println("get doc id " + doc.get_id());
                System.out.println("get doc id1 " + CloudPrint.docID);


                Toast.makeText(History.this, "Document " + hcont.getDocId() + " is selected", Toast.LENGTH_LONG).show();
                CloudPrint.docID = hcont.getDocId();
                CloudPrint.custID = hcont.getCustomerId();
                CloudPrint.printerID = hcont.getPrinterId();

                System.out.println("check selected doc" + CloudPrint.docID);
                System.out.println("check selected customer" + CloudPrint.custID + "yo" + PrinterId1);
                System.out.println("check selected printer" + CloudPrint.printerID + " yo " + CustomerId1);
                System.out.println("check selected user" + user1.get_id());
                System.out.println("check selected copies" + CloudPrint.price);

                printButton.setVisibility(View.VISIBLE);
                Copies.setVisibility(View.VISIBLE);

            }
        });


        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("in map activity " + user1.getFirstName());

                if (Copies.getText().toString().trim().equals("")) {
                    Toast.makeText(History.this, "Copies can not be empty", Toast.LENGTH_SHORT).show();
                } else {

                    if (mc1.getPrinterSelectedCustomer() == null) {
                        Toast.makeText(History.this, "Select printer ", Toast.LENGTH_SHORT).show();
                    } else {


                        if (doc.get_id() == null) {
                            Toast.makeText(History.this, "Select document ", Toast.LENGTH_SHORT).show();
                        } else {
                            mc1.setPrinterSelectedCopy(Integer.parseInt(Copies.getText().toString().trim()));
                            Log.i("copies", "no of copies" + Copies.getText().toString().trim());
                            System.out.println("Copies " + mc1.getPrinterSelectedCopy());
                            int totalamount = mc1.getPrinterSelectedCopy() * mc1.getPrinterSelectedPrice();
                            String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS

                            String alertMesssage = new String("Customer Name : " + mc1.getPrinterSelectedName1() + newLine + newLine + "Printer Name : " + mc1.getPrinterSelectedName2() + newLine + newLine + "Copies : " + mc1.getPrinterSelectedCopy() + newLine + newLine + "Price : " + mc1.getPrinterSelectedPrice() + " $" + newLine + newLine + "Total Amount : " + totalamount + " $");


                            AlertDialog dialog = new AlertDialog.Builder(History.this)
                                    .setTitle("Print Summary")
                                    .setMessage(alertMesssage)
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            mapService = new MapServiceImpl();
                                            mapService.postPrinter(user1, doc, context, mc1);
                                            System.out.println(mc1.getResponse().toString() + " Response");
                                            System.out.println(CloudPrint.docID + " Static variable");
                                            Log.i("Response", "Response  " + mc1.getResponse());
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                            printButton.setVisibility(Button.GONE);
                            Copies.setVisibility(Button.GONE);
                        }
                    }

                }


            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        return true;
    }

}
