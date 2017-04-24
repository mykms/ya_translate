package translate.yandex.ru.translate;


import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class fragment_favorite extends Fragment {

    private String[] dataArray = null;
    private ArrayList<ModelData> dataArrayList = new ArrayList<ModelData>();
    TranslateAdapter adapter = null;

    public fragment_favorite() {
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
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        return rootView;
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

                    if (isFavorite) {
                        curModel = new ModelData(isFavorite, start_text, end_text, route_text);
                        dataArrayList.add(curModel);
                    }

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
            ListView listHistory = (ListView)rootView.findViewById(R.id.listView_favorite);
            listHistory.setAdapter(adapter);
        }
    }
}
