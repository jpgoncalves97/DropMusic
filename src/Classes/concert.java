package Classes;

import java.io.Serializable;
import java.sql.Timestamp;

public class concert implements Serializable {
    private Timestamp data;
    private String local;
    public concert(Timestamp data, String local){
        this.data = data;
        this.local= local;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getLocal() {
        return local;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
