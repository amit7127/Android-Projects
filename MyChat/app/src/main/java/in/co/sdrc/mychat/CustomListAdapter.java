package in.co.sdrc.mychat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amit Kumar Sahoo(amit@sdrc.co.in) on 01-08-2017.
 */

public class CustomListAdapter extends BaseAdapter {

    ArrayList<String> users;
    Context context;
    private static LayoutInflater inflater=null;

    public CustomListAdapter(ArrayList<String> users,Context context) {
        this.users = users;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View view1 =inflater.inflate(R.layout.list_element, null);
        holder.name = (TextView) view1.findViewById(R.id.user_email);
        holder.listLayout = (LinearLayout) view1.findViewById(R.id.list_layout);
        holder.name.setText((String)getItem(i));
        holder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user",(String)getItem(i));
                context.getApplicationContext().startActivity(intent);
            }
        });

        return view1;
    }
    public class Holder
    {
        TextView name;
        LinearLayout listLayout;
    }
}
