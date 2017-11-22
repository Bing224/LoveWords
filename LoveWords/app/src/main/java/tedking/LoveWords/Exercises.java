package tedking.LoveWords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class Exercises extends AppCompatActivity {
    private Button choice1, choice2, choice3, choice4;
    private TextView wordquestion;
    private SharedPreferences preferences;
    private Vibrator vibrator;
    long [] pattern = {100,400};
    private String database1 = "四六级词库", database2 = "托福词库",worddatabase = "worddatabase",questionnumber = "questionnumber";
    private int num, position, times = 1;
    public void findview(){
        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);
        wordquestion = (TextView) findViewById(R.id.wordquestion);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }
    public String getDatabase(){
        preferences = getSharedPreferences("sharedpreference",MODE_PRIVATE);
        String s = preferences.getString(worddatabase,database1);
        if (s.equals(database1)){
            return "cetre";
        }
        else {
            return "toeflre";
        }
    }
    public boolean whether_empty(){
        File file = new File(getFilesDir()+"/databases/newdata.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getPath(),null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("select * from "+ getDatabase(), null);
        if (cursor.getCount() == 0) {
            database.close();
            return true;
        }
        else {
            database.close();
            return false;
        }
    }
    public int setText() {
        if (whether_empty()) {
            return 5;
        } else {
            File file = new File(getFilesDir() + "/databases/newdata.db");
            SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = database.rawQuery("select * from " + getDatabase() + " order by RANDOM() limit 1", null);
            String word = "",table = "", explains = "";
            String[] randomexplains = new String[3];
            while (cursor.moveToNext()) {
                word = cursor.getString(0);
                explains = cursor.getString(2);
            }
            wordquestion.setText(word);
            if (getDatabase().equals("cetre")) {
                table = "cetcomplete";
            } else {
                table = "toeflcomplete";
            }
            cursor = database.rawQuery("select * from " + table + " order by RANDOM() limit 3", null);
            int i = 0;
            while (cursor.moveToNext()) {
                randomexplains[i] = cursor.getString(2);
                i++;
            }
            database.delete(getDatabase(),"english=?",new String[]{word});
            database.close();
            int position = (int) (3 * Math.random() + 1);
            if (position == 1) {
                choice1.setText(explains);
                choice2.setText(randomexplains[0]);
                choice3.setText(randomexplains[1]);
                choice4.setText(randomexplains[2]);
            } else if (position == 2) {
                choice1.setText(randomexplains[0]);
                choice2.setText(explains);
                choice3.setText(randomexplains[1]);
                choice4.setText(randomexplains[2]);
            } else if (position == 3) {
                choice1.setText(randomexplains[0]);
                choice2.setText(randomexplains[1]);
                choice3.setText(explains);
                choice4.setText(randomexplains[2]);
            } else {
                choice1.setText(randomexplains[0]);
                choice2.setText(randomexplains[1]);
                choice3.setText(randomexplains[2]);
                choice4.setText(explains);
            }
            return position;
        }
    }
    public void operation(int i){
        if (position == 5){
            Intent intent = new Intent();
            intent.setClass(Exercises.this,FinishExercise.class);
            intent.putExtra("whetherempty","Yes");
            startActivity(intent);
            Exercises.this.finish();
        }
        else if (times == num && position == i){
            Intent intent = new Intent();
            intent.setClass(Exercises.this,FinishExercise.class);
            intent.putExtra("whetherempty","No");
            startActivity(intent);
            Exercises.this.finish();
        }
        else if (position == i){
            position = setText();
            times ++;
        }
        else{
            vibrator.vibrate(pattern,-1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);
        preferences = getSharedPreferences("sharedpreference",MODE_PRIVATE);
        num = preferences.getInt(questionnumber,10);
        findview();
        position = setText();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.choice1:
                        operation(1);
                        break;
                    case R.id.choice2:
                       operation(2);
                        break;
                    case R.id.choice3:
                        operation(3);
                        break;
                    case R.id.choice4:
                        operation(4);
                        break;
                }
            }
        };
        choice1.setOnClickListener(onClickListener);
        choice2.setOnClickListener(onClickListener);
        choice3.setOnClickListener(onClickListener);
        choice4.setOnClickListener(onClickListener);
    }
}
