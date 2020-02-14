package com.example.bikashvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.response.LoginResponse;
import com.example.bikashvoting.response.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register_vote_app extends AppCompatActivity {


    EditText fname,lname,user,pass;
    RadioButton can,vot;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vote_app);
        fname=findViewById(R.id.f_name);
        lname=findViewById(R.id.l_name);
        user=findViewById(R.id.e_username);
        pass=findViewById(R.id.p_password);
        can=findViewById(R.id.can);
        vot=findViewById(R.id.vot);
        register=findViewById(R.id.b_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=null;
                String firstname=fname.getText().toString();
                String lastname=lname.getText().toString();
                String username=user.getText().toString();
                String password=pass.getText().toString();

                if (can.isChecked()){
                    type="candidate";
                }else if (vot.isChecked()){
                    type="voter";
                }
                User user=new User(firstname,lastname,username,password,type);
                UserApi userApi= Url.getInstance().create(UserApi.class);
                Call<Void> regresponse=userApi.registerUser(user);
                regresponse.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(register_vote_app.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                        }else {

                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(register_vote_app.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


    }
}
