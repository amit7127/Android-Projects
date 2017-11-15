package in.co.sdrc.myalarmlocationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

/**
 * Created by Amit Kumar Sahoo(amit@sdrc.co.in) on 12-09-2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Hi helllllooooooo", Toast.LENGTH_SHORT).show();
        Location location = new Location(context)
    }
}
