package mx.kennyeni.immockup;


import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	SignUpCallback callback = new SignUpCallback() {
		
		@Override
		public void done(ParseException e) {
			if(e == null){
				sendToast("Registro exitoso");
			}else{
				
				sendToast(e.getMessage());
			}
		}
	};
	
	public void registrarUsuario(View view){
		String mail = ((EditText)findViewById(R.id.register_mail)).getText() + "";
		String username = ((EditText)findViewById(R.id.login_user)).getText() + "";
		String pass = ((EditText)findViewById(R.id.login_pass)).getText() + "";
		ParseUser user = new ParseUser();
		user.setEmail(mail);
		user.setUsername(username);
		user.setPassword(pass);
		
		user.signUpInBackground(callback);
		
	}
	
	

}
