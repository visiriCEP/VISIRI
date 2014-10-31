import org.cse.visiri.util.Event;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */

public abstract class CEPEngine {

    public final int ENGINE_TYPE_DIRECT=0;
    public final int ENGINE_TYPE_SIDDHI=1;

    public void start(){}
    public  void stop(){}
    public Object saveState(){

        return new UnsupportedOperationException();
    }
    public void sendEvent(Event e){}


}
