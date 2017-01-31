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
import pojo.*;
import pojo.Category;

/**
 * Created by dominik on 21.12.16..
 */

/**
 * Klasa Statistics za vizualizaciju korisnickih zapisa. Sadrzi sve potrebne metode za dohvacanje, obradu i prikaz podataka.
 * Za vizualizaciju koristi HelloCharts
 * Vise na <a href="https://github.com/lecho/hellocharts-android">HelloCharts</a>
 */
public class Statistics extends BaseFragment implements FragmentInterface, DataInterface
{
    /**
     * Klasa DataBuilder za dohvacanje podataka iz baze podataka
     */
    private DataBuilder dataBuilder = new DataBuilder(this);

    /**
     * Klasa SharedPreferences za dohvaćanje korisnickog user_id atributa iz aktivne sesije
     */
    private SharedPreferences preferences;

    /**
     * Lista tipa Record koja sadrzi sve podatke koji će biti prikazani na grafikonima
     */
    private ArrayList<Record> records;

    /**
     * Lista tipa Category. Sadrzi kategorije aktivnog korisnika.
     */
    private ArrayList<Category> categories;

    /**
     * Klasa View sadrži sve komponente korisnickog sucelja
     */
    private View view;

    /**
     * Definicija nijanse plave boje.
     */
    private int blue = new Color().rgb(0, 77, 153);

    /**
     * Definicija nijanse crvene boje.
     */
    private int red = new Color().rgb(204, 0, 0);

    /**
     * Pomocna varijabla broj kategorija potrebna zbog pogreske prilikom generiranje podataka
     * za vizualizaciju
     */
    private int categoriesSize;

    /**
     * Pomocna klasa CategoryC potrebna prilikom pripreme podataka za vizualizaciju.
     */
    class CategoryC
    {
        /**
         * Identifikator kategorije potreban za dohvacanje naziva kategorije
         */
        private String id;

        /**
         * Suma prihoda za pojedinu kategoriju
         */
        private float prihodi;

        /**
         * Suma rashoda za pojedinu kategoriju
         */
        private float rashodi;

        /**
         * Konstruktor klase CategoryC
         * @param id Identifikator kategorije
         * @param prihodi Iznos prihoda pojedinog zapisa
         * @param rashodi Iznos rashoda pojedinog zapisa
         */
        public CategoryC(String id, float prihodi, float rashodi)
        {
            this.id = id;
            this.prihodi = prihodi;
            this.rashodi = rashodi;
        }

        /**
         * @return ukupan iznos rashoda za kategoriju
         */
        public float getRashodi()
        {
            return rashodi;
        }

        /**
         * Zbraja iznos rashoda na postojecu sumu. Moguce je da postoji vise zapisa u istoj kategoriji pa je potrebno zbrojiti sve iznose.
         * @param rashodi Iznos rashoda za jedan zapis
         */
        public void setRashodi(float rashodi)
        {
            this.rashodi += rashodi;
        }

        /**
         * @return Identifikator kategorije
         */
        public String getId()
        {
            return id;
        }

        /**
         * Postavlja identifikator kategorije
         * @param id Identifikator kategorije
         */
        public void setId(String id)
        {
            this.id = id;
        }

        /**
         * @return ukupan iznos prihoda za kategoriju
         */
        public float getPrihodi()
        {
            return prihodi;
        }

        /**
         * Zbraja iznos prihoda na postojecu sumu. Moguce je da postoji vise zapisa u istoj kategoriji pa je potrebno zbrojiti sve iznose.
         * @param prihodi Iznos prihoda za jedan zapis
         */
        public void setPrihodi(float prihodi)
        {
            this.prihodi += prihodi;
        }
    }

    /**
     * Konstruktor fragmenta Statistics
     * @param name Naziv fragmenta
     * @return Fragment Statistics
     */
    public static final Statistics newInstance(String name)
    {
        Statistics s = new Statistics();
        s.setName(name);

        return s;
    }

    /**
     *
     * @return
     */
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

    /**
     * Metoda priprema i postavlja podatke na LineChartView
     * @param view Objekt tipa LineChartView
     * @param type Objekt tipa String koji predstavlja vrstu zapisa (prihod/rashod)
     */
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

    /**
     * Metoda priprema i postavlja podatke na ColumnChartView
     * @param view Objekt tipa ColumnChartView
     */
    private void setColumnChart(ColumnChartView view)
    {
        ColumnChartData data;
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Kategorija");
        axisY.setName("Iznos");

        int numColumns = categoriesSize;
        float nullCatP = 0;
        float nullCatR = 0;

        List<CategoryC> tempData = new ArrayList<CategoryC>();
        for(Category item : categories)
        {
            tempData.add(new CategoryC(item.getId(), 0, 0));
        }

        for(Record record : records)
        {
            if(record.getCatgoryId() == null)
            {
                if(record.getVrsta().equals("1"))
                    nullCatP += (Float.valueOf(record.getIznos()));
                else
                    nullCatR += (Float.valueOf(record.getIznos()));

                records.remove(record);
                numColumns++;
            }
        }

        for(CategoryC item: tempData)
        {
            for(Record record : records)
            {
                if(record.getCatgoryId().equals(item.getId()))
                {
                    if(record.getVrsta().equals("1"))
                        item.setPrihodi(Float.valueOf(record.getIznos()));
                    else
                        item.setRashodi(Float.valueOf(record.getIznos()));
                }
            }
        }

        if(nullCatP > 0 || nullCatR > 0)
        {
            tempData.add(new CategoryC("-1", nullCatP, nullCatR));
        }

        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> xValues = new ArrayList<AxisValue>();
        for (int i = 0; i < numColumns; i++)
        {
            values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(tempData.get(i).getPrihodi(), blue));
            values.add(new SubcolumnValue(tempData.get(i).getRashodi(), red));


            if(tempData.get(i).getId().equals("-1"))
            {
                xValues.add(new AxisValue(i, "NEMA".toCharArray()));
            }
            else
            {
                xValues.add(new AxisValue(i, categories.get(i).getTitle().toCharArray()));
            }

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

    /**
     * Metoda priprema i postavlja podatke na PieChartView
     * Izracunava razliku prihoda i rashoda i postavlja stanje u textview
     * @param view Objekt tipa PieChartView
     * @param textview Objekt tipa TextView
     */
    private void setPieChart(PieChartView view, TextView textview)
    {
        PieChartData data;
        float sumP = 0;
        float sumR = 0;
        float fillRatio = 0.9f;

        for(Record item : records)
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

    /**
     * Metoda kojom se inicijaliziraju potrebni HelloCharts objekti
     * Sadrzi pozive za svaki tip korištenog HelloCharts objekta
     * Nakon postavljanja podataka, liste records i categories postavljaju se u null da se
     * sprijeci daljnje dupliciranje podataka prilikom ponovnog pozivanja fragmenta
     */
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
    }

    /**
     * Metoda za preuzimanje podatata iz database modula
     * @param data Objekt rezultata (odgovora) web servisa
     */
    @Override
    public void buildData(Object data)
    {
        if(data instanceof Categories)
        {
            Categories category = (Categories) data;
            categories = new ArrayList<pojo.Category>();
            if (category !=null)
            {
                for (Category item : category.getCategory())
                {
                    categories.add(item);
                }
            }

            categoriesSize = categories.size();
            dataBuilder.getRecords(userID());
        }

        if(data instanceof Records)
        {
            Records record = (Records) data;
            records = new ArrayList<Record>();
            if (record !=null)
            {
                for (Record item : record.getRecord())
                {
                    records.add(item);
                }

                setData();
            }
        }
    }

    /**
     * Metoda za dohvacanje korisnickog user_id atributa iz SharedPreferences
     * @return Korisnicki identifikator
     */
    private String userID()
    {
        preferences = getActivity().getSharedPreferences("login", 0);
        String status = preferences.getString("id", "");
        return  status;
    }
}
