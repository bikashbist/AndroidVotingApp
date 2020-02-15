package com.example.bikashvoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.adapter.CandidateAdapter;
import com.example.bikashvoting.response.User;
import com.example.bikashvoting.strictMode.StrictModeClass;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class candidate_dashbord extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView textView;
    public static String loggedUserId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_dashbord);
        circleImageView=findViewById(R.id.cili);
        textView=findViewById(R.id.tvluc);
        recyclerView=findViewById(R.id.recyclevotes);
        getLoggedUser();
    }


    private  void getUserList(){
        StrictModeClass.StrictMode();
        UserApi userApi= Url.getInstance().create(UserApi.class);
        final Call<List<User>> getUserListCall=userApi.getUserList();
        getUserListCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(candidate_dashbord.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    List<User> candidateUserList=response.body();
                    //System.out.println();
                    //CandidateAdapter candidateAdapter=new CandidateAdapter(candidate_dashbord.this,candidateUserList);
                    //recyclerView.setAdapter(candidateAdapter);

                    //recyclerView.setLayoutManager(new LinearLayoutManager(candidate_dashbord.this, LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(candidate_dashbord.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getLoggedUser(){
        StrictModeClass.StrictMode();
        UserApi userApi= Url.getInstance().create(UserApi.class);
        Call<User> getUserCall=userApi.getUser(Url.token);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(candidate_dashbord.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    loggedUserId=response.body().get_id();
                    textView.setText("Welcome: "+response.body().getFirstName()+response.body().getLastName());

                    String imagepath = Url.uploads + response.body().getImage();
                    System.out.println(imagepath);
                    if (response.body().getImage()==null){
                        circleImageView.setImageResource(R.drawable.noimage);
                    }else {
                        try {
                            URL url = new URL(imagepath);
                            Bitmap imageBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                            circleImageView.setImageBitmap(imageBitmap);

                            // Picasso.with(getContext()).load(imagepath).into(image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(candidate_dashbord.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
