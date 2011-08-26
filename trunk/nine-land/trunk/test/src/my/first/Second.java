package my.first;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Second extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Bundle bundle = this.getIntent().getExtras();
        String key = bundle.getString("key");

        TextView t = (TextView) this.findViewById(R.id.second_text);
        t.setText(key);

        Button b = (Button) this.findViewById(R.id.button1);
        b.setOnClickListener(this.onclick);
    }

    private OnClickListener onclick = new OnClickListener() {

        public void onClick(View v) {
            Second.this.finish();

        }
    };
}
