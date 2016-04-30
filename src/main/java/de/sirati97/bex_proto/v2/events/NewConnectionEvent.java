package de.sirati97.bex_proto.v2.events;

import de.sirati97.bex_proto.events.EventDistributor;
import de.sirati97.bex_proto.events.EventDistributorImpl;
import de.sirati97.bex_proto.events.GenericEvent;
import de.sirati97.bex_proto.v2.artifcon.ArtifConnection;

/**
 * Created by sirati97 on 29.04.2016.
 */
public class NewConnectionEvent<Connection extends ArtifConnection> implements GenericEvent {
    private static final EventDistributor DISTRIBUTOR = new EventDistributorImpl();
    private static final Class[] GENERICS_SUPERCLASSES = {ArtifConnection.class};
    public static EventDistributor getEventDistributor() {
        return DISTRIBUTOR;
    }
    public static Class[] getGenericsSuperclasses() {
        return GENERICS_SUPERCLASSES;
    }

    public NewConnectionEvent(Connection connection, Class<Connection> connectionClass) {
        this.connection = connection;
        this.generics = new Class[]{connectionClass};
    }


    private final Connection connection;
    private final Class[] generics;

    public Connection getConnection() {
        return connection;
    }

    @Override
    public Class[] getGenerics() {
        return generics;
    }
}