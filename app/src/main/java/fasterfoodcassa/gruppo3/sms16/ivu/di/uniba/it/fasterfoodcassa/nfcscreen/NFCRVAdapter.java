package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.nfcscreen;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;

public class NFCRVAdapter extends RecyclerView.Adapter<NFCRVAdapter.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> quantities;

    public NFCRVAdapter(ArrayList<String> names, ArrayList<String> quantities, ArrayList<String> prices) {
        this.names = names;
        this.quantities = quantities;
        this.prices = prices;
    }

    public Double getSubTotal(int position){
        Double subtotal = (Double.valueOf(prices.get(position)) * Double.valueOf(quantities.get(position)));
        subtotal = Math.floor(subtotal * 100) / 100; //Per ottenere solo le prime due cifre decimali
        return subtotal;
    }

    @Override
    public NFCRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row_order, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Qui ci sono i dati da "stampare"
        holder.txtNomeProdotto.setText(names.get(position));
        holder.txtPrezzo.setText(prices.get(position));
        holder.txtQuantity.setText(quantities.get(position));

        Double subtotal = getSubTotal(position);
        holder.txtTOT.setText(subtotal.toString());
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtNomeProdotto;
        public TextView txtPrezzo;
        public TextView txtQuantity;
        public TextView txtTOT;

        public ViewHolder(View v) {
            super(v);
            txtNomeProdotto = (TextView) v.findViewById(R.id.txtNomeProdotto);
            txtPrezzo = (TextView) v.findViewById(R.id.txtPrezzo);
            txtQuantity = (TextView) v.findViewById(R.id.txtQuantity);
            txtTOT = (TextView) v.findViewById(R.id.txtTot);
        }
    }
}
