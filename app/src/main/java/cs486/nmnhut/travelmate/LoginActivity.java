package cs486.nmnhut.travelmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public String username;
    private String password;
    private Button btnLogin;
    private EditText txtPassword;
    private EditText txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeEvents();
    }

    private void InitializeEvents() {
        //----
        txtUsername = findViewById(R.id.txtUserName);
        //----
        txtPassword = findViewById(R.id.txtPassword);
        txtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_GO)
                {
                    buttonLoginPress();
                }
                return false;
            }
        });
        //----------------------------
        btnLogin  = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLoginPress();
            }
        });
        //----------------------------
    }

    String HashedPassword()
    {
        return txtPassword.getText().toString();
    }

    private void buttonLoginPress() {
        ValueEventListener e = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (HashedPassword().equals(dataSnapshot.getValue(String.class)))
                        launchMainActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference ref = db.getReference("user");
        ref.child(txtUsername.getText().toString()).addListenerForSingleValueEvent(e);


    }

    private void launchMainActivity() {
        this.username = txtUsername.getText().toString();
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("username",this.username);
        startActivity(intent);
    }
}
