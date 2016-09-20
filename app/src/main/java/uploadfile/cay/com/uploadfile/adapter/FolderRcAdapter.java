package uploadfile.cay.com.uploadfile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uploadfile.cay.com.uploadfile.R;

/**
 * Created by C on 2016/9/20.
 */
public class FolderRcAdapter extends RecyclerView.Adapter<FolderRcAdapter.ViewHolder> {
    private int[] folders;
    private String[] times;
    private Context context;
    private String[] names;

    public FolderRcAdapter(int[] folders, String[] times, Context context, String[] names) {
        this.folders = folders;
        this.times = times;
        this.context = context;
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.timeTextView.setText(times[position]);
        holder.nameTextView.setText(names[position]);
        holder.mImageView.setImageResource(folders[position]);

    }

    @Override
    public int getItemCount() {
        return folders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView nameTextView;
        TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.folder_image);
            nameTextView = (TextView) itemView.findViewById(R.id.folder_name);
            timeTextView = (TextView) itemView.findViewById(R.id.folder_time);
        }
    }
}
