package retrofit.json.example.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import retrofit.json.example.R;
import retrofit.json.example.models.GitHubResponseItem;

public class GitHubItemsAdapter extends RecyclerView.Adapter<GitHubItemsAdapter.ViewHolder> {
        private static final String LOG_TAG = GitHubItemsAdapter.class.getSimpleName();
    private static final String ADAPTER_STATE_KEY = "adapter_state_key";

    private List<GitHubResponseItem> mItems;
        private Context mContext;
        private PostItemListener mItemListener;

        public GitHubItemsAdapter(Context context, List<GitHubResponseItem> posts, PostItemListener itemListener) {
            mItems = posts;
            mContext = context;
            mItemListener = itemListener;
        }

        @Override
        public GitHubItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View postView = inflater.inflate(R.layout.card, parent, false);

            ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(GitHubItemsAdapter.ViewHolder holder, int position) {
            GitHubResponseItem item = mItems.get(position);
            TextView firstLine = holder.nameText;
            firstLine.setText(item.getName());
            TextView secondLine = holder.descText;
            secondLine.setText(item.getDescription());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void update(List<GitHubResponseItem> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        private GitHubResponseItem getItem(int adapterPosition) {
            return mItems.get(adapterPosition);
        }

    public void putAdapterState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArray(ADAPTER_STATE_KEY, mItems.toArray(new GitHubResponseItem[mItems.size()]));
    }

    public void updateAdapterState(Bundle savedInstanceState) {
        GitHubResponseItem[] state  = (GitHubResponseItem[])savedInstanceState.getParcelableArray(ADAPTER_STATE_KEY);
        mItems = Arrays.asList(state);
    }

    public interface PostItemListener {
            void onPostClick(String url);
        }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameText;
        public TextView descText;
        CardView itemCard;
        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            itemCard = (CardView) itemView.findViewById(R.id.git_card);
            nameText = (TextView) itemView.findViewById(R.id.item_name);
            descText = (TextView) itemView.findViewById(R.id.item_description);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            GitHubResponseItem item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getHtmlUrl());

            notifyDataSetChanged();
        }
    }

}
