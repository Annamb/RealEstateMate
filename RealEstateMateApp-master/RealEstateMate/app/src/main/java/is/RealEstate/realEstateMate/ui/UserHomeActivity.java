package is.RealEstate.realEstateMate.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.FilteredOffers;
import is.RealEstate.realEstateMate.entities.FilteredProperties;
import is.RealEstate.realEstateMate.entities.Offer;
import is.RealEstate.realEstateMate.entities.Property;
import is.RealEstate.realEstateMate.entities.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserHomeActivity extends AppCompatActivity {
    public static final String TAG = UserHomeActivity.class.getSimpleName();
    private FilteredProperties mFilteredProperties;
    private FilteredOffers mFilteredOffers;
    private PropertyAdapter mAdapter;
    private OfferAdapter mAdapter2;
    private User user;
    private SharedPreferences mPrefs;
    final String PREFERENCE_STRING = "LoggedInUser";
    private int userID = 0;

    @BindView(R.id.propertyList)
    ListView mPropertyList;
    @BindView(R.id.offerList)
    ListView mOfferList;
    @BindView(R.id.usernameTextView)
    TextView mUserNameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs =  getSharedPreferences(PREFERENCE_STRING, MODE_PRIVATE);
        user = new User("TempUser", "temppass", "temp@mail.com");
        String json = mPrefs.getString("LoggedInUser", "");
        if (json == null)
            System.out.println("hdfka;dflkajsf;lkajdsf;laksdjf;adslkfjas;lfkja;dlfkfjasf;l");
        System.out.println(json);
        try{
            parseUserData(json);
        }catch (JSONException e){
            Log.e(TAG, "JSON caught: ", e);
            goToLogin();
        }

        setContentView(R.layout.activity_userhome);
        ButterKnife.bind(this);
        getProperties();
        getOffers();
        mUserNameTextView.setText(user.getUserName());
    }

    /**
     * Býr til request til þess að sækja núverandi logged in user
     */
    private void getSessionUser(){
        Request request = new Request.Builder()
                .url("http://10.0.2.2:9090/loggedin")
                .build();

        callBackend(request);
    }

    /**
     * Býr til request til þess að sækja allar eignir í gagnagrunn
     */
    private void getProperties() {
        Request request = new Request.Builder()
                .url("http://10.0.2.2:9090/seeAll")
                .build();

        callBackend(request);
    }

    /**
     * Býr til request til þess að sækja öll offer í gagnagrunn
     */
    private void getOffers() {
        Request request = new Request.Builder()
                .url("http://10.0.2.2:9090/Offers")
                .build();

        callBackendOffers(request);
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

    /**
     * Kallar á bakenda með request til þess að sækja lista yfir eignir núverandi logged in notanda
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
     * Kallar á bakenda til þess að sækja upplýsingar um tilboð sem núverandi notandi hefur gert
     * @param request
     */
    private void callBackendOffers(Request request){
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
                            mFilteredOffers = parseOfferListDetails(jsonData);

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
     * Uppfærir property og offer listview-in
     */
    private void updateDisplay() {
        mAdapter = new PropertyAdapter(this, mFilteredProperties.getProperties());

        mPropertyList.setAdapter(mAdapter);

        mAdapter2 = new OfferAdapter(this, mFilteredOffers.getOffers());

        mOfferList.setAdapter(mAdapter2);
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
            if (elem.getLong("sellerID") == userID ) {
                Property property = new Property(elem.getLong("propertyID"),
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
        }

        filteredProperties.setProperties(properties);

        return filteredProperties;
    }

    /**
     * Býr til tilvik af FilteredOffers sem geymir upplýsingar um hvaða tilboð notandinn er skráður fyrir út frá gögnum frá bakenda
     * @param jsonData
     * @return
     * @throws JSONException
     */
    private FilteredOffers parseOfferListDetails(String jsonData) throws JSONException{
        FilteredOffers filteredOffers = new FilteredOffers();

        ArrayList<Offer> offers =new ArrayList<Offer>();
        JSONArray array=new JSONArray(jsonData);
        for(int i=0;i<array.length();i++){
            JSONObject elem=(JSONObject)array.get(i);
            if (elem.getLong("userID") == userID ) {
                Offer offer = new Offer(elem.getLong("offerID"),
                        elem.getLong("propertyID"),
                        elem.getLong("offerAmount"),
                        elem.getLong("userID"));
                System.out.println(elem.getLong("offerAmount"));
                offers.add(offer);
            }
        }

        filteredOffers.setOffers(offers);

        return filteredOffers;
    }

    /**
     * Býr til tilvik af user út frá gögnum frá bakenda
     * @param userData
     * @throws JSONException
     */
    private void parseUserData(String userData) throws JSONException {
        if (userData == null){
            goToMain();
        }
        JSONObject jsonObk= new JSONObject(userData);
        JSONObject json = jsonObk.getJSONObject("user");
        System.out.println(json);
        userID = json.getInt("id");
        user =  new User(userID, json.get("userName").toString(),
                json.get("userPassword").toString(),
                json.get("userEmail").toString());
    }

    /**
     * Færir okkur yfir á login page
     */
    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Færir okkur yfir á upphafssíðu
     */
    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
