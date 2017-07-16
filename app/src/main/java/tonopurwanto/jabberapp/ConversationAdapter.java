package tonopurwanto.jabberapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tonopurwanto.jabberapp.models.Conversation;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>
{
    private List<Conversation> mDataset;

    public ConversationAdapter(List<Conversation> data)
    {
        setHasStableIds(true);

        mDataset = new ArrayList<>();
        refreshData(data);
    }

    public void refreshData(List<Conversation> data)
    {
        mDataset.clear();
        mDataset.addAll(data);

        Collections.sort(this.mDataset, Collections.<Conversation>reverseOrder());

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.item_message);
        }

        public void bindTo(Conversation chat)
        {
            textView.setText(chat.getMessage());
        }
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item_row, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bindTo(this.mDataset.get(position));
    }

    @Override public int getItemCount()
    {
        return mDataset.size();
    }

    @Override public long getItemId(int position)
    {
        return mDataset.get(position).getId();
    }
}
