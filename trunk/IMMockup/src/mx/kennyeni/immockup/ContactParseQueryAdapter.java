package mx.kennyeni.immockup;

import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

public class ContactParseQueryAdapter<T extends ParseObject> extends ParseQueryAdapter<T> {

	Context acrtivity;
	LayoutInflater inflater;
	
	public ContactParseQueryAdapter(Context context,
			com.parse.ParseQueryAdapter.QueryFactory<T> queryFactory) {
		super(context, queryFactory);
		acrtivity = context;
	}
	
	@Override
	public View getItemView(T object, View v, ViewGroup parent) {
		ParseObject ob = (ParseObject) object;
		String nombre = ob.getString("Para");
		int mensajes = ob.getInt("Contador");
		if (v == null){
			inflater = (LayoutInflater) acrtivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.contacto, null, false);
		}
		((TextView)v.findViewById(R.id.contacto_nombre)).setText(nombre);
		((TextView)v.findViewById(R.id.contacto_mensajes)).setText(mensajes + "");
		//((ImageView)v.findViewById(R.id.contacto_imagen));
		return v;
	}

}
