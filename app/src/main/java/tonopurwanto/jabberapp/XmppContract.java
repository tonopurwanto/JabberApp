package tonopurwanto.jabberapp;

import tonopurwanto.jabberapp.events.OutgoingMessageEvent;

public interface XmppContract
{
    void configure();
    void connect();
    void onOutgoingMessage(OutgoingMessageEvent event);
}
