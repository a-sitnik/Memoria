package memoria.snid1.memoria.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import memoria.snid1.memoria.R;
import memoria.snid1.memoria.database.DAOMem;
import memoria.snid1.memoria.utils.DateTimeFormatter;

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
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView= inflater.inflate(R.layout.mem_in_list_item, parent, false);

        /*super.getView(position, convertView, parent);
        View view = convertView;*/
        /*if (view == null) {
            view = lInflater.inflate(R.layout.mem_in_list_item, parent, false);
        }*/
        DAOMem mem = getMem(position);

        // filling viev
        ((TextView) newView.findViewById(R.id.noteViev)).setText(mem.getNote());
        ((TextView) newView.findViewById(R.id.dateViev)).setText(getFormattedDate(mem));
        ((TextView) newView.findViewById(R.id.timeViev)).setText(getFormattedTime(mem));

        //((Button) newView.findViewById(R.id.after_swipe_button_1)).
        return newView;
    }

    // obj by position
   public DAOMem getMem(int position) {
        return ((DAOMem) getItem(position));
    }

}
