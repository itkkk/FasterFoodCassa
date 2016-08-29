package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.localscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.RestaurantChoiceActivity;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db.DbController;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Order;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.OrderItem;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.OrderList;

public class LocalActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private SplitToolbar bottomToolbar;
    private TextView mToolbarTitle;
    private RecyclerView mOrderRecycler;
    private RecyclerAdapterRVOrders adapter;
    private ProgressBar mProgressBar;
    private View rootView;
    private OrderList orderList;
    private String localName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        bottomToolbar = (SplitToolbar) findViewById(R.id.bottom_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        mOrderRecycler = (RecyclerView) findViewById(R.id.ordersRecycler);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLocal);
        rootView = (View) findViewById(android.R.id.content);

        setupToolbar();
        retrieveOrdersAndSetRecycler();

    }

    void setupToolbar() {
        localName = getIntent().getStringExtra("localName");
        mToolbarTitle.setText(localName);

        if (bottomToolbar != null) {
            EnhancedMenuInflater.inflate(getMenuInflater(), bottomToolbar.getMenu(), true);
            bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_update: {
                            mOrderRecycler.setVisibility(View.INVISIBLE);
                            retrieveOrdersAndSetRecycler();
                            break;
                        }

                        case R.id.action_set_seats: {
                            Bundle bundle = new Bundle();
                            bundle.putString("localname", mToolbarTitle.getText().toString());
                            SeatsDialog seatsDialog = new SeatsDialog();
                            seatsDialog.setArguments(bundle);
                            seatsDialog.show(getFragmentManager(), null);
                            break;
                        }

                        case R.id.action_change_local: {
                            SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("localName", null);
                            editor.commit();

                            Intent i = new Intent(getApplicationContext(), RestaurantChoiceActivity.class);
                            startActivity(i);
                            break;
                        }
                    }
                    return true;
                }
            });
        }
    }

    void retrieveOrdersAndSetRecycler(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                modifyProgressBarVisibility(true);
                orderList = new DbController().getOrders(getResources().getString(R.string.DB_Orders), localName);
                if(orderList != null){
                    if(orderList.getOrders().size() == 0){
                        Snackbar.make(rootView, getResources().getString(R.string.no_orders),Snackbar.LENGTH_LONG).show();
                        modifyProgressBarVisibility(false);

                    }
                    else{
                        modifyProgressBarVisibility(false);
                        setRecycler();
                    }
                }
                else {
                    Snackbar.make(rootView, getResources().getString(R.string.no_orders),Snackbar.LENGTH_LONG).show();
                    modifyProgressBarVisibility(false);
                }
            }
        };
        thread.start();
    }

    void setRecycler(){
        LocalActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter=new RecyclerAdapterRVOrders(getApplicationContext() ,orderList.getOrders());
                mOrderRecycler.setAdapter(adapter);
                mOrderRecycler.setVisibility(View.VISIBLE);
                mOrderRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                mOrderRecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                        mOrderRecycler, new RecyclerTouchListener.ClickListener(){

                    @Override
                    public void onClick(View view, int position) {
                        Order order = orderList.getOrders().get(position);
                        ArrayList<String> names = new ArrayList<>();
                        ArrayList<String> quantitites = new ArrayList<>();
                        ArrayList<String> prices = new ArrayList<>();

                        for(int i = 0; i < order.getItems().size(); i++){
                            OrderItem item = order.getItems().get(i);
                            names.add(item.getNome());
                            quantitites.add(item.getQuantita());
                            prices.add(item.getPrezzo());
                        }
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("names",names);
                        bundle.putStringArrayList("quantities", quantitites);
                        bundle.putStringArrayList("prices",prices);
                        OrderDialog orderDialog = new OrderDialog();
                        orderDialog.setArguments(bundle);
                        orderDialog.show(getFragmentManager(),null);
                    }

                    @Override
                    public void onLongClick(View view, int position) {}
                }));
            }
        });
    }

    void modifyProgressBarVisibility(final boolean visibility){
        LocalActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(visibility)
                    mProgressBar.setVisibility(View.VISIBLE);
                else
                    mProgressBar.setVisibility(View.GONE);
            }
        });
    }

}

