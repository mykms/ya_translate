package translate.yandex.ru.translate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class FileJob extends Activity
{
    private Context _context;
    private final String fnameSetting = "yaSetting", fnameData = "yaFavorite.dat";

    public FileJob(Context _ContextMainApp)
    {
        this._context = _ContextMainApp;
    }

    public String GetFileNameSetting()
    {
        return this.fnameSetting;
    }
    public String GetFileNameData()
    {
        return this.fnameData;
    }

    // TODO: Сохранение настроек в файл
    public void WriteParams(String[] InParam)
    {
        try
        {
            SharedPreferences _pref = _context.getSharedPreferences(fnameSetting, MODE_PRIVATE);
            SharedPreferences.Editor _editor = _pref.edit();
            _editor.putString("MAIL", InParam[0]);

            _editor.commit();
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.getStackTrace();
        }
    }
    // TODO: Загрузка настроек из файла.
    public String[] ReadParams()
    {
        String[] outParams = new String[7];
        try
        {
            SharedPreferences _pref = _context.getSharedPreferences(fnameSetting, MODE_PRIVATE);
            outParams[0] = _pref.getString("MAIL", "");
        }
        catch (Exception ex)
        {
            outParams[0] = "";
        }
        return outParams;
    }

    public void WriteData(String datastring, boolean Append)
    {
        FileOutputStream fos = null;
        try
        {
            if (Append)
                fos = _context.openFileOutput(fnameData, _context.MODE_APPEND);
            else
                fos = _context.openFileOutput(fnameData, MODE_PRIVATE);
            fos.write(datastring.getBytes());
        }
        catch(IOException ex)
        {
            ex.getStackTrace();
        }
        finally
        {
            try
            {
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex)
            {
                ex.getStackTrace();
            }
        }
    }

    public String[] ReadData()
    {
        FileInputStream fin = null;
        ArrayList<String> dataArr = new ArrayList<String>();

        //StringBuilder buildStr = new StringBuilder();
        try {
            fin = _context.openFileInput(fnameData);
            InputStreamReader sr = new InputStreamReader(fin);
            BufferedReader br = new BufferedReader(sr);

            String temp_str = "";
            while ((temp_str = br.readLine()) != null )
            {
                dataArr.add(temp_str);
            }
        }
        catch(IOException ex)
        {
            ex.getStackTrace();
        }
        finally
        {
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex)
            {
                ex.getStackTrace();
            }
        }

        String[] arr = new String[dataArr.size()];
        arr = dataArr.toArray(arr);
        return arr;
    }
}
