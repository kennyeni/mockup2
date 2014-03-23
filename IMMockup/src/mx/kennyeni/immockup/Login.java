package mx.kennyeni.immockup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

public class Login extends Activity {

	private static Activity acti;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);	
		acti = this;
	}

	public void abrirRegistro(View view){
		Intent intent = new Intent(this, Register.class);
		startActivity(intent);
	}
	
	public void sendToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public void logUser(View v){
		String username = ((EditText)findViewById(R.id.login_user)).getText() + "";
		String pass = ((EditText)findViewById(R.id.login_pass)).getText() + "";
		ParseUser.logInInBackground(username, pass, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					sendToast("Login exitoso");
					String username = user.getUsername();
					PushService.subscribe(getApplicationContext(), username, MainActivity.class);
					Intent intent = new Intent(acti,MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					sendToast(e.getMessage());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}
