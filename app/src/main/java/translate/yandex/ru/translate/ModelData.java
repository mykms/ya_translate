package translate.yandex.ru.translate;


public class ModelData
{
    public boolean isFavorite;
    public String startText;
    public String endText;
    public String routeLang;

    public ModelData(boolean _isFavorite, String _startText, String _endText, String _routeLang)
    {
        isFavorite = _isFavorite;
        startText = _startText;
        endText = _endText;
        routeLang = _routeLang;
    }

}
