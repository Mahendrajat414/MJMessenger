package com.example.mjmessenger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdpter extends RecyclerView.Adapter<UserAdpter.viewholder> implements Filterable {
    private Context context;
    private ArrayList<Users> usersArrayList;
    private ArrayList<Users> originalUsersList;


    public UserAdpter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
        this.originalUsersList = new ArrayList<>(usersArrayList); // Initialize originalUsersList
    }

    @NonNull
    @Override
    public UserAdpter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdpter.viewholder holder, int position) {

        Users users =usersArrayList.get(position);

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUserId())){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }else {
            holder.username.setText(users.getUserName());
            holder.userstatus.setText(users.getStatus());
            Picasso.get().load(users.getProfilepic()).into(holder.userimg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, chatWin.class);
                    intent.putExtra("nameeee", users.getUserName());
                    intent.putExtra("reciverImg", users.getProfilepic());
                    intent.putExtra("uid", users.getUserId());
                    context.startActivity(intent);


                }
            });
        }

    }
    public void removeItem(int position) {
        usersArrayList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase().trim();

                ArrayList<Users> filteredList;
                if (charString.isEmpty()) {
                    // If the search bar is empty, return the original list
                    filteredList = new ArrayList<>(usersArrayList);
                } else {
                    // Filter the list based on the search query
                    ArrayList<Users> filteredUsers = new ArrayList<>();
                    for (Users user : usersArrayList) {
                        if (user.getUserName().toLowerCase().contains(charString)) {
                            filteredUsers.add(user);
                        }
                    }
                    filteredList = filteredUsers;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values != null) {
                    ArrayList<Users> filteredList = new ArrayList<>((Collection<? extends Users>) filterResults.values);

                    // Update the list with the filtered results
                    usersArrayList.clear();
                    usersArrayList.addAll(filteredList);

                    // If the search bar is empty, restore the original list
                    if (charSequence.length() == 0) {
                        usersArrayList.addAll(originalUsersList);
                    }

                    notifyDataSetChanged();
                }
            }
        };

    }



    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
