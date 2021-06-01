package com.dasu.turtlejava;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainLogic extends AppCompatActivity {
    static double sensor1;
    static double sensor2;
    static double sensor3;
    static double diff_12=20;
    static double diff_23=20;
    public int time;
    public static int neck_count;
    public static int waist_count;
    public int f_time;
    public int f_day;

    public  static MainActivity act = new MainActivity();

    //블루투스 감지 함수
    //if(블루투스 감지 함수)sensor1,2,3측정값확인
    //->허용값이면 time_set호출(최초 한번 시간 셋팅 [시간 변수 = hour][분 변수 = min]=>(중간에 측정값이 허용되지않으면 초기화))
    // 셋팅된 시간에서 1~3시간 경과시 measure()+알림함수 호출
    // 시간 경과가 되지 않았다면 measure()함수만
    // 이후 다시 sensor1,2,3측정값확인(1분마다)
    //->허용값이 아니면 1분후 재측정
    protected void time_set(double sensor1, double sensor2, double sensor3) {
        int year ,month,day,hour,min,sec;

        if(sensor3-sensor1<=20&&sensor3-sensor1>=0) {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
             month = cal.get(Calendar.MONTH) + 1;
             day = cal.get(Calendar.DAY_OF_MONTH);
             if(f_day==0)  f_day = cal.get(Calendar.DAY_OF_MONTH);
             else if(day==f_day+1) {
                 f_day = cal.get(Calendar.DAY_OF_MONTH);
                 neck_count = 0;
                  waist_count = 0;
             }
             hour = cal.get(Calendar.HOUR_OF_DAY);
             min = cal.get(Calendar.MINUTE);
             sec = cal.get(Calendar.SECOND);
             f_day=day;
             int cal_time =  hour*3600+min*60+sec;
             if(f_time==0){
                 f_time = cal_time; //처음 시간 측정
             }
             else{
                 if(cal_time-f_time>=10800) {
                     act.title="찍찍!! 혹시 못보셨어요??";
                     act.content="다리에 쥐가 났어요~!!" +
                             "앉아계신지 2시간이 되었답니다~!";
                 }
                 else if(cal_time-f_time>=7200) {
                     act.title="혹시 화장실 안가고 싶으신가요??";
                     act.content="앉아계신지 2시간이 되었답니다~~!!";
                 }
                 else if (cal_time-f_time>=3600) {
                     act.title="지금 안아계신지 1시간이 되었어요!";
                     act.content="가벼운 스트레칭은 어떠신가요?";
                 }
             }
            measure(sensor1,sensor2,sensor3);
            Log.w("time", "getData" + "현재 시각은 " + year + "년도 " + month + "월 " + day + "일 " + hour + "시 " + min + "분 " + sec + "초입니다.");
        }
        else{
            f_time = 0;
        }
    }
    //else 측정X

    protected void measure(double sensor1, double sensor2, double sensor3)  {
        double waist_radian = Math.atan2(sensor2-sensor1,diff_12);
        double waist_angle = waist_radian * 180 / Math.PI;
        double neck_radian = Math.atan2(sensor3-sensor2,diff_23);
        double neck_angle = neck_radian * 180 / Math.PI;
        Log.w("FireBaseData", "getData" + waist_angle);
        Log.w("FireBaseData", "getData" + neck_angle);
        boolean waist = waist_ck(waist_angle);
        boolean neck = neck_ck(neck_angle);
        if(waist&&neck){
            //자세알림 -> 각도전달
            act.title="위험!!";
            act.content="바른자세로 앉아주세요!!!!!";
        //    count++;
        }
        else if(waist){
            //허리알림 -> 각도전달
            act.title="허리 업 ~!! 허리를 세워볼까요??";
            act.content="허리각도는 현재"+Math.round(waist_angle*100)/100.0+"도로 허리가 많이 굽었습니다.";
            waist_count++;
        }
        else if(neck){
            //목알림 -> 각도전달
            act.title="거북이가 되어가고있어요~! 턱을 살짝 아래로 당겨볼까요?";
            act.content="목각도는 현재"+Math.round(neck_angle*100)/100.0+"도 입니다.";
            neck_count++;
        }
    }

    private boolean neck_ck(double neck_angle) {
        if(neck_angle>15) return true;
        return false;
    }

    private boolean waist_ck(double waist_angle) {
        if(waist_angle>15) return true;
        return false;
    }
}
