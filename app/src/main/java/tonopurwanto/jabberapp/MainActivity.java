package tonopurwanto.jabberapp;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import tonopurwanto.jabberapp.events.ConnectionSuccessfulEvent;
import tonopurwanto.jabberapp.events.IncomingMessageEvent;
import tonopurwanto.jabberapp.events.OutgoingMessageEvent;
import tonopurwanto.jabberapp.models.Conversation;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mListView;
    private Realm mRealm;
    private List<Conversation> mData;
    private ConversationAdapter mAdapter;

    @Inject public Context mContext;
    @Inject public EventBus mEventBus;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((JabberApp) getApplication()).getComponent().inject(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mListView = (RecyclerView) findViewById(R.id.listview);

        Realm.init(mContext);
        mRealm = Realm.getDefaultInstance();

        mData = new ArrayList<>();
        mAdapter = new ConversationAdapter(mData);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setHasFixedSize(true);
        mListView.setAdapter(mAdapter);

        startService(new Intent(this, XmppService.class));
    }

    private void sendMessage()
    {
        mEventBus.post(new OutgoingMessageEvent("neosolusi@jabb.im", "Aje Gileeee"));
    }

    @Subscribe public void onConnectionSuccessfulEvent(ConnectionSuccessfulEvent event)
    {
        sendMessage();
    }

    @Subscribe public void onNewMessageEvent(final IncomingMessageEvent event)
    {
        runOnUiThread(new Runnable()
        {
            @Override public void run()
            {
                refresh(event);
            }
        });
    }

    private void refresh(IncomingMessageEvent event)
    {
        Random id = new Random();
        Conversation chat = new Conversation();
        chat.setId(id.nextLong());
        chat.setJid(event.getFrom().toString());
        chat.setMessage(event.getMessage().getBody());
        chat.setCreated_at(new Date());

        mData.add(chat);
        mAdapter.refreshData(mData);
    }

    @Override protected void onResume()
    {
        super.onResume();

        mEventBus.register(this);
    }

    @Override protected void onPause()
    {
        mEventBus.unregister(this);

        super.onPause();
    }
}
