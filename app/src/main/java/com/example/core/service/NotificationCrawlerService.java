package com.example.core.service;

import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationCrawlerService extends android.service.notification.NotificationListenerService {
    public final static String TAG = "=====";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate():: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy():: ");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.e(TAG, "onListenerConnected():: ");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        // 새로운 알림이 생성되었을 때
        super.onNotificationPosted(sbn);
        Log.e(TAG, "onNotificationPosted()");

        Notification notification = sbn.getNotification();
        Bundle extras = sbn.getNotification().extras;

        String contentTitle = extras.getString(Notification.EXTRA_TITLE);
        CharSequence contentText = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        CharSequence bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        CharSequence bigContentTitle = extras.getCharSequence(Notification.EXTRA_TITLE_BIG);

        Icon smallIcon = notification.getSmallIcon();
        Icon largeIcon = notification.getLargeIcon();

        Log.e(TAG, "NotiPosted::  " +
                " / id : " + sbn.getId() +
                " / bigContentTitle : " + bigContentTitle +
                " / bigText : " + bigText +
                " / contentText : " + contentText +
                " / subText : " + subText +
                " / contentTitle : " + contentTitle +
                " / icon : " + smallIcon.getResId() +
                " / packageName : " + sbn.getPackageName()
        );




//        // 백엔드 API 테스트용 코드
//        String url = "http://noti-drawer.run.goorm.io/api/analyze-sentence";
//        String sentence = "{\"sentence\": \"오늘은 치킨 섭취를 먹구 싶어용.\"}";
//
//        GetNounInSentenceAsync getNoun = new GetNounInSentenceAsync(url, sentence);
//        getNoun.execute();





    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

        // 알림이 지워졌을 때
        super.onNotificationRemoved(sbn);
        Log.e(TAG, "onNotificationRemoved():: ");

        Log.e(TAG, "NotiIRemoved:: " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId());
    }




//    // 백엔드 API 테스트용 코드
//    private class GetNounInSentenceAsync extends AsyncTask<Void, Void, String> {
//
//        String url;
//        String sentence;
//
//        // Constructor
//        public GetNounInSentenceAsync(String url, String sentence){
//            this.sentence = sentence;
//            this.url = url;
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            // 비동기 처리 후 결과값을 리턴
//            // 이 메소드가 끝난 후에 onPostExecute()가 실행됨
//            return new ApiCommUtil().request(url, sentence);
//        }
//
//        @Override
//        protected void onPostExecute(String result){
//            super.onPostExecute(result);
//            // 서버 통신 후 처리 완료
//            // 받아온 값은 result에 저장되며 JSON 타입으로 저장됨
//            // 올바르지 못한 값을 받아왔을 때, 빈 String 값이 나옴
//
//            if(result.equals(""))
//                Log.e("=====", "실패하였습니다");
//            else
//                Log.e("=====", "완료하였습니다");
//        }
//    }

}