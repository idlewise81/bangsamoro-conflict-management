package lagsit.bangsamoroconflictviewer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import lagsit.bangsamoroconflictviewer.R;
import lagsit.bangsamoroconflictviewer.app.Config;
import lagsit.bangsamoroconflictviewer.chart_items.BarChartItem;
import lagsit.bangsamoroconflictviewer.chart_items.ChartItem;
import lagsit.bangsamoroconflictviewer.chart_items.LineChartItem;
import lagsit.bangsamoroconflictviewer.chart_items.PieChartItem;

public class MainActivity extends AppCompatActivity {//} implements AdapterView.OnItemClickListener {
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    ArrayList<String> dataKey = new ArrayList<>();
    ArrayList<String> dataValue = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
        }
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        setTitle("BANGSAMORO Conflict Viewer");

        // initialize the utilities
        Utils.init(this);


/*
        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        objects.add(new ContentItem("Line Chart", "A simple demonstration of the linechart."));
        objects.add(new ContentItem("Line Chart (Dual YAxis)",
                "Demonstration of the linechart with dual y-axis."));

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
        */

        createColor();
        loadData();

    }


    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        Firebase ref = new Firebase(Config.FIREBASE_URL);
        Query graphYearRef = ref.child(Config.getRefChild(this));

        graphYearRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // System.out.println(dataSnapshot);
                if (dataSnapshot.hasChildren()) {
                    dataKey = new ArrayList<>();
                    dataValue = new ArrayList<>();
                    for (DataSnapshot masterSnapshot : dataSnapshot.getChildren()) {

                        dataKey.add(masterSnapshot.getKey());
                        dataValue.add(masterSnapshot.getValue().toString());

                    }

                    ListView lv = (ListView) findViewById(R.id.listView1);

                    ArrayList<ChartItem> list = new ArrayList<>();
                    list.add(new PieChartItem(generateDataPie(), getApplicationContext(),
                            Config.getRefName(MainActivity.this)));
                    list.add(new BarChartItem(generateDataBar(), getApplicationContext()));
                    list.add(new LineChartItem(generateDataLine(), getApplicationContext()));
                    ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
                    if (lv != null) {
                        lv.setAdapter(cda);
                    }

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void createColor() {
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }


    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < dataKey.size(); i++) {
            entries.add(new BarEntry(i, Integer.valueOf(dataValue.get(i))));

        }
        BarDataSet d = new BarDataSet(entries, Config.getRefType(this) + dataKey.size());
        d.setColors(colors);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        //  for (int i = 0; i < 4; i++) {
        //      entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        //    }
        for (int i = 0; i < dataKey.size(); i++) {
            System.out.println(dataKey.get(i) + "=" + dataValue.get(i));
            entries.add(new PieEntry(Float.valueOf(dataValue.get(i)), dataKey.get(i)));
        }
        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(colors);

        return new PieData(d);
    }


    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<>();

        for (int i = 0; i < dataKey.size(); i++) {
            e1.add(new Entry(i, Integer.valueOf(dataValue.get(i))));

        }
        LineDataSet d1 = new LineDataSet(e1, Config.getRefType(this));
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        /*
        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < dataKey.size(); i++) {
            e2.add(new Entry(i, e1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);
        */

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        //  sets.add(d2);

        return new LineData(sets);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                auth.signOut();
                break;
            }
            case R.id.ic_actor: {
                Config.setRefChild(MainActivity.this, "gr_actor_all");
                Config.setRefName(MainActivity.this, "Incidents\nby\nActors");
                Config.setRefType(MainActivity.this, "Actors");
                Config.setRefLevel(MainActivity.this, 2);
                loadData();
                break;
            }
            case R.id.ic_category: {
                Config.setRefChild(MainActivity.this, "gr_category_all");
                Config.setRefName(MainActivity.this, "Incidents\nby\nCategory");
                Config.setRefType(MainActivity.this, "Category");
                Config.setRefLevel(MainActivity.this, 2);
                loadData();
                break;
            }
            case R.id.ic_province: {
                Config.setRefChild(MainActivity.this, "gr_province_all");
                Config.setRefName(MainActivity.this, "Incidents\nby\nProvince");
                Config.setRefType(MainActivity.this, "Province");
                Config.setRefLevel(MainActivity.this, 2);
                loadData();
                break;
            }
            case R.id.ic_humancost: {
                Config.setRefChild(MainActivity.this, "gr_humancost_all");
                Config.setRefName(MainActivity.this, "Incidents\nby\nHuman Cost");
                Config.setRefLevel(MainActivity.this, 2);
                loadData();
                break;
            }
            case R.id.ic_manifest: {
                Config.setRefChild(MainActivity.this, "gr_manifest_all");
                Config.setRefName(MainActivity.this, "Incidents\nby\nManifest");
                Config.setRefType(MainActivity.this, "Manifest");
                Config.setRefLevel(MainActivity.this, 2);
                loadData();
                break;
            }
            case R.id.ic_gender: {
                Config.setRefChild(MainActivity.this, "gr_gender_all");
                Config.setRefName(MainActivity.this, "Incidents\nby\nGender");
                Config.setRefType(MainActivity.this, "Gender");
                Config.setRefLevel(MainActivity.this, 2);
                loadData();
                break;
            }
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
