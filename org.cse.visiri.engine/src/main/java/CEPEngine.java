import org.cse.visiri.util.Event;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */

public abstract class CEPEngine {
    private int ENGINE_TYPE;
    public void start(){}
    public  void stop(){}
    public Object saveState(){
        return new UnsupportedOperationException();
    }
    public void sendEvent(Event e){}

    public int getENGINE_TYPE() {
        return ENGINE_TYPE;
    }

    public void setENGINE_TYPE(int ENGINE_TYPE) {
        this.ENGINE_TYPE = ENGINE_TYPE;
    }
}
