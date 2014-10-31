package main.java.org.cse.visiri.communications;

/**
 * Created by Geeth on 2014-10-31.
 */
public class Environment {

    EnvironmentChangedCallback changedCallback;

    public Environment(EnvironmentChangedCallback callback)
    {
        changedCallback = callback;
        throw new UnsupportedOperationException();
    }



    String getNodeId()
    {
        throw new UnsupportedOperationException();
    }
}
