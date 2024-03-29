package mx.kennyeni.immockup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class NewContactDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add_contact, null))
        // Add action buttons
               .setPositiveButton(R.string.agregar, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // add the new contact                	  
                	   Intent intent = new Intent(getActivity(), Messages.class);
                   	   intent.putExtra(VariablesGlobales.INTENT_MENSAJE_DESTINATARIO, ((EditText)((AlertDialog)dialog).findViewById(R.id.username)).getText().toString() );
                   	   startActivity(intent);
                   }
               })
               .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       NewContactDialogFragment.this.getDialog().cancel();
                   }
               });      
        return builder.create();
    }
    
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


}
