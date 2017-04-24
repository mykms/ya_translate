package translate.yandex.ru.translate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private Spinner spin_left;
    private Spinner spin_right;
    private TextView outText;
    private String[][] LangTable;
    private EditText InText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        String dataStr = "1;en-ru;my text;мой текст\n" + "1;fr-en;bonjour;привет\n" + "0;en-ru;people;человек\n";
        FileJob fileJob = new FileJob(this.getApplicationContext());
        fileJob.WriteData(dataStr, true);
*/
        // проверим - подключен ли интернет?
        if (!isNetworkActive())
        {
            ShowError("Нет соединения с интернетом. Перевод невозможен");
            return;
        }
        else
        {
            spin_left = (Spinner)findViewById(R.id.spinner_left);
            spin_right = (Spinner)findViewById(R.id.spinner_right);
            // Получаем список поддерживаемых языков
            final TranslateTaskLoad translateTask = new TranslateTaskLoad();
            translateTask.execute(spin_left, spin_right, getApplicationContext());

            InText = (EditText)findViewById(R.id.editText);
            InText.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if(event.getAction() == KeyEvent.ACTION_UP)// && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        outText = (TextView)findViewById(R.id.OutputTextView);
                        // сохраняем текст, введенный до нажатия Enter в переменную
                        String strCatName = InText.getText().toString();

                        if(isNetworkActive())
                        {
                            int isLoad_spin = spin_left.getCount();
                            if (isLoad_spin < 1)
                            {
                                ShowError("Список языков не загружен. Перевод невозможен!");
                                return false;
                            }
                            Object from = null;
                            Object to = null;
                            try {
                                from = spin_left.getSelectedItem();
                                to = spin_right.getSelectedItem();
                            }
                            catch (Exception e) {e.printStackTrace();}

                            if(from != null && to != null)
                            {
                                if (from.toString().equalsIgnoreCase(to.toString())) {
                                    ShowError("Язык начального текста и язык перевода совпадают!");
                                    return false;
                                }
                                String[][] x = null;
                                try {
                                    // Получаем направление перевода
                                    x = translateTask.get();
                                    LangTable = x;
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                String route = GetKodLanguage(from.toString(), x) + "-" + GetKodLanguage(to.toString(), x);
                                TranslateTask translate = new TranslateTask();
                                translate.execute(strCatName, route, outText, getApplicationContext());

                                return true;
                            }
                            else {
                                ShowError("Выберите язык с Какого на Какой нужно перевести!");
                                return false;
                            }
                        }else {
                            ShowError("Нет соединения с интернетом. Перевод невозможен");
                            return false;
                        }
                    }
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {
                        if (FieldsIsNotEmpty())
                        {
                            if (LangTable == null)
                            {
                                ShowError("Не удалось сохранить перевод");
                                return false;
                            }
                            String route = null;
                            try {
                                route = GetKodLanguage(spin_left.getSelectedItem().toString(), LangTable) + "-" + GetKodLanguage(spin_right.getSelectedItem().toString(), LangTable);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                return false;
                            }
                            try {
                                String inString = InText.getText().toString().replace(';', ',').replace('\n', '.');
                                String outString = outText.getText().toString().replace(';', ',').replace('\n', '.');
                                String dataStr = "0;" + route + ";" + inString + ";" + outString;
                                FileJob fileJob = new FileJob(getApplicationContext());
                                fileJob.WriteData(dataStr, true);
                                return true;
                                //ShowError(dataStr);
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                                ShowError("НЕ удалось сохранить текст с переводом");
                                return false;
                            }
                        }
                        MainActivity.super.onBackPressed();
                    } //end
                    return false;
                }
            });
        }
    }

    public void OnClick_ActivityFavorite(View view)
    {
        Intent FavIntent = new Intent(MainActivity.this, ActivityFavorite.class);
        startActivity(FavIntent);
    }

    private boolean FieldsIsNotEmpty()
    {
        if (spin_left == null || spin_right == null || outText == null || InText == null)
            return false;
        if (spin_left.getCount() < 1 || spin_right.getCount() < 1 || outText.getText().toString().isEmpty() || InText.getText().toString().isEmpty())
            return false;
        if (spin_left.getSelectedItem() == null && spin_right.getSelectedItem() == null)
            return false;
        return true;
    }

    public void OnClick_ButtonReversLang(View view)
    {
        if (spin_left != null && spin_right != null)
        {
            if (spin_left.getCount() > 0 && spin_right.getCount() > 0)
            {
                int from = 0, to = 0;
                from = spin_left.getSelectedItemPosition();
                to = spin_right.getSelectedItemPosition();
                // Меняем местами
                spin_left.setSelection(to);
                spin_right.setSelection(from);
            }
        }
    }

    public void OnClick_ButtonShare(View view)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String text = "";
        if (outText == null)
        {
            ShowError("Еще пока нечем делиться. Переведите текст");
            return;
        }
        if (outText.getText().toString().isEmpty())
        {
            ShowError("Еще пока нечем делиться. Переведите текст");
            return;
        }
        text = outText.getText().toString();

        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        try {
            startActivity(Intent.createChooser(sendIntent, "Нужно поделиться"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void OnClick_buttonCopytext(View view)
    {
        if (outText == null)
        {
            ShowError("Нет текста для копирования");
            return;
        }
        if (outText.length() < 1) {
            ShowError("Нет текста для копирования");
            return;
        }

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(outText.getText());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("TAG", outText.getText());
            clipboard.setPrimaryClip(clip);
        }
        ShowError("Перевод скопирован в буфер обмена");
    }

    public void OnClick_AddToFavorite(View view)
    {
        String textFavError = "Не удалось добавить в избранное";

        if (FieldsIsNotEmpty())
        {
            if (LangTable == null)
            {
                ShowError(textFavError);
                return;
            }
            String route = null;
            try {
                route = GetKodLanguage(spin_left.getSelectedItem().toString(), LangTable) + "-" + GetKodLanguage(spin_right.getSelectedItem().toString(), LangTable);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ShowError(textFavError);
                return;
            }
            String inString = InText.getText().toString().replace(';', ',').replace('\n', '.');
            String outString = outText.getText().toString().replace(';', ',').replace('\n', '.');
            String dataStr = "1;" + route + ";" + inString + ";" + outString;

            FileJob fileJob = new FileJob(this.getApplicationContext());
            fileJob.WriteData(dataStr, true);

            // Процедура добавления
            ShowError("Добавлено в Избранное");
        }
        else
            ShowError(textFavError);
    }

    public void OnClick_ActivitySetting(View view)
    {
        // Загружем настройки
        Intent intent = new Intent(this, ActivitySetting.class);
        startActivity(intent);
    }

    public void ShowError(String errorText)
    {
        Toast toast = Toast.makeText(getApplicationContext(), errorText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private boolean isNetworkActive()
    {
        ConnectivityManager connetInet = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = connetInet.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && connetInet.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    private String GetKodLanguage(String LanguageName, String[][] LanguageTable)
    {
        if (LanguageName == null || LanguageName.isEmpty())
            return null;
        if (LanguageTable == null || LanguageTable.length < 1)
            return null;
        for (int i = 0; i < LanguageTable[0].length; i++)
        {
            if (LanguageName.equalsIgnoreCase(LanguageTable[1][i]))
            {
                return LanguageTable[0][i];
            }
        }
        return null;
    }
}
