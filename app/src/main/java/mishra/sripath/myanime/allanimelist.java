package mishra.sripath.myanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class allanimelist extends AppCompatActivity {
    final ArrayList<String> AnimeList = new ArrayList<String>();
    final ArrayList<String> AnimeLink = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allanimelist);
        final ListView mainListView = (ListView) findViewById(R.id.mainListView);
        final ArrayAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, AnimeList);
        Ion.with(getApplicationContext()).load("http://animehaven.to/anime-list").asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                String arr[]=result.split("a href=");
                for(int i=3;i<arr.length-2;i++)
                {
                    if(arr[i].contains(" class=")&&(arr[i].contains("http://animehaven.to/")))
                    {
                        if(arr[i].contains("Anime List"))
                            break;
                        arr[i]=arr[i].substring(arr[i].indexOf('"')+1);
                        String link=arr[i].substring(0,arr[i].indexOf('"'));
                        arr[i]=arr[i].substring(arr[i].indexOf('>')+1);
                        String title=arr[i].substring(0,arr[i].indexOf('<'));
                        AnimeList.add(title);
                        AnimeLink.add(link);
                    }
                }
                for(int i=0;i<AnimeList.size();i++)
                {
                    for(int j=i+1;j<AnimeList.size();j++)
                        if(AnimeList.get(i).compareToIgnoreCase(AnimeList.get(j))>0){
                            String t=AnimeList.get(i);
                            AnimeList.set(i,AnimeList.get(j));
                            AnimeList.set(j,t);
                            t=AnimeLink.get(i);
                            AnimeLink.set(i,AnimeLink.get(j));
                            AnimeLink.set(j,t);
                        }
                }
                mainListView.setAdapter(listAdapter);
                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        call( position );
                    }
                });

            }

        });
    }
    public void call(int i)
    {
        Intent myIntentA2A3 = new Intent(this,Anime.class);
        myIntentA2A3.putExtra("Name",AnimeList.get(i));
        myIntentA2A3.putExtra("Link",AnimeLink.get(i));
        startActivity(myIntentA2A3);
    }
}
