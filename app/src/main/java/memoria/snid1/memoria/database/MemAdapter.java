package memoria.snid1.memoria.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

import memoria.snid1.memoria.R;
import memoria.snid1.memoria.database.DAOMem;

public class MemAdapter extends ArrayAdapter<DAOMem> {

    //Dateformat used
    static SimpleDateFormat dout = new SimpleDateFormat("HH:mm\ndd MMM yy");
    Context ctx;
    LayoutInflater lInflater;

    public MemAdapter(Context context, int textViewResourceId, List<DAOMem> prod) {
        super(context, textViewResourceId, prod);
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // formatting date
    public String getFormattedDate(DAOMem mem){
        return dout.format(mem.getDate());
    }

    // id by position
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        DAOMem mem = getMem(position);

        // filling viev
        ((TextView) view.findViewById(R.id.noteViev)).setText(mem.getNote());
        ((TextView) view.findViewById(R.id.dateViev)).setText(getFormattedDate(mem));

        return view;
    }

    // obj by position
   DAOMem getMem(int position) {
        return ((DAOMem) getItem(position));
    }

}
