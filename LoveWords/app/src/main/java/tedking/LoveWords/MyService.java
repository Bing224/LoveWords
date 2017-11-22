package tedking.LoveWords;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.io.File;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyService extends Service {
    private AlarmManager manager;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");'
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long alarmtime = System.currentTimeMillis() + 8 * 24 * 60 * 60 * 1000;
        long copyalarmtime = alarmtime;
        boolean[] oneday = {false, false, false, false, false, false, false};
        String[] stringday = {"日", "一", "二", "三", "四", "五", "六"};
        boolean hasalarm = false;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        List<String> outdate = new LinkedList<String>();
        File file = new File(getFilesDir() + "/databases/newdata.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        //以下为删除过期闹钟
        Cursor cursor = database.rawQuery("select * from alarm where repeate = ?", new String[]{"单次闹钟"});
        while (cursor.moveToNext()) {
            if (Long.parseLong(cursor.getString(2)) < System.currentTimeMillis()) {
                outdate.add(cursor.getString(0));
            }
        }
        cursor.close();
        Iterator<String> iterator = outdate.iterator();
        while (iterator.hasNext()) {
            database.delete("alarm", "time = ?", new String[]{iterator.next()});
        }
        //以下为设置闹钟时间
        cursor = database.rawQuery("select * from alarm", null);
        while (cursor.moveToNext()) {
            for (int i = 0; i < 7; i++) {
                oneday[i] = false;
            }
            if (!cursor.getString(2).equals("")) {
                long temptime = Long.parseLong(cursor.getString(2));
                if (alarmtime > temptime) {
                    alarmtime = temptime;
                }
            } else {
                String timedetail = cursor.getString(0), repeateday = cursor.getString(1), temphour, tempminute;
                String[] timedetailarray = timedetail.split(":");
                temphour = timedetailarray[0];
                tempminute = timedetailarray[1];
                for (int i = 0; i < 7; i++) {
                    if (repeateday.contains(stringday[i])) {
                        oneday[i] = true;
                    }
                }
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temphour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(tempminute));
                calendar.set(Calendar.SECOND, 0);
                if (oneday[day - 1]) {
                    if (calendar.getTimeInMillis() > System.currentTimeMillis() && calendar.getTimeInMillis() < alarmtime) {
                        alarmtime = calendar.getTimeInMillis();
                    } else {
                        for (int i = 1; i < 7; i++) {
                            if (oneday[(day - 1 + i) % 7] && (calendar.getTimeInMillis() + 24*60*60*1000*i < alarmtime)) {
                                alarmtime = calendar.getTimeInMillis() + 24 * 60 * 60 * 1000 * i;
                                break;
                            }
                        }
                    }
                }
            }
            hasalarm = true;
        }
        if (hasalarm) {
            Intent intent1 = new Intent(this, Alarming.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
            manager.setWindow(AlarmManager.RTC_WAKEUP, alarmtime, 1000, pendingIntent);
        }
        database.close();
        return super.onStartCommand(intent, flags, START_STICKY);
    }
    @Override
    public void onDestroy(){
        Intent localintent = new Intent();
        localintent.setClass(MyService.this,MyService.class);
        localintent.setAction("自行重启");
        startService(localintent);
        super.onDestroy();
    }
}
