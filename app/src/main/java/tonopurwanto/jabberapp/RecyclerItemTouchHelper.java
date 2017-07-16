package tonopurwanto.jabberapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback
{
    private ConversationAdapter mAdapter;

    public RecyclerItemTouchHelper(ConversationAdapter adapter)
    {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        this.mAdapter = adapter;
    }

    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
//        mAdapter.onMove();

        return true;
    }

    @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {

    }
}
