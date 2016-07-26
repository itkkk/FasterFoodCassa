package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.db;

import android.app.Application;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.Local;
import fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata.LocalsList;

public class DbController extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //inizializzazione Firebase
        Firebase.setAndroidContext(getApplicationContext());
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public DbController(){}
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
            thread.sleep(1000);
        }catch (InterruptedException e){
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }finally {
            return locals;
        }
    }
}
