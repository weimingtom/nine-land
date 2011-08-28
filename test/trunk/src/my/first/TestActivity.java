package my.first;

import my.first.view.MyView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {
    /** Called when the activity is first created. */
    final static protected int MENU_ABOUT = Menu.FIRST;
    final static protected int MENU_QUIT = Menu.FIRST + 1;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyView myView = new MyView(this);
        setContentView(myView);

//        Button btn = (Button) findViewById(R.id.btn1);
//        btn.setOnClickListener(this.onClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ABOUT, 0, "关于");
        menu.add(0, MENU_QUIT, 1, "退出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case MENU_ABOUT:
//            this.openUri();
            Intent intent = new Intent();
            intent.setClass(TestActivity.this, Second.class);
            Bundle bundle = new Bundle();
            bundle.putString("key", "from bundle");
            intent.putExtras(bundle);
            
            startActivity(intent);
            break;
        case MENU_QUIT:
            this.finish();
            break;
        }
        return true;
    }

    public void openUri() {
        Uri uri = Uri.parse("http://urs.mmou.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
            Uri uri = Uri.parse("http://urs.mmou.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
    };
}