package org.cse.visiri.core;

/**
 * Created by lasitha on 2/3/15.
 */
public interface GUICallback {
    public void queriesChanged();
    public void newEnginesRecieved(String from);
    public void bufferingStart();
}
