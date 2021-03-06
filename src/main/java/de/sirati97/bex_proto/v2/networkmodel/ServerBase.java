package de.sirati97.bex_proto.v2.networkmodel;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.EventDelegate;
import de.sirati97.bex_proto.events.EventPriority;
import de.sirati97.bex_proto.events.EventRegister;
import de.sirati97.bex_proto.events.Listener;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.v2.IServiceFactory;
import de.sirati97.bex_proto.v2.events.ConnectionClosedEvent;
import de.sirati97.bex_proto.v2.events.NewConnectionEvent;
import de.sirati97.bex_proto.v2.service.basic.BasicService;

import java.io.IOException;

import static de.sirati97.bex_proto.v2.networkmodel.CommonArchitectureFunction.Server;

/**
 * Created by sirati97 on 17.04.2016.
 */
public abstract class ServerBase<Connection extends BasicService> extends ConnectionBase<Connection> implements IServer<Connection> {
    private ILogger logger;
    private EventRegister eventRegister;
    private EventDelegate<ConnectionClosedEvent<Connection>> connectionClosedHandler;

    public ServerBase(IServiceFactory<Connection> factory) {
        super(factory);
        connectionClosedHandler = new EventDelegate<ConnectionClosedEvent<Connection>>(ConnectionClosedEvent.class) {
            @Override
            public void onEvent(ConnectionClosedEvent<Connection> event) {
                unregisterConnection(event.getConnection());
            }
        }.generics(factory.getConnectionClass()).priority(EventPriority.Monitor);
    }

    /**wraps isListening*/
    @Override
    public final boolean isConnected() {
        return isListening();
    }

    /**wraps startListening*/
    @Override
    public final void connect() throws IOException {
        startListening();
    }


    /**wraps stopListening*/
    @Override
    public final void disconnect() {
        stopListening();
    }

    @Override
    protected final ILogger getLogger() {
        return logger==null?(logger=createLogger()):logger;
    }
    protected abstract ILogger createLogger();

    @Override
    protected void registerConnection(Connection connection) {
        super.registerConnection(connection);
        connection.getEventRegister().addParent(getEventRegister());
        connection.getEventRegister().registerEventDelegateListener(connectionClosedHandler);
        connection.invokeEvent(new NewConnectionEvent<>(connection, getFactory().getConnectionClass()));
    }

    @Override
    protected void unregisterConnection(Connection connection) {
        super.unregisterConnection(connection);
        connection.getEventRegister().removeParent(getEventRegister());
    }

    @Override
    public boolean registerEventListener(Listener listener) {
        return getEventRegister().registerEventListener(listener);
    }

    @Override
    public boolean unregisterEventListener(Listener listener) {
        return getEventRegister().unregisterEventListener(listener);
    }

    @Override
    public void invokeEvent(Event event) {
        getEventRegister().invokeEvent(event);
    }

    @Override
    public EventRegister getEventRegister() {
        if (eventRegister == null) {
            eventRegister = new EventRegister(getLogger());
        }
        return eventRegister;
    }

    @Override
    public EventRegister getEventRegisterImplementation() {
        return getEventRegister();
    }

    @Override
    public IArchitectureFunction getArchitectureFunction() {
        return Server;
    }
}
