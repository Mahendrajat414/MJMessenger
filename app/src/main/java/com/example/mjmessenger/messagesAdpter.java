package com.example.mjmessenger;

import static com.example.mjmessenger.chatWin.reciverIImg;
import static com.example.mjmessenger.chatWin.senderImg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdpter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModelclass> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public messagesAdpter(Context context, ArrayList<msgModelclass> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }

    }

// ... (your existing code)

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);

        Log.d("Adapter", "onBindViewHolder: Message: " + messages.getMessage() +
                " ImageUrl: " + messages.getImageUrl());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                messagesAdpterArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, messagesAdpterArrayList.size());
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return true;
            }
        });

        if (holder instanceof senderVierwHolder) {
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());

            if (messages.getImageUrl() != null && !messages.getImageUrl().isEmpty()) {
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.msgtxt.setVisibility(View.GONE);

                // Use Picasso to load the image into the ImageView
                Log.d("Adapter", "Sender Image URL: " + messages.getImageUrl());
                Picasso.get().load(messages.getImageUrl()).into(viewHolder.imageView);

                // Set sender's profile image
                Log.d("Adapter", "Sender Profile Image URL: " + senderImg);
                Picasso.get().load(senderImg).into(viewHolder.profilerggg);
            } else {
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.msgtxt.setVisibility(View.VISIBLE);

                // Set sender's profile image
                Picasso.get().load(senderImg).into(viewHolder.profilerggg);
            }
        } else if (holder instanceof reciverViewHolder) {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());


            if (messages.getImageUrl() != null && !messages.getImageUrl().isEmpty()) {
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.msgtxt.setVisibility(View.GONE);

                // Use Picasso to load the image into the correct ImageView
                Log.d("Adapter", "Receiver Image URL: " + messages.getImageUrl());
                Picasso.get().load(messages.getImageUrl()).into(viewHolder.imageView);

                // Set receiver's profile image
                Log.d("Adapter", "Receiver Profile Image URL: " + reciverIImg);
                Picasso.get().load(reciverIImg).into(viewHolder.pro);
            } else {
                viewHolder.imageView.setVisibility(View.GONE);
                viewHolder.msgtxt.setVisibility(View.VISIBLE);

                // Set receiver's profile image
                Picasso.get().load(reciverIImg).into(viewHolder.pro);
            }
        }
    }

// ... (your existing code)

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    public class senderVierwHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView imageView;
        CircleImageView profilerggg; // Add this line

        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
            imageView = itemView.findViewById(R.id.imageView3);
            profilerggg = itemView.findViewById(R.id.profilerggg);  // Initialize the CircleImageView
        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView imageView;
        CircleImageView pro;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
            imageView = itemView.findViewById(R.id.imageView);  // Corrected ID
            pro = itemView.findViewById(R.id.pro);
        }
    }
}
