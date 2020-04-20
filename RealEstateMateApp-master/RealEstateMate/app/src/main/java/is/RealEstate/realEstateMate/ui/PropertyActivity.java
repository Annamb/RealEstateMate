package is.RealEstate.realEstateMate.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.User;

public class PropertyActivity extends AppCompatActivity {

    @BindView(R.id.makeOfferButton) Button makeOfferButton;
    @BindView(R.id.congratulations) TextView congratulations;
    @BindView(R.id.toLogin) Button toLogin;
    @BindView(R.id.youHaveToLogIn) TextView youHaveToLogIn;

    public Long propertyID;
    public static final String TAG = PropertyActivity.class.getSimpleName();
    private User user;
    private SharedPreferences mPrefs;
    final String PREFERENCE_STRING = "LoggedInUser";
    private int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        ButterKnife.bind(this);

        //Náð í upplýsingar úr sharedPrefrences og prufu user búinn til
        mPrefs =  getSharedPreferences(PREFERENCE_STRING, MODE_PRIVATE);
        user = new User("TempUser", "temppass", "temp@mail.com");
        String json = mPrefs.getString("LoggedInUser", "");

        /*
         * Reynt að breyta upplýsingunum yfir í user hlut með fallinu parseUseData(json).
         * Ekkert gert ef það virkar ekki.
         */
        try{
            parseUserData(json);
        }catch (JSONException e){
            Log.e(TAG, "JSON caught: ", e);
        }

        // Takkinn toLogin gerður visible en breytt í home takka og vísar á MainActivity.
        toLogin.setVisibility(View.VISIBLE);
        toLogin.setText("Home");
        toLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToMain();
            }
        });

        // Myndir sóttar úr intent og url-in sett í strengjafylki
        String imageId1 = (String) getIntent().getSerializableExtra("image1");
        String url1 = "http://10.0.2.2:9090/Image/" + imageId1;
        String imageId2 = (String) getIntent().getSerializableExtra("image2");
        String url2 = "http://10.0.2.2:9090/Image/" + imageId2;
        String imageId3 = (String) getIntent().getSerializableExtra("image3");
        String url3 = "http://10.0.2.2:9090/Image/" + imageId3;
        System.out.println(url1);
        String[] imageUrls = new String[]{url1,url2,url3};

        // Myndirnar settar inn í viewPager í viðmóti svo hægt sé að skrolla í gegnum þær
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);

        //Upplýsingar um eign sóttar í Intent
        propertyID = (Long) getIntent().getSerializableExtra("propertyID");
        String propertyName = (String) getIntent().getSerializableExtra("propertyName");
        String propertyNumber = (String) getIntent().getSerializableExtra("propertyNumber");
        Long prize =  (Long) getIntent().getSerializableExtra("prize");

        System.out.println(prize);
        String streetName = propertyName + " " + propertyNumber;
        System.out.println(propertyName);
        TextView street = (TextView) findViewById(R.id.street);
        street.setText(streetName);

        Long zip =  (Long) getIntent().getSerializableExtra("zip");
        String town = (String) getIntent().getSerializableExtra("town");

        String postal = zip + " " + town;
        TextView postalcode = (TextView) findViewById(R.id.postalcode);
        postalcode.setText(postal);

        String prizet = prize.toString()+" kr." ;
        TextView prizetext = (TextView) findViewById(R.id.prize);
        prizetext.setText(prizet);


        // Upplýsingar í töflu
        Long size =  (Long) getIntent().getSerializableExtra("size");
        Long bathrooms =  (Long) getIntent().getSerializableExtra("bathrooms");
        Long rooms =  (Long) getIntent().getSerializableExtra("rooms");
        String type = (String) getIntent().getSerializableExtra("type");

        // Fyllt út í viðmót
        TextView sizetext = (TextView) findViewById(R.id.sizetext);
        String sizet = size.toString() + " m^2";
        sizetext.setText(sizet);
        TextView roomstext = (TextView) findViewById(R.id.roomstext);
        roomstext.setText(rooms.toString());
        TextView bathroomstext = (TextView) findViewById(R.id.bathroomstext);
        bathroomstext.setText(bathrooms.toString());
        TextView typetext = (TextView) findViewById(R.id.typetext);
        typetext.setText(type);

        /* Þegar ýtt er á makeOfferButton..
        *
        * Notandi loggaður inn:
        * Kallað á MakeOfferFragment og propertyID sett í bundle með
        *
        * Notandi ekki loggaður inn:
        * Button toLogin breytist úr 'Home' í 'Sign in' og vísar á Login Activity og
        * texti birtist í textView youHaveToLogIn.
        *
        * */
        makeOfferButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("ytt a make offer");

                mPrefs =  getSharedPreferences(PREFERENCE_STRING, MODE_PRIVATE);
                user = new User("TempUser", "temppass", "temp@mail.com");
                String json = mPrefs.getString("LoggedInUser", "");

               if(json==null) {
               }
               try{
                    parseUserData(json);
                }catch (JSONException e){
                    Log.e(TAG, "JSON caught: ", e);
                    youHaveToLogIn.setVisibility(View.VISIBLE);
                    toLogin.setVisibility(View.VISIBLE);
                    toLogin.setText("Sign in");
                    toLogin.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            goToLogin();
                        }
                    });
                }

                /*
                * Ef userinn heitir enn TempUser þá er notandi loggaður út.
                * Ef hann er ekki TempUser þá er notandi loggaður inn og MakeOfferFragment opnað.
                */
                if(user.getUserName() != "TempUser") {
                    FragmentManager fm = getSupportFragmentManager();
                    MakeOfferFragment fragment = new MakeOfferFragment();
                    Bundle data = new Bundle();//Use bundle to pass data
                    data.putString("data", propertyID.toString());//put string, int, etc in bundle with a key value
                    fragment.setArguments(data);//Finally set argument bundle to fragment

                    fm.beginTransaction().replace(R.id.activity_container, fragment).commit();
                }
            }
        });
    }

    /**
     * Streng úr sharedPrefrences breytt í notanda.
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
     * Opnar LoginActivity
     */
    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Opnar MainActivity
     */
    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
}
