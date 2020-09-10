package com.example.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Models.journal;
import com.example.studentplanner.Constant;
import com.example.studentplanner.EditJournalActivity;
import com.example.studentplanner.HomeActivity;
import com.example.studentplanner.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalHolder> {

    private Context context;
    private ArrayList<journal> list;
    private ArrayList<journal> listAll;
    private SharedPreferences preferences;

    public JournalAdapter(Context context, ArrayList<journal> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        preferences= context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public JournalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_journal,parent,false);
        return new JournalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalHolder holder, int position) {
        journal journal = list.get(position);
        Picasso.get().load(Constant.URL+"storage/profiles"+journal.getUser().getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"storage/journal"+journal.getPhoto()).into(holder.imgJournal);
        holder.txtName.setText(journal.getUser().getUserName());
        holder.txtComments.setText("View all"+journal.getComments());
        holder.txtLikes.setText(journal.getLikes()+"Likes");
        holder.txtDate.setText(journal.getDate());
        holder.txtDesc.setText(journal.getAbout());
        holder.txtTag.setText(journal.getTag());
        holder.txtFeelings.setText(journal.getFeelings());

        holder.btnLike.setImageResource(
                journal.isSelfLike()?R.drawable.ic_baseline_favorite_24:R.drawable.ic_baseline_favorite_outline
        );

        holder.btnLike.setOnClickListener(v->{
            holder.btnLike.setImageResource(
            journal.isSelfLike()?R.drawable.ic_baseline_favorite_outline:R.drawable.ic_baseline_favorite_24
             );

            StringRequest request =new StringRequest(Request.Method.POST,Constant.LIKE_JOURNAL,response -> {

                journal mJournal =list.get(position);
                try {
                    JSONObject object =new JSONObject(response);
                    if (object.getBoolean("success")){
                        mJournal.setSelfLike(!journal.isSelfLike());
                        mJournal.setLikes(mJournal.isSelfLike()?journal.getLikes()+1:journal.getLikes()-1);
                        list.set(position, mJournal);
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    }
                    else{

                        holder.btnLike.setImageResource(
                                journal.isSelfLike()?R.drawable.ic_baseline_favorite_24:R.drawable.ic_baseline_favorite_outline
                        );


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            },err->{

                err.printStackTrace();

            }){
                //ADD TOKEN

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String token= preferences.getString("token","");
                    HashMap<String,String>map = new HashMap<>();
                    map.put("Authorization","Bearer"+token);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String>map = new HashMap<>();
                    map.put("id",journal.getId()+"");
                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);

        });

        if(journal.getUser().getId()==preferences.getInt("id",0)){

            holder.btnJournalOption.setVisibility(View.VISIBLE);
        }else{
            holder.btnJournalOption.setVisibility(View.GONE);
        }
        holder.btnJournalOption.setOnClickListener(v->{
            PopupMenu popupMenu =new PopupMenu(context,holder.btnJournalOption);
            popupMenu.inflate(R.menu.menu_journal_options);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.item_edit:{
                            Intent i =new Intent(((HomeActivity)context), EditJournalActivity.class);
                            i.putExtra("journalId",journal.getId());
                            i.putExtra("position",position);
                            i.putExtra("text",journal.getAbout());
                           // i.putExtra("Ftext",journal.getFeelings());
                          //  i.putExtra("text",journal.getTag());
                          //  i.putExtra("text",journal.getDate());
                            context.startActivity(i);
                            return true;


                        }
                        case R.id.item_delete:{

                            deleteJournal (journal.getId(),position);
                            return true;

                        }
                    }


                    return false;
                }
            });

            popupMenu.show();
        });
    }
    private void deleteJournal(int journalId,int position){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Delete Journal Entry?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest request =new StringRequest(Request.Method.POST,Constant.DELETE_JOURNAL,response -> {

                    try {
                        JSONObject object =new JSONObject(response);
                        if(object.getBoolean("success")){
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            listAll.clear();
                            listAll.addAll(list);
                            
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },error -> {

                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String token= preferences.getString("token","");
                        HashMap<String,String>map = new HashMap<>();
                        map.put("Authorization","Bearer"+token);
                        return map;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String>map = new HashMap<>();
                        map.put("id",journalId+"");
                        return map;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class JournalHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtDate,txtDesc,txtTag,txtFeelings,txtLikes,txtComments;
        private CircleImageView imgProfile;
        private ImageView imgJournal;
        private ImageButton btnJournalOption,btnLike,btnComment;

        public JournalHolder(@NonNull View itemView) {
            super(itemView);

            txtName=itemView.findViewById(R.id.txtJournalname);
            txtDate=itemView.findViewById(R.id.txtJournalDate);
            txtDesc=itemView.findViewById(R.id.txtjournalAbout);
            txtTag=itemView.findViewById(R.id.txtjournalTag);
            txtFeelings=itemView.findViewById(R.id.txtjournalFeeling);
            txtLikes=itemView.findViewById(R.id.txtJournalLikes);
            txtComments=itemView.findViewById(R.id.txtJournalComments);
            imgProfile=itemView.findViewById(R.id.imgjournalProfile);
            imgJournal=itemView.findViewById(R.id.imgJournalPhoto);
            btnJournalOption=itemView.findViewById(R.id.btnJournalOption);
            btnLike=itemView.findViewById(R.id.btnJournalLike);
            btnComment=itemView.findViewById(R.id.btnJournalComment);
            btnJournalOption.setVisibility(View.GONE);



        }
    }
}
