package translate.yandex.ru.translate;

import android.os.AsyncTask;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.net.*;
import java.io.*;
import android.content.Context;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class TranslateTask extends AsyncTask<Object, Void, Void>
{
    private String key = "trnsl.1.1.20170329T112400Z.cd564cde6e19156f.57646457d143b50f0f93aa23ef1b609723dfbaa1";
    private String inText = "", outText;
    private String route = "";
    private TextView TextFieled = null;
    private Context thisContext = null;

    @Override
    protected void onPreExecute() {
        //
    }

    @Override
    protected Void doInBackground(Object... params){
        try {
            inText = (String)params[0];
            route = (String)params[1];
            TextFieled = (TextView)params[2];
            thisContext = (Context)params[3];
            // Ограничение на POST 10 000 символов
            if (inText != null)
                if(inText.length() > 10000) inText = inText.substring(0, 10000);
            inText = TextIterator(inText);
            String str = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="+key+"&text="+inText+"&lang="+route+"&format=plain&options=0";

            JsonUtils jutils = new JsonUtils();
            String res = jutils.GetJsonResponse(str, JsonUtils.Method.POST);
            outText = jutils.GetText(res);
        }
        catch (Exception e) {e.printStackTrace();}

        return null;
    }

    @Override
    protected void onPostExecute(Void response) {
        super.onPostExecute(response);
        TextFieled.setText(outText);
    }

    public String TextIterator(String input)
    {
        if (input == null || input.isEmpty())
        {
            return "";
        }
        try {
            return URLEncoder.encode(input.toString(), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
