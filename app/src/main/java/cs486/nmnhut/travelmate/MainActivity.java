package cs486.nmnhut.travelmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button btnGo;
    private TextView txtPosition;
    private EditText editText;
    private TextView mTextMessage;
    private WebView view;
    private String username;
    private static LocationHelper locationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        //----------------------------\\

        //----------------------------\\
        Intent myIntent = this.getIntent();
        this.username = myIntent.getStringExtra("username");
        locationHelper = new LocationHelper(this,this.username);
        //locationHelper.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},100);

        }
        else
        {
            view = (WebView)findViewById(R.id.webview);

            view.getSettings().setJavaScriptEnabled(true);
            // this function is used to access javascript from html page
            view.addJavascriptInterface(new JavaScriptInterface(this), "AndroidNativeCode");
            // load file from assets folder

            view.loadUrl("file:///android_asset/map.html");
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if (requestCode == locationHelper.MY_PERMISSIONS_REQUEST_FINE_LOCATION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            locationHelper.requestLocationUpdate();
    }

    public class JavaScriptInterface {
        Context mContext;


        JavaScriptInterface(Context c) {
            mContext = c;
        }


        // method to send JsonArray to HTML
        @JavascriptInterface
        public void getValueJson() throws JSONException
        {
            final JSONArray jArray = new JSONArray();


            JSONObject jObject = new JSONObject();

            jObject.put("Country", "Germany");
            jObject.put("Popularity","100");
            jObject.put("Temperature","513");
            jArray.put(jObject);

            jObject = new JSONObject();
            jObject.put("Country", "Brazil");
            jObject.put("Popularity","200");
            jObject.put("Temperature","112");
            jArray.put(jObject);

            jObject = new JSONObject();
            jObject.put("Country", "India");
            jObject.put("Popularity","300");
            jObject.put("Temperature","417");
            jArray.put(jObject);

            jObject = new JSONObject();
            jObject.put("Country", "Australia");
            jObject.put("Popularity","400");
            jObject.put("Temperature","819");
            jArray.put(jObject);

            System.out.println(jArray.toString());
            // send value from java class to html javascript function
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.loadUrl("javascript:setJson(" + jArray + ")");
                }
            });

        }

    }
}
