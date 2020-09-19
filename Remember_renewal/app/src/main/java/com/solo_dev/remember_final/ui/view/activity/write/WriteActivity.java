package com.solo_dev.remember_final.ui.view.activity.write;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.solo_dev.remember_final.R;
import com.solo_dev.remember_final.data.write.Data_Write;
import com.solo_dev.remember_final.ui.view.fragment.WriteFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity{

    private Uri filePath;
    FragmentManager fm;
    FragmentTransaction tran;
    WriteFragment writeFragment;
    View innerView;
    FloatingActionButton fab_write;
    Button select_image;
    String storage2;
    boolean viewing = true;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_write);

        fab_write = findViewById(R.id.fab_write);


        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_Dialog();
            }
        });

        writeFragment = new WriteFragment();
        setFrag(0);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            uploadFile((TextView)innerView.findViewById(R.id.img_select));
        }
    }
    private void uploadFile(final TextView text) {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://rememberenweal.appspot.com/").child("images/" + filename);
            storage2 = String.valueOf("images/" + filename);
            storageReference = storageRef;
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            text.setText("이미지 선택됨.");
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
    public void create_Dialog(){
        innerView = getLayoutInflater().inflate(R.layout.writting_dialog, null);
        final Button send_btn = innerView.findViewById(R.id.send);
        final EditText ed_title, ed_contents;

        select_image = innerView.findViewById(R.id.select);
        ed_title = innerView.findViewById(R.id.title_write);
        ed_contents = innerView.findViewById(R.id.write_content);


        final AlertDialog mDialog = new AlertDialog.Builder(WriteActivity.this)
                .setView(innerView)
                .create();

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        select_image.setOnClickListener(v-> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
        });

        send_btn.setOnClickListener(v-> {
            if(!ed_title.getText().toString().isEmpty() && !ed_contents.getText().toString().isEmpty()) {
                DatabaseReference task = databaseReference.child("write").push();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Data_Write data = new Data_Write(ed_title.getText().toString(), ed_contents.getText().toString(), getTime(), storage2, 0, 0, task.getKey(), viewing, user.getEmail(),user.getDisplayName());
                task.setValue(data);
                storage2 = null;

                LayoutInflater inflater = getLayoutInflater();
                View toastDesign = inflater.inflate(R.layout.toast, (ViewGroup)findViewById(R.id.toast_design_root)); //toast_design.xml 파일의 toast_design_root 속성을 로드

                ImageView imgs = toastDesign.findViewById(R.id.img_toast);
                Glide.with(getApplicationContext()).load(R.drawable.yellow).into(imgs);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0); // CENTER를 기준으로 0, 0 위치에 메시지 출력
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(toastDesign);
                toast.show();

                mDialog.cancel();
            }
        });

        mDialog.show();
    }
    public String getTime(){
        long now2 = System.currentTimeMillis();
        Date date = new Date(now2);
        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("aa HH:mm");

        return CurTimeFormat.format(date);
    }
    public void setFrag(int n) {    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.fragment_container, writeFragment);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 1:

                break;
        }
    }
}
