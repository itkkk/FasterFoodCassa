package fasterfoodcassa.gruppo3.sms16.ivu.di.uniba.it.fasterfoodcassa.dbdata;

import java.util.ArrayList;
import java.util.List;

//lista di locali
public class LocalsList{
    private ArrayList<Local> locals = new ArrayList<>();

    public void addLocal(Local local){
        locals.add(local);
    }

    public ArrayList<Local> getLocals(){
        return locals;
    }
}
