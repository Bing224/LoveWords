package tedking.LoveWords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Alarming extends AppCompatActivity {
    private Button exit, toexercise;
    private MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    private Vibrator vibrator;
    private final  long EXCUTE_TIME = 180000;
    Handler handler = new Handler();
    long [] pattern = {1000,1000};
    private String song,songchoice = "song";
    private String song1 = "梦中的婚礼",song2 = "天空之城", song3 = "故乡的原风景";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarming);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
       // intent = new Intent(Alarming.this,DealReceiver.class);
        //intent.setAction("来自响铃界面");
        Intent intent = new Intent(Alarming.this,MyService.class);
        intent.setAction("来自响铃界面");
        startService(intent);
        exit = (Button) findViewById(R.id.exit);
        toexercise = (Button)findViewById(R.id.toexercise);
        preferences = getSharedPreferences("sharedpreference",MODE_PRIVATE);
        song = preferences.getString(songchoice,song1);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Alarming.this.finish();
            }
        },EXCUTE_TIME);
        if (song.equals(song1)) {
            mediaPlayer = MediaPlayer.create(Alarming.this, R.raw.meng);
        }
        else if (song.equals(song2)){
            mediaPlayer = MediaPlayer.create(Alarming.this,R.raw.tian);
        }
        else {
            mediaPlayer = MediaPlayer.create(Alarming.this,R.raw.gu);
        }
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        vibrator.vibrate(pattern,0);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                vibrator.cancel();
                Alarming.this.finish();
            }
        });
        toexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                vibrator.cancel();
                Intent intent = new Intent();
                intent.setClass(Alarming.this,Exercises.class);
                startActivity(intent);
                Alarming.this.finish();
            }
        });
    }
    @Override
    public void onDestroy(){
        //sendBroadcast(intent);
        mediaPlayer.release();
        vibrator.cancel();
        super.onDestroy();
    }
}
