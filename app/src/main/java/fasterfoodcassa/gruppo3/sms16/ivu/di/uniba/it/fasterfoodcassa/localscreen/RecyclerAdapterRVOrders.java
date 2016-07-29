package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.localscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.R;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Order;

public class RecyclerAdapterRVOrders extends RecyclerView.Adapter<RecyclerAdapterRVOrders.MyViewHolder>{

    private LayoutInflater inflater;
    private List<Order> orderList;
    //private List<SettingsElementRVOrders> data= Collections.emptyList();
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
   /* public RecyclerAdapterRVOrders(Context context,List<SettingsElementRVOrders> data){
        inflater=LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }*/

    public RecyclerAdapterRVOrders(Context context, List<Order> orderList){
        inflater=LayoutInflater.from(context);
        this.orderList = orderList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = inflater.inflate(R.layout.recycler_row_orders,parent,false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText("Ordine " + (position+1));
        holder.tot.setText(orderList.get(position).getTotale() + "€");
        holder.state.setText(orderList.get(position).getStato());
        holder.items.setText(String.valueOf(orderList.get(position).getItems().size()));

        //animate(holder);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        TextView name;
        TextView state;
        TextView tot;
        TextView items;
        public MyViewHolder(View itemView){
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.txtNameOrder);
            state=(TextView) itemView.findViewById(R.id.txtStateOrder);
            tot=(TextView) itemView.findViewById(R.id.txtTotOrder);
            items=(TextView) itemView.findViewById(R.id.txtItems);
        }
    }

    /*
    public void animate(MyViewHolder holder) {
        final Animation animBounce = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        holder.itemView.setAnimation(animBounce);
    }*/
}

