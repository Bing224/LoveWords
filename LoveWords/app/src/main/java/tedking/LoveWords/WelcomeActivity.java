package tedking.LoveWords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        preferences = getSharedPreferences("sharedpreference",MODE_PRIVATE);
        guide = preferences.getBoolean("guide",false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (guide){
                    Intent intent = new Intent(WelcomeActivity.this,GuideActivity.class);
                    editor = preferences.edit();
                    editor.putBoolean("guide",false).commit();
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
                else{
                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        },2000);
    }
}
