package mx.kennyeni.immockup;

import com.parse.Parse;

import android.app.Application;

public class Aplicacion extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(VariablesGlobales.counter){
        	Parse.initialize(this, "I7aW7NSedlwvNtAjZmQm9bcUT7FPSvl10SOauIey", "L3DPvfP1pTn7MvVzoOzQqBqwqHDp1DKGOFXj8dzt");
        	VariablesGlobales.counter = false;
        }
	}

}
