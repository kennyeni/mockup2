package mx.kennyeni.immockup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

public class MessageParseQueryAdapter<T extends ParseObject> extends ParseQueryAdapter<T> {

	Context acrtivity;
	LayoutInflater inflater;
	String currentUser;
	
	public MessageParseQueryAdapter(Context context,
			com.parse.ParseQueryAdapter.QueryFactory<T> queryFactory, String currentUser) {
		super(context, queryFactory);
		acrtivity = context;
		this.currentUser = currentUser;
	}
	
	@Override
	public View getItemView(T object, View v, ViewGroup parent) {
		if (object == null)
			return null;
		ParseObject ob = (ParseObject) object;
		String emisor = ob.getString("Emisor");
		String mensaje = ob.getString("Mensaje");
		SimpleDateFormat n = new SimpleDateFormat("HH:mm");
		String hora = n.format(ob.getCreatedAt());
		inflater = (LayoutInflater) acrtivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (!emisor.equals(currentUser)){
			v = inflater.inflate(R.layout.mensaje_para, null, false);
			((TextView)v.findViewById(R.id.mensaje_para)).setText(mensaje);
			((TextView)v.findViewById(R.id.mensaje_para_fecha)).setText(hora);
			
		}else{
			v = inflater.inflate(R.layout.mensaje_de, null, false);
			((TextView)v.findViewById(R.id.mensaje_de)).setText(mensaje);
			((TextView)v.findViewById(R.id.mensaje_de_fecha)).setText(hora);
		}
		
		return v;
	}

}
