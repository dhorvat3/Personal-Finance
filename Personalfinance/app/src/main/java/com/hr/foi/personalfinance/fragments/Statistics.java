package com.hr.foi.personalfinance.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hr.foi.personalfinance.R;
import com.hr.foi.userinterface.BaseFragment;
import com.hr.foi.userinterface.FragmentInterface;

import java.util.ArrayList;
import java.util.List;

import core.DataBuilder;
import core.DataInterface;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import pojo.Category_;
import pojo.Record_;

/**
 * Created by dominik on 21.12.16..
 */

public class Statistics extends BaseFragment implements FragmentInterface, DataInterface
{
    private DataBuilder dataBuilder = new DataBuilder(this);
    private SharedPreferences preferences;
    private ArrayList<Record_> records = new ArrayList<Record_>();
    private ArrayList<Category_> categories = new ArrayList<Category_>();
    private View view;
    private int blue = new Color().rgb(0, 77, 153);
    private int red = new Color().rgb(204, 0, 0);


    class CategoryC
    {
        private String id;
        private float prihodi;
        private float rashodi;

        public CategoryC(String id, float prihodi, float rashodi)
        {
            this.id = id;
            this.prihodi = prihodi;
            this.rashodi = rashodi;
        }

        public float getRashodi()
        {
            return rashodi;
        }

        public void setRashodi(float rashodi)
        {
            this.rashodi += rashodi;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public float getPrihodi()
        {
            return prihodi;
        }

        public void setPrihodi(float prihodi)
        {
            this.prihodi += prihodi;
        }
    }

    public static final Statistics newInstance(String name)
    {
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
        view = inflater.inflate(R.layout.statistics_layout, container, false);
        dataBuilder.getCategories(userID());
        return view;
    }

    private void setLineChart(LineChartView view, String type)
    {
        LineChartData data = new LineChartData();
        int color;

        switch (type)
        {
            case "1":
                color = blue;
                break;
            case "0":
                color = red;
                break;
            default:
                color = blue;
        }
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Datum").setHasTiltedLabels(true);
        axisY.setName("Iznos");

        List<AxisValue> xValues = new ArrayList<AxisValue>();
        List<PointValue> yValues = new ArrayList<PointValue>();
        for (int i = 0; i < records.size(); i++)
        {
            if(records.get(i).getVrsta().equals(type))
            {
                xValues.add(new AxisValue(i, records.get(i).getDatum().substring(0, 10).toCharArray()));
                axisX.setValues(xValues);
                yValues.add(new PointValue(i, Float.valueOf(records.get(i).getIznos())));
            }
        }

        data.setAxisXBottom(new Axis(xValues).setHasLines(true));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        Line line = new Line(yValues).setColor(color);
        line.setHasLabelsOnlyForSelected(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        data.setLines(lines);

        view.setInteractive(true);
        view.setZoomType(ZoomType.HORIZONTAL);
        view.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        view.setLineChartData(data);
    }

    private void setColumnChart(ColumnChartView view)
    {
        ColumnChartData data;
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Kategorija");
        axisY.setName("Iznos");

        int numColumns = categories.size();

        List<CategoryC> tempData = new ArrayList<CategoryC>();
        for(Category_ item : categories)
        {
            tempData.add(new CategoryC(item.getId(), 0, 0));
        }

        for(CategoryC item: tempData)
        {
            for(Record_ record : records)
            {
                if(record.getVrsta().equals("1"))
                    item.setPrihodi(Float.valueOf(record.getIznos()));
                else
                    item.setRashodi(Float.valueOf(record.getIznos()));
            }
        }

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> xValues = new ArrayList<AxisValue>();
        for (int i = 0; i < numColumns; i++)
        {
            values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(tempData.get(i).getPrihodi(), blue));
            values.add(new SubcolumnValue(tempData.get(i).getRashodi(), red));

            xValues.add(new AxisValue(i, categories.get(i).getTitle().toCharArray()));
            axisX.setValues(xValues);

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }

        data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(xValues).setHasLines(true));

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        view.setColumnChartData(data);
    }

    private void setPieChart(PieChartView view, TextView textview)
    {
        PieChartData data;
        float sumP = 0;
        float sumR = 0;
        float fillRatio = 0.9f;

        for(Record_ item : records)
        {
            if(item.getVrsta().equals("1"))
                sumP += Float.valueOf(item.getIznos());
            else
                sumR += Float.valueOf(item.getIznos());
        }

        float difference = sumP - sumR;
        if(difference >= 0.0)
        {
            textview.setText("+ " + String.valueOf(difference) + " HKN");
            textview.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        else
        {
            textview.setText("- " + String.valueOf(Math.abs(difference)) + " HKN");
            textview.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        List<SliceValue> values = new ArrayList<SliceValue>();

        SliceValue sliceP = new SliceValue(Math.round(sumP), blue).setLabel("Prihod (" + sumP + ")");
        SliceValue sliceR = new SliceValue(Math.round(sumR), red).setLabel("Rashod (" + sumR + ")");

        values.add(sliceP);
        values.add(sliceR);

        data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(true);
        data.setHasCenterCircle(true);

        view.setCircleFillRatio(fillRatio);
        view.setPieChartData(data);
    }

    private void setData()
    {
        LineChartView lineGraphPrihodi = (LineChartView) view.findViewById(R.id.lineGraphPrihodi);
        LineChartView lineGraphRashodi = (LineChartView) view.findViewById(R.id.lineGraphRashodi);
        ColumnChartView columnGraphKategorije = (ColumnChartView) view.findViewById(R.id.columnGraphKategorije);
        PieChartView pieGraphPR = (PieChartView) view.findViewById(R.id.pieGraphPR);
        TextView textview = (TextView) view.findViewById(R.id.balance);

        setLineChart(lineGraphPrihodi, "1");
        setLineChart(lineGraphRashodi, "0");
        setPieChart(pieGraphPR, textview);
        setColumnChart(columnGraphKategorije);

        records = null;
        categories = null;
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

        dataBuilder.getRecords(userID());
    }

    @Override
    public void buildData(Object data)
    {
        if(data instanceof pojo.Category)
        {
            pojo.Category category = (pojo.Category) data;
            if (category !=null)
            {
                for (Category_ item : category.getCategory())
                {
                    categories.add(item);
                }
            }
        }

        if(data instanceof pojo.Record)
        {
            pojo.Record record = (pojo.Record) data;
            if (record !=null)
            {
                for (Record_ item : record.getRecord())
                {
                    records.add(item);
                }

                setData();
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
