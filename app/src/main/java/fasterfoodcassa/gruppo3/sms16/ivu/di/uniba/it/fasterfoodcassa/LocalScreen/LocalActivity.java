package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.LocalScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.RestaurantChoiceActivity;

public class LocalActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private SplitToolbar bottomToolbar;
    private TextView mToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        bottomToolbar = (SplitToolbar) findViewById(R.id.bottom_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbarTitle);


        setupToolbar();



    }

    void setupToolbar(){
        mToolbarTitle.setText(getIntent().getStringExtra("localName"));

        if (bottomToolbar != null) {
            EnhancedMenuInflater.inflate(getMenuInflater(), bottomToolbar.getMenu(), true);
            bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();

                    switch(item.getItemId()){
                        case R.id.action_update: {
                            Toast.makeText(getApplicationContext(), "refresh", Toast.LENGTH_LONG).show();
                            break;
                        }

                        case R.id.action_receive_order:{
                            Toast.makeText(getApplicationContext(), "receive", Toast.LENGTH_LONG).show();
                            break;
                        }

                        case R.id.action_set_seats:{
                            Toast.makeText(getApplicationContext(), "seats", Toast.LENGTH_LONG).show();
                            break;
                        }

                        case R.id.action_change_local:{
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

}
