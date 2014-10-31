import org.cse.visiri.util.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Malinda Kumarasinghe on 10/31/2014.
 */
public class EngineHandler {
    private Map<Query,CEPEngine> queryEngineMap;
    private Map<String,List<CEPEngine>> eventEngineMap;
   // private OutputEventReciever sink;

    public void addQuery(Query q){}
    public void removeQuery(int queryID){}

    public Map<Query, CEPEngine> getQueryEngineMap() {
        return queryEngineMap;
    }

    public void setQueryEngineMap(Map<Query, CEPEngine> queryEngineMap) {
        this.queryEngineMap = queryEngineMap;
    }

    public Map<String, List<CEPEngine>> getEventEngineMap() {
        return eventEngineMap;
    }

    public void setEventEngineMap(Map<String, List<CEPEngine>> eventEngineMap) {
        this.eventEngineMap = eventEngineMap;
    }

   // public OutputEventReciever getSink() {
    //    return sink;
    //}

//    public void setSink(OutputEventReciever sink) {
//        this.sink = sink;
//    }
}
