package com.example.bikashvoting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikashvoting.Apis.UserApi;
import com.example.bikashvoting.R;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.response.User;
import com.example.bikashvoting.response.VoteResponse;
import com.example.bikashvoting.ui.home.HomeFragment;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidateAdapter extends  RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>{

    Context context;
    String candidateId;
    List<User> usersList;

    public CandidateAdapter(Context context, List<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_list, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
         final User candidateUser = usersList.get(position);
         candidateId=candidateUser.get_id();
         holder.canTextView.setText(""+candidateUser.getFirstName()+candidateUser.getLastName());
         holder.votes.setText(""+candidateUser.getVotes().length);
       String image = Url.uploads + candidateUser.getImage();
       if (candidateUser.getImage()==null){
           holder.canImage.setImageResource(R.drawable.noimage);
       }else {
           try {
               URL url = new URL(image);

               Bitmap imageBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

               holder.canImage.setImageBitmap(imageBitmap);

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       holder.btnVote.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            vote();
           }
       });
    }

    private void vote() {
        UserApi userApi= Url.getInstance().create(UserApi.class);
        Call<VoteResponse> voteResponseCall=userApi.vote(candidateId,Url.token);
        voteResponseCall.enqueue(new Callback<VoteResponse>() {
            @Override
            public void onResponse(Call<VoteResponse> call, Response<VoteResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VoteResponse> call, Throwable t) {
                Toast.makeText(context, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder{
            CircleImageView canImage;
            TextView canTextView;
            Button btnVote;
            TextView votes;
        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            votes=itemView.findViewById(R.id.votervote);
            canImage=itemView.findViewById(R.id.canImage);
            canTextView=itemView.findViewById(R.id.canName);
            btnVote=itemView.findViewById(R.id.btnVote);

        }
    }

}
