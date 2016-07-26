package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db.DbController;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Local;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.LocalsList;

public class RestaurantChoiceActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private ArrayList<Local> locals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_choice);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView) findViewById(R.id.listViewLocals);

        setupToolbar();
        setupLocalList();

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
                LocalsList localsList = new DbController().queryLocals(getResources().getString(R.string.DB_Locals));
                if(localsList != null){

                    locals = localsList.getLocals();

                    RestaurantChoiceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);

                            setListView();
                        }
                    });

                }
            }
        };
        getLocals.start();
    }

    void setListView(){
        ArrayList<String> nameList = new ArrayList<String>();
        for(int i = 0; i < locals.size(); i++){
            nameList.add(locals.get(i).getNome());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String> (getApplicationContext()
                ,R.layout.local_list_item, R.id.txtLocal, nameList);

        mListView.setAdapter(adapter);
    }
}
