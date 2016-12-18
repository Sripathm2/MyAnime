package mishra.sripath.myanime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class marked extends AppCompatActivity {

    final ArrayList<String> AnimeList = new ArrayList<String>();
    final ArrayList<String> AnimeLink = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marked);
        final ListView mainListView = (ListView) findViewById(R.id.mainListView);
        final ArrayAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, AnimeList);
        File root = new File(Environment.getExternalStorageDirectory(), "/Android/data/com.MyAnime/marked.txt");
        try {
            FileReader fr = new FileReader(root);
            BufferedReader bfr = new BufferedReader(fr);

            int n = 0;
            while (true) {
                String read = bfr.readLine();
                if (read == null)
                    break;
                else {
                    String Animename=read.substring(0,read.indexOf('-'));
                    String Animelink=read.substring(read.lastIndexOf('-')+1);
                    AnimeList.add(Animename);
                    AnimeLink.add(Animelink);
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
                mainListView.setAdapter(listAdapter);
                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        call( position );
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
