package mishra.sripath.myanime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Anime extends AppCompatActivity {
    final ArrayList<String> EPList = new ArrayList<String>();
    final ArrayList<String> EPLink = new ArrayList<String>();
    String description;
    String pagelink="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Intent intent = getIntent();
        final String Animelink=intent.getStringExtra("Link");
        final String AnimeTitle=intent.getStringExtra("Name");
        final ListView mainListView = (ListView) findViewById(R.id.mainListView);
        final ArrayAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, EPList);
        Ion.with(getApplicationContext()).load(Animelink).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                TextView t=(TextView)findViewById(R.id.textView2);
                t.setText(AnimeTitle);
                String res[] = result.split("class=\"synopsys\"> <p>");
                String res1[]=res[1].split("</p> </div>");
                description=res1[0];
                description=description.replace("</p> <p>"," ");
                res=result.split("a href=\"");
               for(int i=0;i<res.length;i++)
               {
                   if(res[i].contains("View all"))
                       pagelink=res[i].substring(0,res[i].indexOf('"'));
               }
                Ion.with(getApplicationContext()).load(pagelink).asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        String res[]=result.split("a href=\"");
                        int page=1;
                        for(int i=1;i<res.length;i++)
                        {
                            if(res[i].contains("Episode ")&&res[i].contains(AnimeTitle)&&res[i].contains("Perma")==false)
                            {
                                String epilink=res[i].substring(0,res[i].indexOf('"'));
                                res[i]=res[i].substring(res[i].indexOf('"')+1);
                                res[i]=res[i].substring(res[i].indexOf('"')+1);
                                String epiname=res[i].substring(0,res[i].indexOf('"'));
                                EPLink.add(epilink);
                                EPList.add(epiname);
                            }
                            if(res[i].contains("page/"))
                            {
                                int lastIndex = 0;
                                while(lastIndex != -1){

                                    lastIndex = res[i].indexOf("page/",lastIndex);

                                    if(lastIndex != -1){
                                        page++;
                                        lastIndex += "page/".length();
                                    }
                                }
                            }
                        }
                        if(page>1)
                        while(page>1)
                        {
                            String url=pagelink+"/page/"+String.valueOf(page);
                            page-=1;
                            Ion.with(getApplicationContext()).load(url).asString().setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    String res[]=result.split("a href=\"");
                                    for(int i=1;i<res.length;i++)
                                    {
                                        if(res[i].contains("Episode ")&&res[i].contains(AnimeTitle)&&res[i].contains("Perma")==false)
                                        {
                                            String epilink=res[i].substring(0,res[i].indexOf('"'));
                                            res[i]=res[i].substring(res[i].indexOf('"')+1);
                                            res[i]=res[i].substring(res[i].indexOf('"')+1);
                                            String epiname=res[i].substring(0,res[i].indexOf('"'));
                                            EPLink.add(epilink);
                                            EPList.add(epiname);
                                            if(Double.parseDouble(epiname.substring(epiname.lastIndexOf(' ')+1))==1)
                                            {
                                                EPList.add(" ");
                                                EPLink.add(" ");
                                            }
                                        }
                                    }
                                    for(int i=0;i<EPList.size();i++)
                                    {
                                        for(int j=i+1;j<EPList.size();j++) {

                                            double pi,pj;
                                            if(EPList.get(i).length()>2&&EPList.get(j).length()>2) {
                                                pi = Double.parseDouble(EPList.get(i).substring(EPList.get(i).lastIndexOf(' ') + 1));
                                                pj = Double.parseDouble(EPList.get(j).substring(EPList.get(j).lastIndexOf(' ') + 1));
                                                if (pj > pi) {
                                                    String t = EPList.get(i);
                                                    EPList.set(i, EPList.get(j));
                                                    EPList.set(j, t);
                                                    t = EPLink.get(i);
                                                    EPLink.set(i, EPLink.get(j));
                                                    EPLink.set(j, t);
                                                }
                                            }
                                        }
                                    }
                                    listAdapter.notifyDataSetChanged();
                                    System.out.println(EPList.size());
                                }

                            });
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

        });
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(Anime.this,AnimeDesc.class);
                activityChangeIntent.putExtra("desc", description);
                startActivity(activityChangeIntent);
            }
        });
        final Button button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File root = new File(Environment.getExternalStorageDirectory(), "/Android/data/com.MyAnime/marked.txt");
                try {
                    FileOutputStream of = new FileOutputStream(root, true);
                    PrintWriter od = new PrintWriter(of);
                    od.println(AnimeTitle + "--" + Animelink);
                    od.close();
                    of.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void call(int i) {
        Intent myIntentA2A3 = new Intent(this, Episode.class);

        myIntentA2A3.putExtra("Link", EPLink.get(i));
        startActivity(myIntentA2A3);
    }
}
