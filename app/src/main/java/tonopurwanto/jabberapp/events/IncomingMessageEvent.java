package tonopurwanto.jabberapp.events;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class IncomingMessageEvent
{
    private EntityBareJid from;
    private Message message;
    private Chat chat;

    public IncomingMessageEvent(EntityBareJid from, Message message, Chat chat)
    {
        this.from = from;
        this.message = message;
        this.chat = chat;
    }

    public EntityBareJid getFrom()
    {
        return from;
    }

    public Message getMessage()
    {
        return message;
    }
}
