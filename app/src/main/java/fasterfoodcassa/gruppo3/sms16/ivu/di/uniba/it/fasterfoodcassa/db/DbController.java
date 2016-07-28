package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db;

import android.app.Application;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Local;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.LocalsList;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Order;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.OrderItem;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.OrderList;

public class DbController extends Application{
    static private boolean connected;


    @Override
    public void onCreate() {
        super.onCreate();
        //inizializzazione Firebase
        Firebase.setAndroidContext(getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public DbController(){}

    static public Boolean isConnected(String DBUrl){
        Firebase connectedRef = new Firebase(DBUrl);
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);
            }
            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
        Thread thread = Thread.currentThread();
        try{
            thread.sleep(100);
        }catch (InterruptedException e){}
        finally {
            return connected;
        }
    }

    public LocalsList queryLocals(String DBUrl){
        final LocalsList locals = new LocalsList();

        Firebase localsRef = new Firebase(DBUrl);
        localsRef.keepSynced(true);

        localsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot localSnapshot : snapshot.getChildren()){
                    Local local = localSnapshot.getValue(Local.class);
                    locals.addLocal(local);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
        Thread thread = Thread.currentThread();
        try{
            thread.sleep(3250);
        }catch (InterruptedException e){
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }finally {
            return locals;
        }
    }

    public OrderList getOrders(String DBUrl, final String localName){
        final OrderList orders = new OrderList();

        Firebase orderRef = new Firebase(DBUrl);
        orderRef.keepSynced(true);

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()){
                    String name = (String) orderSnapshot.child("locale").getValue();
                    if(name.equals(localName)){
                        Order order = new Order();
                        order.setEmail((String) orderSnapshot.child("email").getValue());
                        order.setData((String)orderSnapshot.child("data").getValue());
                        order.setLocale((String)orderSnapshot.child("locale").getValue());
                        order.setNum_items((Long) orderSnapshot.child("items").getValue());
                        order.setStato((String)orderSnapshot.child("stato").getValue());
                        order.setTotale((String) orderSnapshot.child("totale").getValue());
                        order.setCatena((String) orderSnapshot.child("catena").getValue());
                        for(DataSnapshot children : orderSnapshot.getChildren()) {
                            if (children.hasChildren()){
                                OrderItem item = new OrderItem();
                                item.setNome((String) children.child("nome").getValue());
                                item.setPrezzo((String) children.child("prezzo").getValue());
                                item.setQuantita((String) children.child("quantita").getValue());
                                item.setPosition((Long) children.child("posizione").getValue());
                                order.addOrderItem(item);
                            }
                        }
                        orders.addOrder(order);
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        Thread thread = Thread.currentThread();
        try{
            thread.sleep(1000);
        }catch (InterruptedException e){
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }finally {
            return orders;
        }
    }

    public Order getOrder(String DBUrl, final String pk){
        Firebase orderRef = new Firebase(DBUrl);
        final Order order = new Order();

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    if (orderSnapshot.getKey().equals(pk)) {
                        order.setEmail((String) orderSnapshot.child("email").getValue());
                        order.setData((String) orderSnapshot.child("data").getValue());
                        order.setLocale((String) orderSnapshot.child("locale").getValue());
                        order.setNum_items((Long) orderSnapshot.child("items").getValue());
                        order.setStato((String) orderSnapshot.child("stato").getValue());
                        order.setTotale((String) orderSnapshot.child("totale").getValue());
                        order.setCatena((String) orderSnapshot.child("catena").getValue());
                        for (DataSnapshot children : orderSnapshot.getChildren()) {
                            if (children.hasChildren()) {
                                OrderItem item = new OrderItem();
                                item.setNome((String) children.child("nome").getValue());
                                item.setPrezzo((String) children.child("prezzo").getValue());
                                item.setQuantita((String) children.child("quantita").getValue());
                                item.setPosition((Long) children.child("posizione").getValue());
                                order.addOrderItem(item);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Thread thread = Thread.currentThread();
        try {
            thread.sleep(500);
        }catch (InterruptedException e){
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }finally {
            orderRef.child(pk).child("stato").setValue("chiuso");
            return order;
        }
    }

    public void setSeats(String DBUrl, final String localName, final String seats){
        Firebase ref = new Firebase(DBUrl);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot localSnapshot : dataSnapshot.getChildren()) {
                    String nome = (String) localSnapshot.child("nome").getValue();
                    if(nome.equals(localName)){
                        Firebase localRef = localSnapshot.getRef();
                        localRef.child("posti").setValue(seats);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

}
