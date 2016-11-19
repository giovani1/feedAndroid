package ma.myworklab.feed2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.support.v7.app.ActionBar;


public class menuActivity extends AppCompatActivity {


    Toolbar toolbar;
    Switch news1;
    Switch news2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);
        news1 =(Switch) findViewById(R.id.switch1);
        news2 = (Switch) findViewById(R.id.switch2);
        news1.setChecked(getIntent().getBooleanExtra("news1",false));
        news2.setChecked(getIntent().getBooleanExtra("news2",false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void savedPref(View view){
        SharedPreferences sharedPref=getSharedPreferences("newsList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        Log.w("MYAPP", news1.isChecked()?"true":"false");
        Log.w("MYAPP", news2.isChecked()?"true":"false");
        editor.putBoolean("news1",news1.isChecked());
        editor.putBoolean("news2",news2.isChecked());
        editor.apply();
        //Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();

    }
}
