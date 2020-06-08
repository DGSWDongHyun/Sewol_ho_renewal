package com.solo_dev.remember_renewal.Write_Activity.Fragment;

import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.solo_dev.remember_renewal.R;
import com.solo_dev.remember_renewal.Write_Activity.Adapter.Data_Adapter;
import com.solo_dev.remember_renewal.Write_Activity.DataItem.Data_Write;
import com.solo_dev.remember_renewal.Write_Activity.DataItem.Reported_Data;
import com.solo_dev.remember_renewal.Write_Activity.WriteActivity;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class WriteFragment extends Fragment {

    SwipeRefreshLayout mRefresh;
    List<Data_Write> write = new CopyOnWriteArrayList<>();
    Data_Adapter adapter_write;
    ListView list_written;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public WriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_write, container, false);


        mRefresh = inflate.findViewById(R.id.swipe);
        list_written = inflate.findViewById(R.id.write_list);
        adapter_write = new Data_Adapter(getActivity(), write);
        list_written.setAdapter(adapter_write);



        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reading_data();
                mRefresh.setRefreshing(false);
            }
        });

        initialize();


        list_written.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Data_Write item = (Data_Write) parent.getItemAtPosition(position) ;

                View innerView = getLayoutInflater().inflate(R.layout.details_dialog, null);

                final AlertDialog mDialog = new AlertDialog.Builder(getActivity())
                        .setView(innerView)
                        .create();

                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView title = innerView.findViewById(R.id.title);
                TextView contents = innerView.findViewById(R.id.contents);
                TextView date = innerView.findViewById(R.id.date);
                ImageView img_one = innerView.findViewById(R.id.img_contents);
                RelativeLayout rela = innerView.findViewById(R.id.rela_img);

                title.setText(item.getTitle());
                contents.setText(item.getDate());
                date.setText(item.getContents());

                if(item.getImg_contents() != null){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    Glide.with(getActivity()).load(storageReference.child(item.getImg_contents())).into(img_one);
                }else{
                    rela.setVisibility(View.GONE);
                }


                mDialog.show();

            }
        }) ;

        return inflate;
    }
    public String getTime(){
        long now2 = System.currentTimeMillis();
        Date date = new Date(now2);
        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("aa HH:mm");

        return CurTimeFormat.format(date);
    }
    public void initialize(){
        databaseReference.child("write").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data_Write write_data = dataSnapshot.getValue(Data_Write.class);

                if (write_data.isViewing()) {
                    write.add(new Data_Write(write_data.getTitle(), write_data.getDate(), write_data.getContents(), write_data.getImg_contents(), write_data.getLiked(), write_data.getReported(), write_data.getGetkeys(), write_data.isViewing(), write_data.getUsers(),write_data.getDisplay_name()));
                    adapter_write.notifyDataSetChanged();
                    repoerted_out(write_data);
                }
                adapter_write.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void reading_data() {
        for(Data_Write idx : write){
            write.remove(idx);
        }
        databaseReference.child("write").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data_Write write_data = dataSnapshot.getValue(Data_Write.class);
                if (write_data.isViewing()) {
                    write.add(new Data_Write(write_data.getTitle(), write_data.getDate(), write_data.getContents(), write_data.getImg_contents(), write_data.getLiked(), write_data.getReported(), write_data.getGetkeys(), write_data.isViewing(), write_data.getUsers(),write_data.getDisplay_name()));
                    adapter_write.notifyDataSetChanged();
                    repoerted_out(write_data);
                }
                adapter_write.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void repoerted_out(final Data_Write data) {
        if (data.getReported() > 10) {
            databaseReference.child("write").child(data.getGetkeys()).child("viewing").setValue(false);
        }

    }
}