package my.first;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.btn1);
        btn.setOnClickListener(this.onClick);
    }

    private OnClickListener onClick = new OnClickListener() {

        public void onClick(View v) {
            EditText ea = (EditText) findViewById(R.id.number1);
            EditText eb = (EditText) findViewById(R.id.number2);
            Spinner sp = (Spinner) findViewById(R.id.run);
            int a = Integer.parseInt(ea.getText().toString());
            int b = Integer.parseInt(eb.getText().toString());
            String run = sp.getSelectedItem().toString();
            System.out.print(run);

            int rst = 0;
            if ("+".equalsIgnoreCase(run)) {
                rst = a + b;
            } else if ("-".equalsIgnoreCase(run)) {
                rst = a - b;
            } else if ("*".equalsIgnoreCase(run)) {
                rst = a * b;
            } else if ("/".equalsIgnoreCase(run)) {
                rst = a / b;
            }

            TextView rstv = (TextView) findViewById(R.id.rst);
            rstv.setText(Integer.toString(rst));
            Toast pop = Toast.makeText(TestActivity.this, "oh hohoho",
                    Toast.LENGTH_SHORT);
            pop.show();

        }
    };
}