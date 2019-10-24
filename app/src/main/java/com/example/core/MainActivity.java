package com.example.core;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.model.dto.NotificationDTO;
import com.example.core.presenter.DBManager;
import com.example.core.service.NotificationCrawlerService;
import com.example.core.utils.ApiCommUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private Button btnApiSend;
    private TextView txtApiStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // API-Communication
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnApiSend = findViewById(R.id.btnApiSend);
        txtApiStatus = findViewById(R.id.txtApiStatus);



        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//
        // Notification Service

        // <특별한 접근 -> 알림 접근 혀용> 확인

        if (!isPermissionGranted()) {
            // 접근 혀용이 되어있지 않다면 1. 메시지 발생 / 2, 설정으로 이동시킴
            Toast.makeText(getApplicationContext(), getString(R.string.app_name)+" 앱의 알림 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        // 서비스 시작
        // NotificationListenerService의 경우, Rebind() 메소드 실행 전까지 서비스가 종료되지 않는 것으로 확인됨.
        Intent service = new Intent(getApplicationContext(), NotificationCrawlerService.class);
        startService(service);

        //___________________________________________//



        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//
        // API-Communication
        btnApiSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://noti-drawer.run.goorm.io/api/analyze-sentence";
                String sentence = "{\"sentence\": \"오늘은 치킨 섭취를 먹구 싶어용.\"}";

                AsyncManager getNoun = new AsyncManager(url, sentence);
                getNoun.execute();

                btnApiSend.setEnabled(false);
                txtApiStatus.setText("데이터 수신중입니다.");
            }
        });
        //___________________________________________//



        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//
        // Database Manager
        //        데이터베이스 매니저 생성
//        데이터베이스 파일 실제 위치 : /data/data/앱패키지주소/databases/data.db
        DBManager database = new DBManager(openOrCreateDatabase("data.db", MODE_PRIVATE, null));  // 데이터베이스 생성, 열기


//        새로운 노티를 데이터베이스에 반영하는 예시
//        try {
//            database.updateNewNoti("오늘 날씨가 좋군요!!");  // 노티 메시지를 테이블(Notification, Noun)에 업데이트
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        NOUN 데이터베이스에 추가
//        database.insertNoun("오늘");
//        database.insertNoun("날씨");
//        database.insertNoun("하늘");

//
//        NOTIFICATION 데이터베이스에 대한 weight값 업데이트
//        database.updateNotiWeight(1,true);
//
//        NOUN 데이터베이스에 대한 weight값 업데이트
//        database.updateNounWeight(1, true);
//        database.updateNounWeight("하늘", false);
//
//        NOTIFICATION 데이터베이스 정보를 가져옴
//        ArrayList<NotificationDTO> getData = database.getNotificationList();
//        for(int i = 0; i < getData.size(); i++){
//            Log.e("=====", getData.get(i).id + "/ " + getData.get(i).title + "/ " + getData.get(i).msg + "/ " + getData.get(i).weight + "/ " + getData.get(i).isRead + "/ " + getData.get(i).pkgName);
//        }


        //___________________________________________//

    }



    // Notification Service
    private boolean isPermissionGranted() {
        // 노티수신을 확인하는 권한을 가진 앱 모든 리스트
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        // 이 앱의 알림 접근 허용이 되어있는가?
        return sets != null && sets.contains(getPackageName());
    }



    // API-Communication
    public class AsyncManager extends AsyncTask<Void, Void, String> {

        String url;
        String sentence;

        // Constructor
        public AsyncManager(String url, String sentence){
            this.sentence = sentence;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // 비동기 처리 후 결과값을 리턴
            // 이 메소드가 끝난 후에 onPostExecute()가 실행됨
            return new ApiCommUtil().requestJson(url, sentence);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            // 서버 통신 후 처리 완료
            // 받아온 값은 result에 저장되며 JSON 타입으로 저장됨
            // 올바르지 못한 값을 받아왔을 때, 빈 String 값이 나옴

            btnApiSend.setEnabled(true);
            if(result.equals(""))
                txtApiStatus.setText("실패하였습니다.");
            else
                txtApiStatus.setText("완료하였습니다.\n" + result);
        }
    }
}
