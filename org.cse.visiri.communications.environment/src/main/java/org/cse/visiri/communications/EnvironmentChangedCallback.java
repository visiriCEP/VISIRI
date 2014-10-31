package main.java.org.cse.visiri.communications;

/**
 * Created by Geeth on 2014-10-31.
 */
public interface EnvironmentChangedCallback {

    public void queriesChanged();
    public void nodesChanged();
    public void bufferingStateChanged();
    public void eventSubscriberChanged();
}
