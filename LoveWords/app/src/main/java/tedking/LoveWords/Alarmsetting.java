package tedking.LoveWords;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class Alarmsetting extends AppCompatActivity {
    private ImageButton save, back;
    private Button [] button = new Button[7];
    private TimePicker timePicker;
    private String adderror = "闹钟重复，请重新设置",toonearerror = "您设置的闹钟太相近，请重新设置";
    private int hour,minute;
    private String [] comfirmrepeate = {"日 ","一 ","二 ","三 ","四 ","五 ","六 "};
    boolean [] repeate = {false,false,false,false,false,false,false};
    public void findview(){
        save = (ImageButton) findViewById(R.id.save);
        back = (ImageButton) findViewById(R.id.back);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        button[0] = (Button) findViewById(R.id.sunday);
        button[1] = (Button) findViewById(R.id.monday);
        button[2] = (Button) findViewById(R.id.tuesday);
        button[3] = (Button) findViewById(R.id.wednesday);
        button[4] = (Button) findViewById(R.id.thursday);
        button[5] = (Button) findViewById(R.id.friday);
        button[6] = (Button) findViewById(R.id.saturday);
    }
    public void gettime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        }
        else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
    }
    public void savesomething(){
        File file = new File(getFilesDir()+"/databases/newdata.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getPath(),null,SQLiteDatabase.OPEN_READWRITE);
        gettime();
        Cursor cursor = database.rawQuery("select * from alarm where time = ?", new String[]{hour + ":" + minute});
        ContentValues cv = new ContentValues();
        if (cursor.moveToNext()){
            database.close();
            resetview();
            Toast.makeText(Alarmsetting.this,adderror,Toast.LENGTH_SHORT).show();
        }
        else {
            boolean toonear = false;
            int temphour, tempminute;
            String records;
            String[] strings;
            cursor = database.rawQuery("select * from alarm",null);
            while (cursor.moveToNext()){
                records = cursor.getString(0);
                strings = records.split(":");
                temphour = Integer.parseInt(strings[0]);
                tempminute = Integer.parseInt(strings[1]);
                if (hour == temphour){
                    if (Math.abs(tempminute - minute) < 5){
                        toonear = true;
                        break;
                    }
                }
            }
            if (toonear){
                database.close();
                resetview();
                Toast.makeText(Alarmsetting.this,toonearerror,Toast.LENGTH_SHORT).show();
            }
            else{
                String time = "", storedata = "";
                boolean hasrepeate = false;
                if (hour < 10) {
                    time = "0" + hour + ":";
                }
                else{
                    time = hour + ":";
                }
                if (minute < 10){
                    time = time + "0" + minute;
                }
                else {
                    time = time + minute;
                }
                cv.put("time",time);
                for (int i = 0; i < 7; i ++){
                    if (repeate[i]){
                        storedata += comfirmrepeate[i];
                        hasrepeate = true;
                    }
                }
                if (hasrepeate){
                    cv.put("repeate",storedata);
                    cv.put("specify","");
                }
                else {
                    cv.put("repeate","单次闹钟");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,hour);
                    calendar.set(Calendar.MINUTE,minute);
                    calendar.set(Calendar.SECOND,0);
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()){
                        cv.put("specify",(calendar.getTimeInMillis() + 24*60*60*1000));
                    }
                    else {
                        cv.put("specify",calendar.getTimeInMillis());
                    }
                }
                database.insert("alarm",null,cv);
                database.close();
                //Intent intent = new Intent(Alarmsetting.this,DealReceiver.class);
                Intent intent = new Intent(Alarmsetting.this,MyService.class);
                /*intent.setAction("来自设置界面");
                sendBroadcast(intent);*/
                intent.setAction("来自设置界面");
                startService(intent);
                Alarmsetting.this.finish();
            }
        }
    }
    public void repeatdate(int i){
        if (repeate[i]){
            repeate[i] = false;
            button[i].setTextColor(Color.rgb(255,255,255));
        }
        else {
            repeate[i] = true;
            button[i].setTextColor(Color.rgb(255,0,105));
        }
    }
    public void resetview(){
        for (int i = 0; i < 7; i ++){
            repeate[i] = false;
            button[i].setTextColor(Color.rgb(0,0,0));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmsetting);
        findview();
        timePicker.setIs24HourView(true);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.save:
                        savesomething();
                        break;
                    case R.id.back:
                        Alarmsetting.this.finish();
                        break;
                    case R.id.sunday:
                        repeatdate(0);
                        break;
                    case R.id.monday:
                        repeatdate(1);
                        break;
                    case R.id.tuesday:
                        repeatdate(2);
                        break;
                    case R.id.wednesday:
                        repeatdate(3);
                        break;
                    case R.id.thursday:
                        repeatdate(4);
                        break;
                    case R.id.friday:
                        repeatdate(5);
                        break;
                    case R.id.saturday:
                        repeatdate(6);
                        break;
                    }
                }
            };
        save.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        button[0].setOnClickListener(onClickListener);
        button[1].setOnClickListener(onClickListener);
        button[2].setOnClickListener(onClickListener);
        button[3].setOnClickListener(onClickListener);
        button[4].setOnClickListener(onClickListener);
        button[5].setOnClickListener(onClickListener);
        button[6].setOnClickListener(onClickListener);
        }
    }