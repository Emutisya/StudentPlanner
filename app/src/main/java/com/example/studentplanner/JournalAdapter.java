package com.example.studentplanner;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.journal;

import java.util.ArrayList;

public class JournalAdapter{
    private Context context;
    private ArrayList<journal> list;
    public JournalAdapter(@NonNull View itemView, Context context, ArrayList<journal> list) {

        this.context = context;
        this.list = list;
    }

    public class JournalHolder extends RecyclerView.ViewHolder {



        private TextView txtname, txtdate, txtabout, txtcomment;
        private ImageView imgJournal;
        private ImageButton btnpost, btncomment;

        public JournalHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtjournalname);
            txtdate = itemView.findViewById(R.id.journalpostdate);
            txtabout = itemView.findViewById(R.id.txtjournalAbout);
            txtcomment = itemView.findViewById(R.id.txtJournalComments);
            imgJournal = itemView.findViewById(R.id.journalPhoto);
            btnpost = itemView.findViewById(R.id.btnJournalOption);
            btncomment = itemView.findViewById(R.id.btnjournalComment);
        }

    }
    }
