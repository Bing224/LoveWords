package tedking.LoveWords;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetOne extends AppWidgetProvider {
    private SharedPreferences preferences;
    private String database1 = "四六级词库",worddatabase = "worddatabase",emptycode = "tableempty";
    private static final String UPDATE_CONDUCTION = "YOUNEEDTOUPDATE";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_one);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Intent intent1 = new Intent(context,MyService.class);
        intent1.setAction("来自widget");
        context.startService(intent1);
        Intent intent = new Intent();
        intent.setAction(UPDATE_CONDUCTION);
        context.sendBroadcast(intent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_one);
        remoteViews.setOnClickPendingIntent(R.id.newone,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_one);
        String action = intent.getAction();
        if (action.equals(UPDATE_CONDUCTION)){
            String[] strings = getsomething(context);
            views.setTextViewText(R.id.appwidget_text,strings[0]);
            views.setTextViewText(R.id.buzhidao,strings[1]);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,WidgetOne.class);
            appWidgetManager.updateAppWidget(componentName,views);
        }
    }
    public String[] getsomething(Context context){
        preferences = context.getSharedPreferences("sharedpreference",MODE_PRIVATE);
        String s = preferences.getString(worddatabase,database1),tablename;
        String [] result = new String[2];
        if (s.equals(database1)){
            tablename = "cetre";
        }
        else {
            tablename =  "toeflre";
        }
        File file = new File(context.getFilesDir()+"/databases/newdata.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getPath(),null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("select * from "+ tablename, null);
        if (cursor.getCount() == 0){
            result[0] = emptycode;
            database.close();
            return  result;
        }
        else {
            cursor = database.rawQuery("select * from " + tablename + " order by RANDOM() limit 1", null);
            while (cursor.moveToNext()) {
                result[0] = cursor.getString(0);
                result[1] = cursor.getString(2);
            }
            database.close();
            return result;
        }
    }
}

