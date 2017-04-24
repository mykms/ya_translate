package translate.yandex.ru.translate;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUtils
{
    enum Method {GET, POST};

    private String APIkey = "trnsl.1.1.20170329T112400Z.cd564cde6e19156f.57646457d143b50f0f93aa23ef1b609723dfbaa1";
    private String CurrentLang = "ru";
    private String[][] LanguageTable = new String[2][];

    public JsonUtils()
    {

    }

    public String GetJsonResponse(String UrlRequest, Method _method)
    {
        if (UrlRequest.isEmpty() || UrlRequest == null)
            return null;


        try
        {
            URL url = new URL(UrlRequest);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // Если нужно отправить данные методом POSt
            if (_method == Method.POST) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                try{
                    String data = UrlRequest.substring(UrlRequest.indexOf("?")+1);
                    urlConnection.getOutputStream().write(data.getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            try
            {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                return  stringBuilder.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Object[] LoadLanguages(String jsonString)
    {
        if (jsonString == null)
            return null;
        if (jsonString.isEmpty())
            return null;

        try {
            JSONObject JsonData = new JSONObject(jsonString);
            JSONObject object = (JSONObject)JsonData.get("langs");
            JSONArray Arraynames = object.names();          // ключ

            LanguageTable[0] = new String[Arraynames.length()];
            LanguageTable[1] = new String[Arraynames.length()];

            List<String> JsonDataArray = new ArrayList<String>();
            if (Arraynames.length() > 0)
            {
                for (int i = 0; i < Arraynames.length(); i++)
                {
                    String kod = Arraynames.get(i).toString();
                    String LangName = object.get(kod).toString();
                    LanguageTable[0][i] = kod;
                    LanguageTable[1][i] = LangName;
                    JsonDataArray.add(LangName);
                }
                Collections.sort(JsonDataArray);
                return JsonDataArray.toArray();
            }
            return null;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String[][] GetLanguageTable()
    {
        return LanguageTable;
    }

    public String GetText(String jsonString)
    {
        if (jsonString.isEmpty() || jsonString == null)
            return null;

        try {
            JSONObject JsonData = new JSONObject(jsonString);
            JSONArray object = (JSONArray)JsonData.get("text");
            if (object.isNull(0)) return "";
            return object.get(0).toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
