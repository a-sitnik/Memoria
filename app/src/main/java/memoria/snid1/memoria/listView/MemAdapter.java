package memoria.snid1.memoria.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.ArrayList;

import memoria.snid1.memoria.R;
import memoria.snid1.memoria.database.DAOMem;
import memoria.snid1.memoria.utils.DateTimeFormatter;
import memoria.snid1.memoria.utils.SettingsManager;

public class MemAdapter extends ArrayAdapter {

    //Dateformat used

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<DAOMem> prod;

    public MemAdapter(Context context,int resource, ArrayList<DAOMem> prod) {
        super(context, resource, prod);
        //super(context, textViewResourceId, prod);
        this.prod = prod;
        ctx = context;
        /*lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
    }


    @Override
    public int getCount() {
        return prod.size();
    }

    @Override
    public DAOMem getItem(int position) {
        return prod.get(position);
    }

    // formatting date
    public String getFormattedDate(DAOMem mem){
        return DateTimeFormatter.date.format(mem.getDate());
    }
    // formatting time
    public String getFormattedTime(DAOMem mem){
        return DateTimeFormatter.time.format(mem.getDate());
    }
/*
    // id by position
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DAOMem mem = getMem(position);
        View newView = convertView;
        final ViewHolder holder;

        if (newView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate(R.layout.mem_in_list_item, parent, false);
            holder = new ViewHolder();

            holder.noteView = (TextView) newView.findViewById(R.id.noteView);
            holder.dateView = (TextView)newView.findViewById(R.id.dateView);
            holder.timeView = (TextView)newView.findViewById(R.id.timeView);

            holder.backButton1 = (Button) newView.findViewById(R.id.after_swipe_button_1);
            holder.backButton2 = (Button) newView.findViewById(R.id.after_swipe_button_2);
            holder.backButton3 = (Button) newView.findViewById(R.id.after_swipe_button_3);

            newView.setTag(holder);
        } else {
            holder = (ViewHolder) newView.getTag();

        }
        ((SwipeListView)parent).recycle(newView, position); //TODO: guess what it does

        // filling viev
        holder.noteView.setText(mem.getNote());
        holder.dateView.setText(getFormattedDate(mem));
        holder.timeView.setText(getFormattedTime(mem));

        SettingsManager.getCustomizableButton(holder.backButton1, SettingsManager.INSTANCE.backButton1);
        SettingsManager.getCustomizableButton(holder.backButton2, SettingsManager.INSTANCE.backButton2);
        SettingsManager.getCustomizableButton(holder.backButton3, SettingsManager.INSTANCE.backButton3);

        //((Button) newView.findViewById(R.id.after_swipe_button_1)).
        return newView;
    }
    static class ViewHolder {
        TextView noteView;
        TextView dateView;
        TextView timeView;
        Button backButton1;
        Button backButton2;
        Button backButton3;
    }


    // obj by position
   public DAOMem getMem(int position) {
        return ((DAOMem) getItem(position));
    }

}
