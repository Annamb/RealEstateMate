package is.RealEstate.realEstateMate.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeOfferFragment extends Fragment {

    public static final String TAG = MakeOfferFragment.class.getSimpleName();
    final String PREFERENCE_STRING = "LoggedInUser";
    public boolean svar=true;
    private User user;
    private SharedPreferences mPrefs;
    private int userID = 0;


    public MakeOfferFragment() {

    }

    public static MakeOfferFragment newInstance(String param1, String param2) {
        MakeOfferFragment fragment = new MakeOfferFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Context sótt til að nota sharedPrefrences til að sækja loggedin user.
         */
        Context context = getActivity();
        mPrefs = context.getSharedPreferences(PREFERENCE_STRING, context.MODE_PRIVATE);
        user = new User("TempUser", "temppass", "temp@mail.com");
        String json = mPrefs.getString("LoggedInUser", "");
        /*
         * Breytum json strengnum yfir í user með parseData(json).
         * Ef enginn er loggaður inn er redirectað á login(ætti ekki að geta gerst)
         */
        try{
            parseUserData(json);
        }catch (JSONException e){
            Log.e(TAG, "JSON caught: ", e);
        }

    }

    /**
     * View búið til og Onclick listeners settir á takka.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Viðmótsbreytur sóttar ásamt propertyID
        View v = inflater.inflate(R.layout.fragment_make_offer, container, false);
        Button b = (Button) v.findViewById(R.id.makeOfferButtonID);
        final TextView noUser = (TextView) v.findViewById(R.id.noUser);
        final Button goToLogin = (Button) v.findViewById(R.id.goToLogin);
        final TextInputLayout offerAmount = (TextInputLayout) v.findViewById(R.id.OfferAmount);
        final String propertyIDdata = getArguments().getString("data");
        final Long propertyID = Long.parseLong(propertyIDdata);
        System.out.println(propertyID);

        // On click listener settur á button b sem sér um að búa til tilboð
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notandi ætti að vera loggaður inn en alltaf gott að athuga aftur :P
                if(isLoggedIn()) {
                    System.out.println("Tilbod gert");
                    Random rand = new Random();
                    int offerID = rand.nextInt(1000);
                    String of = offerID + " ";
                    System.out.println(offerAmount.getEditText().getText().toString());
                    String u = offerAmount.getEditText().getText().toString();

                    // Kallað á fallið SaveOffer sem tengist bakenda og býr til offer í gagnagrunni.
                    SaveOffer(of, propertyIDdata, u);

                    PropertyActivity activity = (PropertyActivity) getActivity();
                    activity.makeOfferButton.isPressed();
                    activity.congratulations.setVisibility(View.VISIBLE);
                    activity.congratulations.setText("Congratulations you have made an offer on this property!");
                    closeFragment();
                }else{
                    noUser.setText("You have to be logged in to make a offer!");
                    goToLogin.setVisibility(View.VISIBLE);
                }
            }});
        return v;
    }

    /**
     * Fall sem lokar núverandi fragmenti
     */
    public void closeFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    /**
     * Fall sem kallar á fallið SaveOffer í bakenda sem save-ar offer í gagnagrunn
     * @param offerID
     * @param propertyID
     * @param upph
     */
    public void SaveOffer(String offerID,String propertyID,String upph){
        System.out.println(propertyID);
        System.out.println(upph);
        RequestBody formBody = new FormBody.Builder()
                .add("offerID", offerID)
                .add("pid", propertyID)
                .add("offerAmount", upph)
                .add("userid", String.valueOf(user.getId()))
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:9090/SaveOffer")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("bilad");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("virkar");
            }
        });
    }


    /**
     * Athugar hvort notandi sé loggaður inn.
     * Skilar true eða false.
     * @return
     */
    private boolean isLoggedIn() {

        if(user.getUserName()=="TempUser"){
            svar=false;
        }else{
            System.out.println("userinn er loggadur inn");
            svar=true;
        }
        return svar;
    }

    /**
     * Fall sem tekur inn streng á json formati og býr til user úr upplýsingunum.
     * @param userData
     * @throws JSONException
     */
    private void parseUserData(String userData) throws JSONException {
        JSONObject jsonObk= new JSONObject(userData);
        JSONObject json = jsonObk.getJSONObject("user");
        System.out.println(json);
        userID = json.getInt("id");
        user =  new User(userID, json.get("userName").toString(),
                json.get("userPassword").toString(),
                json.get("userEmail").toString());
    }

}
