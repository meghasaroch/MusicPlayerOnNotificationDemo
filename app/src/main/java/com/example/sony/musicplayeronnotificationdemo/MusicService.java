package com.example.sony.musicplayeronnotificationdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MusicService extends Service {

    static MediaPlayer mediaPlayer;
    public MusicService()
    {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("MYMSG","in on start command");
        String action = intent.getAction();
        Log.d("MYMSG",intent.getAction());
        if(action.equals("play_music"))
        {
            new Thread(new start_music()).start();
        }
        else if(action.equals("stop_music"))
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    class start_music implements Runnable
    {

        @Override
        public void run() {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MusicService.this)
                            .setSmallIcon(R.drawable.ic_apps_black_24dp)
                            .setContentTitle("Playing Music")
                            .setContentText("0%");
            Intent resultIntent = new Intent(MusicService.this, MainActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            MusicService.this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);


            Intent in_stop = new Intent(MusicService.this,MusicService.class);
            in_stop.setAction("stop_music");
            PendingIntent pin_stop = PendingIntent.getActivity(MusicService.this,0,in_stop,0);
            mBuilder.addAction(R.drawable.ic_stop,"Stop",pin_stop);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+ File.separator+"sample.mp3");

                mediaPlayer.prepare();
                mediaPlayer.start();

                int total = mediaPlayer.getDuration();

                mBuilder.setProgress(total,0,false);

                while(true)
                {
                    int current = mediaPlayer.getCurrentPosition();

                    int per =(int) ((current*100.0)/total);
                    Log.d("MYMSG",current+" "+total+" "+per);
                    if( (total-current)<1000 )
                    {
                       break;
                    }
                    mBuilder.setProgress(total,current,false);
                    mBuilder.setContentText(per+"% complete");
                    mNotificationManager.notify(10, mBuilder.build());
                    Thread.sleep(1000);
                }

                mBuilder.setProgress(total,total,false);
                mBuilder.setContentText("100% complete");
                mNotificationManager.notify(10, mBuilder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
