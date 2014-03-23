package mx.kennyeni.immockup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class Messages extends Activity {
	private ParseUser currentUser;
	private String usuarioContraparte;
	MessageParseQueryAdapter<ParseObject> adaptador;
	private ListView lst;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
			try {
				JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
				String emisor = (String) json.get("Emisor");
				if(emisor.equals(usuarioContraparte)){
					adaptador.loadObjects();
				}
			} catch (JSONException e) {
				 Log.d("IMMockup", "JSONException: " + e.getMessage());
			}
        }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		ParseAnalytics.trackAppOpened(getIntent());
		currentUser = ParseUser.getCurrentUser();		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {			
		    usuarioContraparte = extras.getString(VariablesGlobales.INTENT_MENSAJE_DESTINATARIO);
		    if(usuarioContraparte.equals(currentUser.getUsername())){
		    	sendToast("No puedes hablar contigo mismo -.-");
		    	this.finish();
			}		    
		}else{
			this.finish();
		}
		lst = (ListView)findViewById(R.id.mensajes_mensajes);
		setTitle(usuarioContraparte);
	}

	@Override
	protected void onStart() {
		super.onStart();
		cargarConversaciones();
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("De", usuarioContraparte);
		values.put("Para", currentUser.getUsername());
		ParseCloud.callFunctionInBackground("marcarLeido", values, leido);
	}
	
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
        filter.addAction("mx.kennyeni.immockup.UPDATE_STATUS");
        this.registerReceiver(this.receiver, filter);
	};
	
	public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.receiver);
    }
	
	FunctionCallback<Object> leido = new FunctionCallback<Object>() {

		@Override
		public void done(Object object, ParseException e) {
			if (e != null){
				Log.e("IMMockup", e.getMessage());
			}
			
		}
	};
	
	private void cargarConversaciones() {
		// Adpatador con todas las conversaciones
				ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
							@Override
							public ParseQuery<ParseObject> create() {
								ArrayList<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
								
								ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Mensajes");
								query1.whereEqualTo("Destinatario", currentUser.getUsername());
								query1.whereEqualTo("Emisor", usuarioContraparte);
								queries.add(query1);
								
								ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Mensajes");
								query2.whereEqualTo("Emisor", currentUser.getUsername());
								query2.whereEqualTo("Destinatario", usuarioContraparte);
								queries.add(query2);
								
								return ParseQuery.or(queries).orderByAscending("updatedAt");
							}
						};
				adaptador = new MessageParseQueryAdapter<ParseObject>(this, factory, currentUser.getUsername());
				adaptador.setTextKey("Mensaje");
				
				adaptador.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {
					@Override
					public void onLoaded(List<ParseObject> objects, Exception e) {
						/*ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
						//root.removeView(progressBar);
						
						if (objects.size() < 1){
							TextView tx = new TextView(getApplicationContext());
							tx.setText("Sin conversaciones :(");
							tx.setGravity(Gravity.CENTER);
							android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
							lp.gravity = Gravity.CENTER;
							tx.setLayoutParams(lp);
							root.addView(tx);
						}
						*/
					}

					@Override
					public void onLoading() {
						// Create a progress bar to display while the list load
						/*LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				                LayoutParams.WRAP_CONTENT);
				        progressBar.setLayoutParams(lp);
				        progressBar.setIndeterminate(true);

				        // Must add the progress bar to the root of the layout
				        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
				        root.addView(progressBar);*/
				        
					}
				});
				adaptador.setAutoload(true);
				adaptador.setObjectsPerPage(500);
				lst.setAdapter(adaptador);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add("Agregar contacto");		
		menu.add("Salir");		
		//System.out.println(menu.FIRST);
		return true;
	}
	
	private void sendToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public void sendMessage(View v){
		EditText msg1 = (EditText)findViewById(R.id.mensajes_input);
		String str = (msg1).getText() + "";		
		ParseObject msg = new ParseObject("Mensajes");
		msg.put("Emisor", currentUser.getUsername());
		msg.put("Destinatario", usuarioContraparte);
		msg.put("Mensaje", str);
		msg.saveInBackground(messageCallback);
		msg1.setEnabled(false);
		sendToast("Enviando mensaje...");
	}
	
	SaveCallback messageCallback = new SaveCallback() {
		
		@Override
		public void done(ParseException e) {
			if (e == null){
				EditText msg1 = (EditText)findViewById(R.id.mensajes_input);
				msg1.setText("");
				adaptador.loadObjects();
				sendToast("Mensaje enviado");
				msg1.setEnabled(true);
			}else{
				sendToast(e.getMessage());
			}
			
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//System.out.println(item.getTitle());
	    if(item.getTitle().toString().equals("Agregar contacto")) {	    	
	    	DialogFragment newFragment = new NewContactDialogFragment();
	    	newFragment.show(getFragmentManager(), "newContact"); 
	    }else if (item.getTitle().toString().equals("Salir")){
	    	String username = currentUser.getUsername();
			PushService.unsubscribe(this, username);
	    	ParseUser.logOut();
	        Intent intent = new Intent(this,Login.class);	            
			startActivity(intent);
			this.finish();
	    }else{
	            return super.onOptionsItemSelected(item);
	    }	
	    return true;
	}
	
}
