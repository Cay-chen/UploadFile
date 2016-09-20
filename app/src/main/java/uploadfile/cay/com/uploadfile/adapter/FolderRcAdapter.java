package uploadfile.cay.com.uploadfile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import uploadfile.cay.com.uploadfile.R;

/**
 * Created by C on 2016/9/20.
 */
public class FolderRcAdapter extends RecyclerView.Adapter<FolderRcAdapter.ViewHolder> {
    private int[] folders;
    private List<String>  times;
    private Context context;
    private List<String> names;
    private int num;

    public FolderRcAdapter(int[] folders, List<String>  times, Context context, List<String> names, int num) {
        this.folders = folders;
        this.times = times;
        this.context = context;
        this.names = names;
        this.num = num;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < num) {
            holder.nameTextView.setText(names.get(position));
            holder.mImageView.setImageResource(R.mipmap.icon_list_folder);
            holder.timeTextView.setText(times.get(position));
        } else {
            holder.nameTextView.setText(names.get(position));
            Glide.with(context).load("http://118.192.157.178:8080/XiaoWei/servlet/DownloadFile?filename=chenwei\\" + names.get(position)).into(holder.mImageView);
            holder.timeTextView.setText(times.get(position));
        }
      //  holder.timeTextView.setText(times[position]);
       // holder.mImageView.setImageResource(folders[position]);

    }

    @Override
    public int getItemCount() {
        return names.size();
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
