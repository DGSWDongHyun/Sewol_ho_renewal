package com.solo_dev.remember_renewal_.adapter.write;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.solo_dev.remember_renewal_.R;
import com.solo_dev.remember_renewal_.view.data.write.Data_Write;
import com.solo_dev.remember_renewal_.view.data.write.Reported_Data;

import java.util.List;


public class Data_Adapter extends BaseAdapter {
    private Context mContext;
    Dialog dialog;
    private List<Data_Write> listItem = null;
    LayoutInflater mLayoutInflater;
    ViewHolder holder;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public Data_Adapter(Context context, List<Data_Write> listItem) {
        mContext = context;
        this.listItem = listItem;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return listItem.size();
    }

    public Data_Write getItem(int i) {
        return listItem.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView title;
        public TextView contents;
        public TextView date;
        public ImageView img_contents;
        public CardView rela;
        public ImageButton report;
        public TextView display;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.write_item, null);

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.contents = (TextView) convertView.findViewById(R.id.contents);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.img_contents = (ImageView) convertView.findViewById(R.id.img_contents);
            holder.rela = (CardView) convertView.findViewById(R.id.img_view);
            holder.report = (ImageButton) convertView.findViewById(R.id.re);
            holder.display = (TextView)convertView.findViewById(R.id.display);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Data_Write mData = listItem.get(position);

        holder.report.setFocusable(false);
        holder.title.setText(mData.getTitle());
        holder.contents.setText(mData.getDate());
        holder.date.setText(mData.getContents());
        holder.display.setText(mData.getDisplay_Name());

        if (mData.getImg_contents() != null) {
            holder.rela.setVisibility(View.VISIBLE);
            Log.e("Debuged_sewol",mData.getImg_contents());
        } else{
            holder.rela.setVisibility(View.GONE);
        }

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View innerView = inflater.inflate(R.layout.report_dialog, null);

                final Button send_btn = innerView.findViewById(R.id.report_send);
                final EditText ed_contents;

                ed_contents = innerView.findViewById(R.id.text_report);

                final AlertDialog mDialog = new AlertDialog.Builder(mContext)
                        .setView(innerView)
                        .create();

                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                send_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ed_contents.getText().toString().isEmpty()) {
                            FirebaseDatabase.getInstance().getReference().child("write").child(mData.getGetkeys()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Data_Write write_data = dataSnapshot.getValue(Data_Write.class);
                                    int reported = write_data.getReported();
                                    reported++;
                                    DatabaseReference task = databaseReference.child("write").child(mData.getGetkeys()).child("reported");
                                    task.setValue(reported);

                                    DatabaseReference task_report = databaseReference.child("reported").child(write_data.getGetkeys());
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    task_report.setValue(new Reported_Data(user.getEmail(),write_data.getContents()));
                                    if(task_report.getKey().equals(write_data.getGetkeys())){
                                        DatabaseReference task_reported = task_report.child("reportedBy").push();
                                        task_reported.setValue(write_data.getUsers());
                                    }else{
                                        DatabaseReference task_reported = task_report.child("reportedBy").push();
                                        task_reported.setValue(write_data.getUsers());
                                    }

                                    mDialog.cancel();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

                mDialog.show();

            }
        });


        return convertView;
    }
}
