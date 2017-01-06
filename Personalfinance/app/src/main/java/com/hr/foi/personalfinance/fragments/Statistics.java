package com.hr.foi.personalfinance.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hr.foi.personalfinance.R;
import com.hr.foi.personalfinance.adapter.MyListAdapter;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;

import entities.DataBuilder;
import entities.DataInterface;
import pojo.Record_;

/**
 * Created by dominik on 21.12.16..
 */

public class Statistics extends BaseFragment implements FragmentInterface, DataInterface
{
    private DataBuilder dataBuilder = new DataBuilder(this);
    private SharedPreferences preferences;
    private ArrayList<Record_> records;


    public static final Statistics newInstance(String name){
        Statistics s = new Statistics();
        s.setName(name);

        return s;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    /**
     * Used for setting values before view is created.
     *
     * **
     * ** Checking user login status.
     * **
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String userId = userID();
        dataBuilder.getRecords(userId);



        View view = inflater.inflate(R.layout.statistics_layout, container, false);

        // Line Graph za prihode ukupno po danu u mjesecu
        // x os dodaj da prikazuje dan u mjesecu, y je iznos i to je ok
        GraphView lineGraphPrihodi = (GraphView) view.findViewById(R.id.lineGraphPrihodi);
        LineGraphSeries<DataPoint> lineGraphSeries1p = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 8),
                new DataPoint(4, 1),
                new DataPoint(5, 5),
                new DataPoint(6, 9),
                new DataPoint(7, 2),
                new DataPoint(8, 1),
                new DataPoint(9, 4),
                new DataPoint(10, 3),
                new DataPoint(11, 2),
                new DataPoint(12, 7),
                new DataPoint(13, 3),
                new DataPoint(14, 2),
                new DataPoint(15, 1),
                new DataPoint(16, 5),
                new DataPoint(17, 3),
                new DataPoint(18, 8),
                new DataPoint(19, 1),
                new DataPoint(20, 5),
                new DataPoint(21, 9),
                new DataPoint(22, 2),
                new DataPoint(23, 1),
                new DataPoint(24, 4),
                new DataPoint(25, 3),
                new DataPoint(26, 2),
                new DataPoint(27, 7),
                new DataPoint(28, 3),
                new DataPoint(29, 2),
                new DataPoint(30, 2),
                new DataPoint(31, 5),
                new DataPoint(32, 3),
                new DataPoint(33, 8),
                new DataPoint(34, 1),
                new DataPoint(35, 5),
                new DataPoint(36, 9),
                new DataPoint(37, 7),
                new DataPoint(38, 1),
                new DataPoint(39, 4),
                new DataPoint(40, 8),
                new DataPoint(41, 2),
                new DataPoint(42, 7),
                new DataPoint(43, 3),
                new DataPoint(44, 2),
                new DataPoint(45, 16)
        });

        lineGraphSeries1p.setColor(Color.GREEN);
        lineGraphPrihodi.setTitle("Prihodi po danu");
        lineGraphPrihodi.setTitleColor(Color.BLACK);
        lineGraphPrihodi.setTitleTextSize(30);
        lineGraphPrihodi.addSeries(lineGraphSeries1p);

        // activate horizontal zooming and scrolling
        lineGraphPrihodi.getViewport().setScalable(true);
        // activate horizontal scrolling
        lineGraphPrihodi.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        lineGraphPrihodi.getViewport().setScalableY(true);
        // activate vertical scrolling
        lineGraphPrihodi.getViewport().setScrollableY(true);


        // Line Graph za rashode ukupno po danu u mjesecu
        // x os dodaj da prikazuje dan u mjesecu, y je iznos i to je ok
        GraphView lineGraphRashodi = (GraphView) view.findViewById(R.id.lineGraphRashodi);
        LineGraphSeries<DataPoint> lineGraphSeries1r = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        lineGraphSeries1r.setColor(Color.RED);
        lineGraphRashodi.setTitle("Rashodi po danu");
        lineGraphRashodi.setTitleColor(Color.BLACK);
        lineGraphRashodi.setTitleTextSize(30);
        lineGraphRashodi.addSeries(lineGraphSeries1r);

        // activate horizontal zooming and scrolling
        lineGraphRashodi.getViewport().setScalable(true);
        // activate horizontal scrolling
        lineGraphRashodi.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        lineGraphRashodi.getViewport().setScalableY(true);
        // activate vertical scrolling
        lineGraphRashodi.getViewport().setScrollableY(true);

        // Line Graph za ukupni balance po danu u mjesecu
        // x os dodaj da prikazuje dan u mjesecu, y je iznos i to je ok
        GraphView lineGraph2 = (GraphView) view.findViewById(R.id.lineGraph2);
        LineGraphSeries<DataPoint> lineGraphSeries2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        lineGraph2.setTitle("Ukupno stanje po danu");
        lineGraph2.setTitleColor(Color.BLACK);
        lineGraph2.setTitleTextSize(30);
        lineGraph2.addSeries(lineGraphSeries2);

        // activate horizontal zooming and scrolling
        lineGraph2.getViewport().setScalable(true);
        // activate horizontal scrolling
        lineGraph2.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        lineGraph2.getViewport().setScalableY(true);
        // activate vertical scrolling
        lineGraph2.getViewport().setScrollableY(true);


        // Bar Chart za kategorije po mjesecu
        //x os dodaj da je naziv kategorije, y je iznos i ok je
        //mozda dodati legendu
        GraphView barChart = (GraphView) view.findViewById(R.id.barChart);
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        barGraphSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        barGraphSeries.setSpacing(50);

        // draw values on top
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        barChart.setTitle("Ukupno po kategorijama");
        barChart.setTitleColor(Color.BLACK);
        barChart.setTitleTextSize(30);
        barChart.addSeries(barGraphSeries);

        // activate horizontal zooming and scrolling
        barChart.getViewport().setScalable(true);
        // activate horizontal scrolling
        barChart.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        barChart.getViewport().setScalableY(true);
        // activate vertical scrolling
        barChart.getViewport().setScrollableY(true);

        return view;
    }

    /**
     * Used for setting values after view is created.
     * **
     * ** Setting listeners for ui elements.
     * **
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void buildData(Object data)
    {
        pojo.Record record = (pojo.Record) data;
        if (record !=null)
        {
            records = new ArrayList<Record_>();

            for (Record_ item : record.getRecord())
            {
                records.add(item);
            }
        }
    }

    private String userID()
    {
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }
}
