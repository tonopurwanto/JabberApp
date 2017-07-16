package tonopurwanto.jabberapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Date;

import javax.inject.Inject;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import tonopurwanto.jabberapp.events.ConnectionSuccessfulEvent;
import tonopurwanto.jabberapp.events.IncomingMessageEvent;
import tonopurwanto.jabberapp.events.OutgoingMessageEvent;
import tonopurwanto.jabberapp.models.Conversation;

public class XmppService extends Service implements XmppContract, IncomingChatMessageListener
{
    private static final String TAG = XmppService.class.getSimpleName();
    private static final int START_MODE = START_STICKY;

    private IBinder mBinder;
    private boolean mAllowRebind = true;
    private AbstractXMPPConnection mConnection;
    private XMPPTCPConnectionConfiguration mConfig;
    private ChatManager mChatManager;
    private Realm mRealm;

    @Inject public Context mContext;
    @Inject public EventBus mEventBus;
    @Inject public X509TrustManager mX509TrustManager;

    @Nullable @Override public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override public void onCreate()
    {
        ((JabberApp) getApplication()).getComponent().inject(this);

        mEventBus.register(this);

        configure();
        connect();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_MODE;
    }

    @Override public void onDestroy()
    {
        mEventBus.unregister(this);

        super.onDestroy();
    }

    @Override public boolean onUnbind(Intent intent)
    {
        return mAllowRebind;
    }

    @Override public void onRebind(Intent intent)
    {
        super.onRebind(intent);
    }

    @Override public void configure()
    {
        try {
            Realm.init(mContext);
            mRealm = Realm.getDefaultInstance();

            mConfig = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("klakklik01", "makanan")
                    .setHost("jabb.im")
                    .setXmppDomain("jabb.im")
                    .setPort(5222)
                    .setKeystoreType(null)
                    .setCompressionEnabled(true)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                    .setConnectTimeout(30000)
                    .setCustomX509TrustManager(mX509TrustManager)
                    .setDebuggerEnabled(true)
                    .build();

            mConnection = new XMPPTCPConnection(mConfig);
            mChatManager = ChatManager.getInstanceFor(mConnection);
            mChatManager.addIncomingListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }
    }

    @Override public void connect()
    {
        new AsyncTask<Void, Void, Boolean>() {

            boolean isLoggedIn = false;
            Exception e = null;

            @Override protected Boolean doInBackground(Void... params)
            {
                try {
                    mConnection.connect();
                    if (! mConnection.isConnected()) {
                        Log.i(TAG, "XMPP connection failed");
                        return false;
                    }

                    mConnection.login();
                    if (mConnection.isAuthenticated()) isLoggedIn = true;
                } catch (Exception e) {
                    this.e = e;
                }

                return isLoggedIn;
            }

            @Override protected void onPostExecute(Boolean result)
            {
                if (e == null) {
                    if (result) {
                        Log.i(TAG, "XMPP authenticated");
                        mEventBus.post(new ConnectionSuccessfulEvent());
                    } else {
                        Log.i(TAG, "XMPP connection failed");
                        stopSelf();
                    }
                } else {
//                    Log.e(TAG, e.getCause().toString());
                    stopSelf();
                }
            }
        }.execute();
    }

    @Subscribe
    @Override public void onOutgoingMessage(OutgoingMessageEvent event)
    {
        try {
            EntityBareJid jid = JidCreate.entityBareFrom(event.getJid());
            Chat chat = mChatManager.chatWith(jid);
            chat.send(event.getMessage());
        } catch (XmppStringprepException | InterruptedException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override public void newIncomingMessage(EntityBareJid from, Message message, Chat chat)
    {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        Conversation conversation = mRealm.createObject(Conversation.class);
        conversation.setJid(from.toString());
        conversation.setMessage(message.getBody());
        conversation.setCreated_at(new Date());
        mRealm.commitTransaction();

        mEventBus.post(new IncomingMessageEvent(from, message, chat));
    }
}
