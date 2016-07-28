package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.localscreen.LocalActivity;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db.DbController;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Local;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.LocalsList;

public class RestaurantChoiceActivity extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;
    private Toolbar myToolbar;
    private ProgressBar mProgressBar;
    private TextView mTxtSelect;
    private ListView mListView;
    private ArrayList<Local> locals;
    private ArrayList<String> nameList;
    private boolean LOCALS_LOADED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_choice);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.restaurantChoiceLayout);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTxtSelect = (TextView) findViewById(R.id.txtSelect);
        mListView = (ListView) findViewById(R.id.listViewLocals);

        SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String localName = prefs.getString("localName", null);
        if(localName != null){
            Intent i = new Intent(getApplicationContext(), LocalActivity.class);
            i.putExtra("localName", localName );
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK); //pulisce il backstack
            startActivity(i);
        }
        else{
            setupToolbar();

            if(savedInstanceState != null){
                LOCALS_LOADED = savedInstanceState.getBoolean("LOCALS_LOADED");
                if(LOCALS_LOADED) {
                    nameList = savedInstanceState.getStringArrayList("nameList");
                    setListView();
                    modifyLayout(false, true);
                }
                else {
                    setupLocalList();
                }
            }
            else {
                setupLocalList();
            }
        }



    }

    //inizializza toolbar
    private void setupToolbar(){
        //myToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(myToolbar);
    }

    private void setupLocalList(){
        Thread getLocals = new Thread(){
            @Override
            public void run() {
                super.run();
                modifyLayout(true, false);
                LocalsList localsList = new DbController().queryLocals(getResources().getString(R.string.DB_Locals));
                if(localsList != null){
                    nameList = new ArrayList<>();
                    locals = localsList.getLocals();
                    if(locals.size() == 0){
                        LOCALS_LOADED = false;
                        Snackbar.make(mRelativeLayout, getResources().getString(R.string.empty_DB),Snackbar.LENGTH_INDEFINITE).show();
                        modifyLayout(false, false);
                    }
                    else {
                        LOCALS_LOADED = true;
                        for (int i = 0; i < locals.size(); i++) {
                            nameList.add(locals.get(i).getNome());
                        }
                        setListView();
                        modifyLayout(false, true);
                    }
                }
            }
        };
        getLocals.start();
    }

    //hide or show progressbar or txtSelect
    void modifyLayout(final boolean showProgressBar, final boolean showTxtSelect){
        RestaurantChoiceActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(showProgressBar)
                    mProgressBar.setVisibility(View.VISIBLE);
                else
                    mProgressBar.setVisibility(View.GONE);

                if(showTxtSelect)
                    mTxtSelect.setVisibility(View.VISIBLE);
                else
                    mTxtSelect.setVisibility(View.INVISIBLE);

            }
        });
    }

    void setListView(){
        RestaurantChoiceActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter < String > adapter = new ArrayAdapter<>(getApplicationContext()
                        , R.layout.local_list_item, R.id.txtLocal, nameList);
                mListView.setAdapter(adapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences prefs = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("localName", nameList.get(position));
                        editor.commit();

                        Intent i = new Intent(getApplicationContext(), LocalActivity.class);
                        i.putExtra("localName",  nameList.get(position));
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK); //pulisce il backstack
                        startActivity(i);
                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("nameList", nameList);
        outState.putBoolean("LOCALS_LOADED", LOCALS_LOADED);
    }
}
