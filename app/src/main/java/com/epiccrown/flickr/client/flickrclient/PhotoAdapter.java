package com.epiccrown.flickr.client.flickrclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Epiccrown on 12.04.2018.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{

    private List<GalleryItem> items;
    private Context mContext;


    public PhotoAdapter(Context mContext) {
        items = new ArrayList<>();
        this.mContext = mContext;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.gallery_item,parent,false);
        return new PhotoHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        final GalleryItem item = items.get(position);
        if(item.getmURL()!=null) {
            Glide.with(mContext).load(item.getmURL())
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        }else return;

        if(!item.getmCaption().trim().equals(""))
            holder.caption.setText(item.getmCaption());
        else
            holder.caption.setVisibility(View.GONE);

        holder.setIsRecyclable(false);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri link = item.generateLink();
                Intent intent = WebBrowserActivity.newIntent(mContext,link);
//                Intent intent = new Intent(Intent.ACTION_VIEW,link);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView caption;
        ConstraintLayout layout;
        private PhotoHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.gallery_image);
            caption = itemView.findViewById(R.id.gallery_caption);
            layout = itemView.findViewById(R.id.card_content_holder);
        }
    }

    public void setItems(List<GalleryItem> items) {
        this.items.addAll(items);
    }

    public void clearItems(){
        this.items.clear();
    }
}