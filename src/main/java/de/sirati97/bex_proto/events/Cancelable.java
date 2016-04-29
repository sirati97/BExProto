package de.sirati97.bex_proto.events;

/**
 * Created by sirati97 on 29.04.2016.
 */
public interface Cancelable extends Event {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
