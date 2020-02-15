package com.example.bikashvoting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikashvoting.R;
import com.example.bikashvoting.Url.Url;
import com.example.bikashvoting.response.User;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotesAdapter extends  RecyclerView.Adapter<VotesAdapter.VotesViewHolder>{

    Context context;
    List<User> usersList;

    public VotesAdapter(Context context, List<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public VotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.votes_list, parent, false);
        return new VotesAdapter.VotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotesViewHolder holder, int position) {
            User votes=usersList.get(position);
            holder.name.setText(""+votes.getFirstName()+" "+votes.getLastName());
            holder.votes.setText("Votes: "+votes.getVotes().length);

        String image = Url.uploads + votes.getImage();
        if (votes.getImage()==null){
            holder.circleImageView.setImageResource(R.drawable.noimage);
        }else {
            try {
                URL url = new URL(image);

                Bitmap imageBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

                holder.circleImageView.setImageBitmap(imageBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class VotesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView name,votes;
        public VotesViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.canImageVote);
            name=itemView.findViewById(R.id.canNameVote);
            votes=itemView.findViewById(R.id.tvvotes);
        }
    }
}
