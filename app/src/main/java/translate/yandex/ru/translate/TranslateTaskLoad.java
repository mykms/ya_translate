package translate.yandex.ru.translate;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


public class TranslateTaskLoad extends AsyncTask<Object, Void, String[][]>
{
    private String key = "trnsl.1.1.20170329T112400Z.cd564cde6e19156f.57646457d143b50f0f93aa23ef1b609723dfbaa1";
    private String langkey = "ru";
    Spinner lspin = null, rspin = null;
    Context thisContext;
    Object[] data;

    @Override
    protected void onPreExecute()
    {
        //
    }
    @Override
    protected String[][] doInBackground(Object... params)
    {
        try {
            lspin = (Spinner) params[0];
            rspin = (Spinner) params[1];
            thisContext = (Context) params[2];
        }catch (Exception e) {e.printStackTrace();}
        finally {
            String urlstring = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=" + key + "&ui=" + langkey;
            JsonUtils jutils = new JsonUtils();
            String res = jutils.GetJsonResponse(urlstring, JsonUtils.Method.GET);
            data = jutils.LoadLanguages(res);
            return jutils.GetLanguageTable();
        }
    }

    @Override
    protected void onPostExecute(String[][] response)
    {
        super.onPostExecute(response);
        try {
            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(thisContext, android.R.layout.simple_spinner_item, data);
            lspin.setAdapter(adapter);
            rspin.setAdapter(adapter);
            int from_id = 0, to_id = 0;
            for (int i = 0; i < data.length; i++)
            {
                if (data[i].toString().equalsIgnoreCase("Русский")) from_id = i;
                if (data[i].toString().equalsIgnoreCase("Английский")) to_id = i;
            }
            lspin.setSelection(from_id);
            rspin.setSelection(to_id);

        }catch (Exception e) {e.printStackTrace();}
    }
}