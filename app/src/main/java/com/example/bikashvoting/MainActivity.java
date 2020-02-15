package com.example.bikashvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.response.LoginResponse;
import com.example.bikashvoting.response.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    EditText etuser,etpass;
    Button btnlogin, btnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etuser=findViewById(R.id.etUsername);
        etpass=findViewById(R.id.etPassword);
        btnlogin=findViewById(R.id.btnLogin);
        btnregister=findViewById(R.id.btnRegister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), register_vote_app.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username=etuser.getText().toString();
                String Password=etpass.getText().toString();
                User user=new User(Username,Password);

                UserApi userApi= Url.getInstance().create(UserApi.class);
                Call<LoginResponse> loginResponseCall=userApi.checkUser(user);

                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Username or password error", Toast.LENGTH_SHORT).show();
                        }
                        if(response.body().getCode()==200){
                          //  Toast.makeText(MainActivity.this, "voter Login", Toast.LENGTH_SHORT).show();
                            Url.token+=response.body().getToken();
                            Intent intent=new Intent(getApplicationContext(), VotDashboardActivity.class);

                            startActivity(intent);
                        }else if(response.body().getCode()==201){
                           // Toast.makeText(MainActivity.this, "candidate login", Toast.LENGTH_SHORT).show();
                            Url.token+=response.body().getToken();
                            Intent intent=new Intent(getApplicationContext(),candidate_dashbord.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, ""+ response.body().getStatus(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "throw "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
