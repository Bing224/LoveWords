package tedking.LoveWords;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DealReceiver extends BroadcastReceiver {
    private AlarmManager manager;
    public DealReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Intent intent1 = new Intent(context,MyService.class);
        context.startService(intent1);
        //Toast.makeText(context,"jieshoudao",Toast.LENGTH_LONG).show();
    }
}
