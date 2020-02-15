package com.example.bikashvoting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.notification.CreateChannel;
import com.example.bikashvoting.response.ImageResponse;
import com.example.bikashvoting.response.User;
import com.example.bikashvoting.strictMode.StrictModeClass;
import com.example.bikashvoting.ui.home.HomeFragment;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCanActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    EditText firstname,lastname;
    Button btnUpdate;
    TextView textView;
    String imagePath = "";
    String imageName="";

    Button deleteaccount;
    Uri uri;
    int counter=0;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        CreateChannel createChannel = new CreateChannel(this);
        createChannel.createChannel();
        setContentView(R.layout.activity_profile_can);
        circleImageView=findViewById(R.id.circleImageViewUpdate);
        textView=findViewById(R.id.btnupdateImage);
        deleteaccount=findViewById(R.id.deleteAccount);
        firstname=findViewById(R.id.etfirstnameUpdate);
        lastname=findViewById(R.id.etlastnameUpdate);
        btnUpdate=findViewById(R.id.btnUpdateProfile);

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        checkPermission();
        getLoggedUser();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageOnly();
                updateImageName();
            }
        });
    }

    private void deleteUser() {
        UserApi userApi= Url.getInstance().create(UserApi.class);
        Call<User> deleteUser=userApi.deleteUser(Url.token);
        deleteUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ProfileCanActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    Url.token="Bearer ";



                    Notification notification = new NotificationCompat.Builder(ProfileCanActivity.this, CreateChannel.CHANNEL_1)
                            .setSmallIcon(R.drawable.notification_foreground)
                            .setContentTitle("Delete Me")
                            .setContentText("You cannot login with this account!!")
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .build();
                    counter = counter + 1;

                    notificationManagerCompat.notify(counter, notification);


                    Intent intent=new Intent(ProfileCanActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    private void updateProfile() {
        String fname=firstname.getText().toString();
        final String lname=lastname.getText().toString();
        User user=new User(fname,lname,"candidate");

        UserApi userApi= Url.getInstance().create(UserApi.class);
        Call<User> updateUserCall=userApi.updateUser(Url.token,user);

        updateUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ProfileCanActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    User user1=response.body();
                    firstname.setText(""+user1.getFirstName());
                    lastname.setText(""+user1.getLastName());
                    DisplayPopUpNotification();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileCanActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //permission for camera and external storage
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0
            );
        }
    }


    //upload image
    private void saveImageOnly() {
        StrictModeClass.StrictMode();
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profileImage",
                file.getName(), requestBody);

        UserApi userInterface = Url.getInstance().create(UserApi.class);

        Call<ImageResponse> uploadImage = userInterface.uploadImage(body);
        try {
            Response<ImageResponse> imageResponseResponse = uploadImage.execute();

            imageName = imageResponseResponse.body().getFilename();
            System.out.println(imageName);
            Toast.makeText(this, "Image uploaded " + imageName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisplayPopUpNotification() {
        // Notification notification=new NotificationCompat().Builder(this,CreateChannel.CHANNEL_1).
        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.notification_foreground)
                .setContentTitle("UpdateProfile")
                .setContentText("Update success")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        counter = counter + 1;

        notificationManagerCompat.notify(counter, notification);
    }




    //update image name
    private  void updateImageName(){
        StrictModeClass.StrictMode();
        UserApi userApi= Url.getInstance().create(UserApi.class);
        User user=new User(imageName);
        Call<User> updateUserCall=userApi.updateUser(Url.token,user);
        updateUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               if (!response.isSuccessful()){
                    Toast.makeText(ProfileCanActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                }else{
                   circleImageView.setImageURI( uri );
                   Toast.makeText(ProfileCanActivity.this, "Image updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileCanActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    //open gallery
    private void BrowseImage() {
        Intent intent = new Intent( Intent.ACTION_PICK );
        intent.setType( "image/*" );
        startActivityForResult( intent, 0 );
    }

    //set image in circle image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText( this, "Please select an image ", Toast.LENGTH_SHORT ).show();
            }
        }
        try{  uri = data.getData();
            circleImageView.setImageURI( uri );
            imagePath = getRealPathFromUri( uri );}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //getting image path
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader( getApplicationContext(),
                uri, projection, null, null, null );
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
        cursor.moveToFirst();
        String result = cursor.getString( colIndex );
        cursor.close();
        return result;
    }


    private void getLoggedUser(){
        StrictModeClass.StrictMode();
        UserApi userApi= Url.getInstance().create(UserApi.class);
        Call<User> getUserCall=userApi.getUser(Url.token);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ProfileCanActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    //loggedUserId=response.body().get_id();
                    String imagepath = Url.uploads + response.body().getImage();
                    firstname.setText(response.body().getFirstName());
                    lastname.setText(response.body().getLastName());
                    System.out.println(imagepath);
                    if (response.body().getImage()==null){
                        circleImageView.setImageResource(R.drawable.noimage);
                    }else {
                        try {
                            URL url = new URL(imagepath);
                            Bitmap imageBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                            circleImageView.setImageBitmap(imageBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileCanActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
