package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.localscreen;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db.DbController;

public class SeatsDialog extends DialogFragment{

    private EditText txtSeats;
    private Button btnDone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_seats, null);

        txtSeats = (EditText) view.findViewById(R.id.txtSeats);
        btnDone = (Button) view.findViewById(R.id.btnDone);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seats = txtSeats.getText().toString();
                if(!seats.equals("0")){
                    DbController dbController = new DbController();
                    dbController.setSeats(getResources().getString(R.string.DB_Locals),
                            getArguments().getString("localname"), seats);
                }
                dismiss();
            }
        });
    }
}
