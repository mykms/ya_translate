package translate.yandex.ru.translate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityFavorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Фрагмент по умолчанию
        Fragment fragment = new fragment_history();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame1, fragment).commit();
    }

    public void OnClick_ButtonMainActivity(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void OnClick_ActivitySetting(View view)
    {
        // Загружем настройки
        Intent intent = new Intent(this, ActivitySetting.class);
        startActivity(intent);
    }

    public void onClickButton_favorite(View view)
    {
        Fragment fragment = new fragment_favorite();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame1, fragment).commit();
    }

    public void onClickButton_history(View view)
    {
        Fragment fragment = new fragment_history();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame1, fragment).commit();
    }

    public void OnClickButton_delete(View view)
    {
        // Удаление на крестик
    }
}
