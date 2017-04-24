package translate.yandex.ru.translate;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class fragment_history extends Fragment {

    private String[] dataArray = null;
    private ArrayList<ModelData> dataArrayList = new ArrayList<ModelData>();
    TranslateAdapter adapter = null;

    public fragment_history() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FileJob filejob = new FileJob(getActivity().getApplicationContext());
        dataArray = filejob.ReadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Ищем корневой View
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length; i++) {
                ModelData curModel = null;
                try {
                    Object[] obj = dataArray[i].split(";", 4);
                    boolean isFavorite = (Integer.parseInt(obj[0].toString()) == 1);

                    String start_text = obj[2].toString();
                    String end_text = obj[3].toString();
                    String route_text = obj[1].toString();

                    //boolean isFavorite = false;
                    //isFavorite = val==0 ? false : true;

                    curModel = new ModelData(isFavorite, start_text, end_text, route_text);
                    dataArrayList.add(curModel);

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            adapter = new TranslateAdapter(getActivity().getApplicationContext(), dataArrayList);
        }

        View rootView = getView();// берем корневой элемент всего фрагмента
        if (rootView != null)
        {
            // Получаем наш список
            ListView listHistory = (ListView)rootView.findViewById(R.id.listView_history);
            listHistory.setAdapter(adapter);
        }
    }
}
