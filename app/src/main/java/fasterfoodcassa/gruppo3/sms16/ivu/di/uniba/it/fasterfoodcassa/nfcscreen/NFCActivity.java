package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.nfcscreen;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.OrderItem;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.localscreen.RecyclerAdapterRVOrders;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db.DbController;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Order;

public class NFCActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private TextView mToolbarTitle;
    private RecyclerView mOrderRecycler;
    private RecyclerAdapterRVOrders adapter;
    private ProgressBar mProgressBar;
    private TextView txtStringPay;
    private TextView txtTotal;
    private ArrayList<String> names;
    private ArrayList<String> quantities;
    private ArrayList<String> prices;
    private Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        mOrderRecycler = (RecyclerView) findViewById(R.id.orderRecycler);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarOrder);
        txtStringPay = (TextView) findViewById(R.id.txtStringPay);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage ndefMessage = (NdefMessage) rawMessages[0];
            String pk = new String(ndefMessage.getRecords()[0].getPayload());

            retrieveOrder(pk);
        }
    }

    void retrieveOrder(final String pk){
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                order = new DbController().getOrder(getResources().getString(R.string.DB_Orders), pk);
                if(order != null){
                    names = new ArrayList<>();
                    prices = new ArrayList<>();
                    quantities = new ArrayList<>();

                    for(int i = 0; i < order.getItems().size(); i++){
                        OrderItem item = order.getItems().get(i);
                        names.add(item.getNome());
                        quantities.add(item.getQuantita());
                        prices.add(item.getPrezzo());
                    }

                    setupToolbar();
                    setupRecycler(names, quantities, prices);
                    disableProgressBar();
                }
            }


        };
        thread.start();
    }

    void setupRecycler(final ArrayList<String> names, final ArrayList<String> quantities, final ArrayList<String> prices){
        NFCActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOrderRecycler.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mOrderRecycler.setLayoutManager(mLayoutManager);
                NFCRVAdapter adapterNfcRV = new NFCRVAdapter(names,quantities,prices);
                mOrderRecycler.setAdapter(adapterNfcRV);

                if(order.getStato() != null) {
                    if (order.getStato().equals("aperto"))
                        txtStringPay.setText(getResources().getString(R.string.not_payed));
                    else
                        txtStringPay.setText(getResources().getString(R.string.payed));

                    float tot = 0;
                    for (int i = 0; i < prices.size(); i++) {
                        tot = tot + (Float.parseFloat(prices.get(i)) * Float.parseFloat(quantities.get(i)));
                    }

                    txtTotal.setText(String.valueOf(tot) + " €");
                }
                else{
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.error), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    void setupToolbar(){
        NFCActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToolbarTitle.setText(order.getLocale());
            }
    });
    }

    void disableProgressBar(){
        NFCActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
