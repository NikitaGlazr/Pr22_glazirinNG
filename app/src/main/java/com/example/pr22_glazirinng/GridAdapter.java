package com.example.pr22_glazirinng;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private Integer mCols, mRows;
    private ArrayList<String> arrPict; // массив картинок
    private String PictureCollection; // Префикс набора картинок
    private Resources mRes; // Ресурсы приложени
    private static enum Status {CELL_OPEN, CELL_CLOSE, CELL_DELETE};
    private ArrayList<GridAdapter.Status> arrStatus; // состояние ячеек
    private static final int CELL_CLOSE = 0;
    private static final int CELL_OPEN = 1;
    private static final int CELL_DELETE = -1;


    public GridAdapter(Context context, int cols, int rows)
    {
        mContext = context;
        mCols = cols;
        mRows = rows;
        arrPict = new ArrayList<String>();
        arrStatus = new ArrayList<GridAdapter.Status>();

        // Пока определяем префикс так, позже он будет браться из настроек
        PictureCollection = "animal";
        // Получаем все ресурсы приложения
        mRes = mContext.getResources();

        // Метод заполняющий массив vecPict
        makePictArray ();
        // Метод устанавливающий всем ячейкам статус CELL_CLOSE
        closeAllCells();
    }

    private void closeAllCells () {
        arrStatus.clear();
        for (int i = 0; i < mCols * mRows; i++)
            arrStatus.add(GridAdapter.Status.CELL_CLOSE);
    }

    private void makePictArray () {
        // очищаем вектор
        arrPict.clear();
        // добавляем
        for (int i = 0; i < ((mCols * mRows) / 2); i++)
        {
            arrPict.add (PictureCollection + Integer.toString (i));
            arrPict.add (PictureCollection + Integer.toString (i));
        }
        // перемешиваем
        Collections.shuffle(arrPict);
    }

    @Override
    public int getCount() {
        return mCols*mRows;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView view; // выводиться у нас будет картинка

        if (convertView == null)
            view = new ImageView(mContext);
        else
            view = (ImageView)convertView;

        switch (arrStatus.get(position))
        {
            case CELL_OPEN:
                // Получаем идентификатор ресурса для картинки,
                // которая находится в векторе vecPict на позиции position
                Integer drawableId = mRes.getIdentifier(arrPict.get(position), "drawable", mContext.getPackageName());
                view.setImageResource(drawableId);
                break;
            case CELL_CLOSE:
                view.setImageResource(R.drawable.close);
                break;
            default:
                view.setImageResource(R.drawable.none);
        }

        return view;
    }

    public void checkOpenCells() {
        int first = arrStatus.indexOf(GridAdapter.Status.CELL_OPEN);
        int second = arrStatus.lastIndexOf(GridAdapter.Status.CELL_OPEN);
        if (first == second)
            return;
        if (arrPict.get(first).equals (arrPict.get(second)))
        {
            arrStatus.set(first, GridAdapter.Status.CELL_DELETE);
            arrStatus.set(second, GridAdapter.Status.CELL_DELETE);
        }
        else
        {
            arrStatus.set(first, GridAdapter.Status.CELL_CLOSE);
            arrStatus.set(second, GridAdapter.Status.CELL_CLOSE);
        }
        return;
    }

    public void openCell(int position) {
        if (arrStatus.get(position) != GridAdapter.Status.CELL_DELETE)
            arrStatus.set(position, GridAdapter.Status.CELL_OPEN);

        notifyDataSetChanged();
        return;
    }

    public boolean checkGameOver() {
        if (arrStatus.indexOf(GridAdapter.Status.CELL_CLOSE) < 0)
            return true;
        return false;
    }
}
