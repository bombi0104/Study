package vn.bombi.study.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import vn.bombi.study.Fragment.TopFragment;
import vn.bombi.study.Helper.NotificationHelper;
import vn.bombi.study.R;

public class MainActivity extends AppCompatActivity {

    NotificationHelper mNotiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

        mNotiHelper = new NotificationHelper(this);
    }

    private void initLayout(){
        TopFragment fragment = new TopFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.rootView, fragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addBigViewNotification(View v){
        String msg = "111111111111111111111111\n2222222222222222222222\n33333333333333333333333\n" +
                "44444444444444444444444444\n555555555555555555555\n666666666666666666666\n" +
                "77777777777777777777777777\n888888888888888888888\n999999999999999999999\n000000000000000";
        mNotiHelper.addBigView(msg);
    }
}
