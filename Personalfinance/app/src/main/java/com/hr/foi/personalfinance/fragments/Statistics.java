package com.hr.foi.personalfinance.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by dominik on 21.12.16..
 */

public class Statistics extends BaseFragment implements FragmentInterface
{


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.statistics_layout, container, false);

        // Line Graph za prihode ukupno po danu u mjesecu
        // x os dodaj da prikazuje dan u mjesecu, y je iznos i to je ok
        GraphView lineGraphPrihodi = (GraphView) view.findViewById(R.id.lineGraphPrihodi);
        LineGraphSeries<DataPoint> lineGraphSeries1p = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        lineGraphSeries1p.setColor(Color.GREEN);
        lineGraphPrihodi.setTitle("Prihodi po danu");
        lineGraphPrihodi.setTitleColor(Color.BLACK);
        lineGraphPrihodi.addSeries(lineGraphSeries1p);

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
        lineGraphRashodi.addSeries(lineGraphSeries1r);

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
        lineGraph2.addSeries(lineGraphSeries2);


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
        barChart.addSeries(barGraphSeries);

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
}
