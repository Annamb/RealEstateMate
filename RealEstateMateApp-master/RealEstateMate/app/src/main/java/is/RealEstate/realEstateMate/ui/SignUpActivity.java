package is.RealEstate.realEstateMate.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import is.RealEstate.realEstateMate.R;
import is.RealEstate.realEstateMate.entities.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.signUpButton)
    Button signUpButton;
    @BindView(R.id.input_name)
    EditText userNameInput;
    @BindView(R.id.input_email)
    EditText userEmailInput;
    @BindView(R.id.input_password)
    EditText passwordInput;
    @BindView(R.id.input_reEnterPassword)
    EditText rePasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);


        final EditText finalPassword = passwordInput;
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalPassword != rePasswordInput) {
                    System.out.println("password should match! But doesn't really matter.");
                }
                System.out.println("Smellt á Búa til aðgang");
                String username = userNameInput.getText().toString();
                String email = userEmailInput.getText().toString();
                String rePass = rePasswordInput.getText().toString();
                System.out.println("username:"+ username);
                System.out.println("email:"+ email);
                System.out.println("password:"+ rePass);
                User newUser = new User(username, rePass, email);
                saveUser(newUser);

                // Búið að búa til account og notandi fluttur yfir á login page
                goToLoginPage();
            }

        }

        );
    }

    /**
     * Býr til request með upplýsingum um nýjan notanda
     * @param newUser
     */
    private void saveUser(User newUser) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Býr til notanda fyrir þig");
        progressDialog.show();
        String URL_DATA = "http://10.0.2.2:9090/saveUser";
        RequestBody formBody = new FormBody.Builder()
                .add("username", newUser.getUserName())
                .add("emailaddress", newUser.getUserEmail())
                .add("password", newUser.getUserPassword())
                .build();

        Request request = new Request.Builder()
                .url(URL_DATA)
                .post(formBody)
                .build();
        callBackend(request);
    }

    /**
     * Kallar í bakenda til að vista nýjan user
     * @param request
     */
    private void callBackend(Request request){
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
     * Færir okkur yfir á loginpage
     */
    private void goToLoginPage() {
        System.out.println("Notandi fluttur yfir á login page");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

