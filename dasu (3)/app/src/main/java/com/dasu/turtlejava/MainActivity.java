package com.dasu.turtlejava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;



import java.security.MessageDigest;
import java.util.zip.Inflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.util.OptionalBoolean;


public class MainActivity extends AppCompatActivity {
    public  MainLogic logic = new MainLogic();
    private DatabaseReference db;
    public Intent intent;
    public static String title, content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppKeyHash();
        intent = getIntent();
        Log.w("FireBaseData", "getData" + intent.getStringExtra("name"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);


        TextView neck = (TextView) findViewById(R.id.textView6);

         TextView waist = (TextView)findViewById(R.id.textView7);
        db = FirebaseDatabase.getInstance().getReference();
        readSensor();
        createNotification(title,content);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_myinfo, null);
//        TextView tvNickname = (TextView) view.findViewById(R.id.tvNickname);

    //    user.setText(intent.getStringExtra("name").toString() + "님 안녕하세요!");
        neck.setText("허리디스크 경고 알림수 : "  + logic.waist_count);
        waist.setText("거북목 경고 알림수 : " + logic.neck_count);


    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_button:{
                Intent intent2 = new Intent(getApplicationContext(),MyinfoActivity.class);
                intent2.putExtras(intent);
                startActivity(intent2);
            }
        }
        return super.onOptionsItemSelected(item);
    }
public void createNotification(String title,String content) {
    NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

    builder.setSmallIcon(R.mipmap.ic_launcher);
    builder.setContentTitle(title);
    builder.setContentText(content);

    builder.setColor(Color.RED);
    // 사용자가 탭을 클릭하면 자동 제거
    builder.setAutoCancel(true);

    // 알림 표시

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
    }

    // id값은
    // 정의해야하는 각 알림의 고유한 int값
    notificationManager.notify(1, builder.build());
}

    private void removeNotification() {



        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }


    private void readSensor() {
        db.child("rasberry").child("first").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Sensor.class) != null){
                    Sensor post = dataSnapshot.getValue(Sensor.class);
                    logic.time_set(post.sensor1,post.sensor2,post.sensor3);
                    Log.w("FireBaseData", "getData" + post.toString());

                } else {
                    Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


}