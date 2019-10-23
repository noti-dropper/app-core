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
import com.example.core.service.NotificationCrawlerService;
import com.example.core.utils.ApiCommunication;

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

                GetNounInSentenceAsync getNoun = new GetNounInSentenceAsync(url, sentence);
                getNoun.execute();

                btnApiSend.setEnabled(false);
                txtApiStatus.setText("데이터 수신중입니다.");
            }
        });
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
    public class GetNounInSentenceAsync extends AsyncTask<Void, Void, String> {

        String url;
        String sentence;

        // Constructor
        public GetNounInSentenceAsync(String url, String sentence){
            this.sentence = sentence;
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // 비동기 처리 후 결과값을 리턴
            // 이 메소드가 끝난 후에 onPostExecute()가 실행됨
            return new ApiCommunication().request(url, sentence);
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
