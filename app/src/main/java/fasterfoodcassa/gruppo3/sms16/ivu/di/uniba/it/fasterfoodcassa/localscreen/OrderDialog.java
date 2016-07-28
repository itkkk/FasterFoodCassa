package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.localscreen;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.nfcscreen.NFCRVAdapter;

public class OrderDialog extends DialogFragment{

    private RecyclerView recyclerView;
    private TextView txtPrice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_order, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.orderRecycler);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NFCRVAdapter adapter=new NFCRVAdapter(getArguments().getStringArrayList("names"),
                getArguments().getStringArrayList("quantities"), getArguments().getStringArrayList("prices"));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        float tot = 0;
        for(int i = 0; i < getArguments().getStringArrayList("names").size(); i++){
            tot += adapter.getSubTotal(i);
        }

        txtPrice.setText(tot + " €");
    }
}
