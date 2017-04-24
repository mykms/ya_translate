package translate.yandex.ru.translate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TranslateAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater lInflater;
    private ArrayList<ModelData> adapter = null;

    public TranslateAdapter(Context _context, ArrayList<ModelData> _adapter)
    {
        context = _context;
        adapter = _adapter;
        lInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return adapter.size();
    }

    @Override
    public Object getItem(int i) {
        return adapter.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View thisView = view;
        if (thisView == null)
        {
            thisView = lInflater.inflate(R.layout.listview_items, viewGroup, false);
        }
        ModelData data = (ModelData)getItem(i);

        // Заполняем данные
        //thisView.findViewById(R.id.icon).setBackgroundResource(R.drawable.btn_star_big_off);
        if (data.isFavorite)
            ((ImageView) thisView.findViewById(R.id.icon)).setImageResource(R.drawable.btn_star_big_on);
        else
            ((ImageView) thisView.findViewById(R.id.icon)).setBackgroundResource(R.drawable.btn_star_big_off);//.setImageResource(R.drawable.btn_star_big_off);

        ((TextView)thisView.findViewById(R.id.textView_top)).setText(data.startText);
        ((TextView)thisView.findViewById(R.id.textView_bottom)).setText(data.endText);
        ((TextView)thisView.findViewById(R.id.textView_lang)).setText(data.routeLang);

        return thisView;
    }
}
