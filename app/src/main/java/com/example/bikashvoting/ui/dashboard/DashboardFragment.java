package com.example.bikashvoting.ui.dashboard;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.MainActivity;
import com.example.bikashvoting.ProfileCanActivity;
import com.example.bikashvoting.R;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.notification.CreateChannel;
import com.example.bikashvoting.response.ImageResponse;
import com.example.bikashvoting.response.User;
import com.example.bikashvoting.strictMode.StrictModeClass;

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

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {

    CircleImageView circleImageView;
    Button textView, btnUpdate;
    EditText firstname, lastname;
    Uri uri;
    String imagePath = "";
    String imageName = "";

    private DashboardViewModel dashboardViewModel;
    private SensorManager sensorManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        circleImageView = root.findViewById(R.id.circleImageViewUpdateVoter);
        textView = root.findViewById(R.id.btnupdateImageVoter);
        btnUpdate = root.findViewById(R.id.btnUpdateProfileVoter);
        firstname = root.findViewById(R.id.etfirstnameUpdateVoter);
        lastname = root.findViewById(R.id.etlastnameUpdateVoter);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);





        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                String xAxis = "x: " + values[0];
                String yAxis = "y: " + values[1];
                String zAxis = "z: " + values[2];
                float x = values[0];

                if (x > 3) {
                    Url.token = "Bearer ";
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                System.out.println(xAxis);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (sensor != null) {
            sensorManager.registerListener(sensorEventListener, sensor, sensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(getContext(), "Sensor not found", Toast.LENGTH_SHORT).show();
        }




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

        return root;
    }


    //permission for camera and external storage
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0
            );
        }
    }

    private void updateProfile() {
        String fname = firstname.getText().toString();
        final String lname = lastname.getText().toString();
        User user = new User(fname, lname, "voter");

        UserApi userApi = Url.getInstance().create(UserApi.class);
        Call<User> updateUserCall = userApi.updateUser(Url.token, user);

        updateUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    User user1 = response.body();
                    firstname.setText("" + user1.getFirstName());
                    lastname.setText("" + user1.getLastName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            Toast.makeText(getContext(), "Image uploaded " + imageName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //update image name
    private void updateImageName() {
        StrictModeClass.StrictMode();
        UserApi userApi = Url.getInstance().create(UserApi.class);
        User user = new User(imageName);
        Call<User> updateUserCall = userApi.updateUser(Url.token, user);
        updateUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "" + response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    circleImageView.setImageURI(uri);
                    Toast.makeText(getContext(), "Image updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //open gallery
    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    //set image in circle image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "Please select an image ", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            uri = data.getData();
            circleImageView.setImageURI(uri);
            imagePath = getRealPathFromUri(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getting image path
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(),
                uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }


    private void getLoggedUser() {
        StrictModeClass.StrictMode();
        UserApi userApi = Url.getInstance().create(UserApi.class);
        Call<User> getUserCall = userApi.getUser(Url.token);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    //loggedUserId=response.body().get_id();
                    String imagepath = Url.uploads + response.body().getImage();
                    firstname.setText(response.body().getFirstName());
                    lastname.setText(response.body().getLastName());
                    System.out.println(imagepath);
                    if (response.body().getImage() == null) {
                        circleImageView.setImageResource(R.drawable.noimage);
                    } else {
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
                Toast.makeText(getContext(), "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}