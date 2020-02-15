package com.example.bikashvoting.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.R;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.adapter.CandidateAdapter;
import com.example.bikashvoting.response.LoginResponse;
import com.example.bikashvoting.response.User;
import com.example.bikashvoting.strictMode.StrictModeClass;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

  public static String loggedUserId;
    private HomeViewModel homeViewModel;
    CircleImageView image;
    TextView username;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
         image=root.findViewById(R.id.circleLoggedImage);
         username=root.findViewById(R.id.tvloggedName);
            recyclerView=root.findViewById(R.id.candidateUserList);
            getLoggedUser();
                getUserList();

        return root;
    }



    private  void getUserList(){
        StrictModeClass.StrictMode();
        UserApi userApi= Url.getInstance().create(UserApi.class);
        final Call<List<User>> getUserListCall=userApi.getUserList(Url.token);
        getUserListCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    List<User> candidateUserList=response.body();
                    CandidateAdapter candidateAdapter=new CandidateAdapter(getContext(),candidateUserList);
                    recyclerView.setAdapter(candidateAdapter);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    loggedUserId=response.body().get_id();
                    username.setText("Welcome: "+response.body().getFirstName()+response.body().getLastName());

                    String imagepath = Url.uploads + response.body().getImage();
                        System.out.println(imagepath);
                    if (response.body().getImage()==null){
                        image.setImageResource(R.drawable.noimage);
                    }else {
                        try {
                            URL url = new URL(imagepath);
                            Bitmap imageBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                            image.setImageBitmap(imageBitmap);

                          // Picasso.with(getContext()).load(imagepath).into(image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}