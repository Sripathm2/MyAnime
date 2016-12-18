package mishra.sripath.myanime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnimeDesc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_desc);
        Intent intent = getIntent();
        String description=intent.getStringExtra("desc");
        TextView t=(TextView)findViewById(R.id.textView3);
        t.setText(description);
    }
}
