package ma.myworklab.feed2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;
    List<String> news;
    ListAdapter feedAdapter;
    ListView listFeed;
    ViewSwitcher viewSwitch;
    ImageView image;
    Animation hyperspaceJump;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        toolbar = (Toolbar) findViewById(R.id.main_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        listFeed=(ListView) findViewById(R.id.mainNews);
        listFeed.setOnItemClickListener(this);
        fetchData();
        viewSwitch=(ViewSwitcher) findViewById(R.id.viewSwitcher1) ;
        image=(ImageView) findViewById(R.id.loadingImage);
        hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.loading1);

        if(viewSwitch.getCurrentView()!=image){
            viewSwitch.showPrevious();
            image.startAnimation(hyperspaceJump);
        }
        new DoIt().execute((ArrayList)news);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewSwitch.getCurrentView()!=image){
            viewSwitch.showPrevious();
            image.startAnimation(hyperspaceJump);
        }
        new DoIt().execute((ArrayList)news);
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        if(viewSwitch.getCurrentView()!=image){
            viewSwitch.showPrevious();
            image.startAnimation(hyperspaceJump);
        }
        new DoIt().execute((ArrayList)news);
    }

    private class DoIt extends AsyncTask< List<String>,Integer,List<RssItem>>{

        protected List<RssItem> doInBackground(List<String>... urls){
            try{

                List<String> u=urls[0];
                List<RssItem> listItem =new ArrayList<>() ;
                RssReader rss=new RssReader();
                for(int i=0;i<u.size();i++){
                    rss.setUrl(new URL(u.get(i)));
                    listItem.addAll(rss.getItems());
                }
                Collections.sort(listItem, new Comparator<RssItem>() {
                    public int compare(RssItem o1, RssItem o2) {
                        return o2.getPubDate().compareTo(o1.getPubDate());
                    }
                });
                return listItem;
            }catch (Exception e){
                Log.e("MYAPP", "exception", e);
                return null;
            }

        }

        protected void onPostExecute(List<RssItem> result) {
            feedAdapter=new CustomAdapter(getApplicationContext(),result);
            listFeed.setAdapter(feedAdapter);
            viewSwitch.showNext();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu) {
            SharedPreferences sharedPref=getSharedPreferences("newsList", Context.MODE_PRIVATE);
            Intent mIntent =new Intent(this,menuActivity.class);
            mIntent.putExtra("news1",sharedPref.getBoolean("news1",false));
            mIntent.putExtra("news2",sharedPref.getBoolean("news2",false));
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //custom browser
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RssItem item=(RssItem) adapterView.getItemAtPosition(i);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.getLink()));

        builder.addDefaultShareMenuItem();
        Log.w("MYAPP",item.getLink());
    }
    //get user parameter
    void fetchData(){
        SharedPreferences sharedPref=getSharedPreferences("newsList", Context.MODE_PRIVATE);
        news=new ArrayList<>();
        if(sharedPref.getBoolean("news1",false)){
            news.add("http://www.hespress.com/feed/index.rss");
        }
        if(sharedPref.getBoolean("news2",false)){
            news.add("http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");
        }
    }
}
