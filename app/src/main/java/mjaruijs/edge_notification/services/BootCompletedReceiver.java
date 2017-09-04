package mjaruijs.edge_notification.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast toast = Toast.makeText(context, "LOL", Toast.LENGTH_SHORT);
        toast.show();
        Intent mainServiceIntent = new Intent(context, MainService.class);
        context.startService(mainServiceIntent);
    }
}
