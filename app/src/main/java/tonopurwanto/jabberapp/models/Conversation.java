package tonopurwanto.jabberapp.models;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmObject;

public class Conversation extends RealmObject implements Comparable<Conversation>
{
    private Long id;
    private String jid;
    private String message;
    private Date created_at;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getJid()
    {
        return jid;
    }

    public void setJid(String jid)
    {
        this.jid = jid;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Date getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(Date created_at)
    {
        this.created_at = created_at;
    }

    @Override public int compareTo(@NonNull Conversation chat)
    {
        return this.created_at.compareTo(chat.getCreated_at());
    }
}
