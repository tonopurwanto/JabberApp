package tonopurwanto.jabberapp.events;

public class OutgoingMessageEvent
{
    private String jid;
    private String message;

    public OutgoingMessageEvent(String jid, String message)
    {
        this.jid = jid;
        this.message = message;
    }

    public String getJid()
    {
        return jid;
    }

    public String getMessage()
    {
        return message;
    }

    public void setJid(String jid)
    {
        this.jid = jid;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
