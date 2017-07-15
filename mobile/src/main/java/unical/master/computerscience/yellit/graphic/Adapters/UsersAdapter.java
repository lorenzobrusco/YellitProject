package unical.master.computerscience.yellit.graphic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Lorenzo on 14/07/2017.
 */

public class UsersAdapter extends BaseAdapter implements Filterable {

    private List<User> mUsers;
    private List<String> mUserNames;
    ValueFilter valueFilter;
    private LayoutInflater inflater;
    private Context mContext;
    @Bind(R.id.image_user_search_bar)
    RoundedImageView mUserImage;
    @Bind(R.id.email_search_bar)
    TextView mEmailSearchBar;

    public UsersAdapter(final Context mContext, final List<User> users) {
        this.mContext = mContext;
        this.mUsers = users;
        this.mUserNames = new ArrayList<>();
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(User user : mUsers){
            mUserNames.add(user.getEmail());
        }

    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public User getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final View view = inflater.inflate(R.layout.item_user_search_bar, null);
        ButterKnife.bind(this, view);
        Glide.with(mContext)
                .load(mUsers.get(position).getPathImg())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(mContext.getResources().getDrawable(R.drawable.default_user))
                .into(mUserImage);
        mEmailSearchBar.setText(mUsers.get(position).getEmail());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<User> filterList = new ArrayList();
                for (int i = 0; i < mUserNames.size(); i++) {
                    if ((mUserNames.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mUsers.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mUserNames.size();
                results.values = mUserNames;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mUsers = (List) results.values;
            notifyDataSetChanged();
        }

    }

}