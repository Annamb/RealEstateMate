package is.RealEstate.realEstateMate.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.FilteredProperties;
import is.RealEstate.realEstateMate.entities.Property;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private FilteredProperties mFilteredProperties;
    private PropertyAdapter mAdapter;
    @BindView(R.id.propertyList)
    ListView mPropertyList;
    @BindView(R.id.filterButton)
    Button mFilterButton;
    @BindView(R.id.linkToLoginButton)
    Button linkToLoginButton;
    @BindView(R.id.aboutButton)
    Button aboutButton;
    @BindView(R.id.homepageButton)
    Button mHomePageButton;
    @BindView(R.id.logOutButton)
    Button logOutButton;
    private SharedPreferences mPrefs;

    final String PREFERENCE_STRING = "LoggedInUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs =  getSharedPreferences(PREFERENCE_STRING, MODE_PRIVATE);
        ButterKnife.bind(this);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Logging out");
                progressDialog.show();
                System.out.println("Clear saved user preference");
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.putString("LoggedInUser", null);
                prefsEditor.commit();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mHomePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserHomePage();
            }
        });

        linkToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FilterFragment fragment =  new FilterFragment();
                fm.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        getProperties();
        mPropertyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Ýtt á eign");
                List<Property> PropertyList = mFilteredProperties.getProperties();
                Property property = PropertyList.get(i);
                System.out.println(property.streetName);
                openPropertyPage(property);
            }
        });
    }

    /**
     * Opnar síðu sem inniheldur frekari upplýsingar um eign
     * @param property
     */
    private void openPropertyPage(Property property) {
        Intent intent =new Intent(this, PropertyActivity.class);
        intent.putExtra("propertyName", property.getStreetName());
        intent.putExtra("propertyNumber", property.getStreetNumber());
        intent.putExtra("prize", property.getPrice());
        intent.putExtra("zip", property.getZip());
        intent.putExtra("town", property.getTown());
        intent.putExtra("size", property.getPropertySize());
        intent.putExtra("bathrooms", property.getBathrooms());
        intent.putExtra("rooms", property.getRooms());
        intent.putExtra("type", property.getCategory());
        intent.putExtra("image1",property.getImage1());
        intent.putExtra("image2",property.getImage2());
        intent.putExtra("image3",property.getImage3());
        intent.putExtra("propertyID",property.getPropertyID());

        startActivity(intent);

    }

    /**
     * Opnar homepage fyrir notanda
     */
    private void openUserHomePage() {
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }

    /**
     * Opnar login síðu
     */
    private void openLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Býr til request með filterum fyrir leit að eignum
     * @param town
     * @param zip
     * @param bedrooms
     * @param price
     * @param size
     * @param category
     * @param bathrooms
     */
    public void getFilteredProperties(String town, String zip, String bedrooms, String price, String size, String category, String bathrooms) {

            RequestBody formBody = new FormBody.Builder()
                    .add("town", town)
                    .add("zip", zip)
                    .add("bedrooms", bedrooms)
                    .add("price", price)
                    .add("size", size)
                    .add("category", category)
                    .add("bathrooms", bathrooms)
                    .build();

            Request request = new Request.Builder()
                    .url("http://10.0.2.2:9090/search")
                    .post(formBody)
                    .build();

            callBackend(request);
    }

    /**
     * Býr til request sem sækir allar eignir í gagnagrunni
     */
    public void getProperties() {
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:9090/seeAll")
                    .build();

            callBackend(request);
    }

    /**
     * Kallar á bakenda með requesti
     * @param request
     */
    private void callBackend(Request request){
            OkHttpClient client = new OkHttpClient();

        if(isNetworkAvailable()) {
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mFilteredProperties = parsePropertyListDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Uppfærir listann yfir eignir á upphafssíðu
     */
    private void updateDisplay() {
        mAdapter = new PropertyAdapter(this, mFilteredProperties.getProperties());

        mPropertyList.setAdapter(mAdapter);
    }

    /**
     * Býr til tilvik af FilteredProperties sem geymir upplýsingar um "núverandi" eignir sem eru birtar út frá gögnum frá bakenda
     * @param jsonData
     * @return
     * @throws JSONException
     */
    private FilteredProperties parsePropertyListDetails(String jsonData) throws JSONException{

        FilteredProperties filteredProperties = new FilteredProperties();

        ArrayList<Property> properties =new ArrayList<Property>();
        JSONArray array=new JSONArray(jsonData);
        for(int i=0;i<array.length();i++){
            JSONObject elem=(JSONObject)array.get(i);
            Property property=new Property(elem.getLong("propertyID"),
                    elem.getLong("bedrooms"),
                    elem.getLong("bathrooms"),
                    elem.getString("streetName"),
                    elem.getString("streetNumber"),
                    elem.getLong("zip"),
                    elem.getString("town"),
                    elem.getLong("price"),
                    elem.getLong("propertySize"),
                    elem.getString("category"),
                    elem.getLong("rooms"),
                    elem.getLong("latitude"),
                    elem.getLong("longitude"),
                    elem.getLong("agentID"),
                    elem.getLong("sellerID"),
                    elem.getString("image1"),
                    elem.getString("image2"),
                    elem.getString("image3"),
                    elem.getString("image4"));
            properties.add(property);
        }

        filteredProperties.setProperties(properties);

        return filteredProperties;
    }

    /**
     * Athugar hvort network sé aðgengilegt
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }

    /**
     * Upplýsir notanda ef um villu er að ræða
     */
    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
