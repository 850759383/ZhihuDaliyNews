package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.utils.ImageLoader;
import com.yininghuang.zhihudailynews.widget.PosterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuLatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ZhihuLatestNews> mLatestNewsList = new ArrayList<>();
    private List<ZhihuLatestNews.ZhihuStory> mZhihuStoryList = new ArrayList<>();

    private int TYPE_LOADING = -1;
    private int TYPE_POSTER = 0;
    private int TYPE_ITEM = 1;

    private OnItemClickListener mOnItemClickListener;

    public ZhihuLatestAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POSTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster_layout, parent, false);
            return new PosterHolder(view);
        } else if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_status, parent, false);
            return new LoadingHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_lastest, parent, false);
            return new NewsHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PosterHolder) {
            PosterView posterView = ((PosterHolder) holder).getPosterView();
            if (mLatestNewsList.size() > 0) {
                posterView.initPosters(mLatestNewsList.get(0).getTopStories());
                posterView.setOnPosterClickListener(new PosterView.OnPosterClickListener() {
                    @Override
                    public void onPosterClick(ZhihuLatestNews.ZhihuTopStory story) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onPosterClick(story);
                    }
                });
            }
        } else if (holder instanceof NewsHolder) {
            ZhihuLatestNews.ZhihuStory itemData = mZhihuStoryList.get(position - 1);
            ((NewsHolder) holder).getTitle().setText(itemData.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onNewsClick(mZhihuStoryList.get(holder.getAdapterPosition() - 1));
                }
            });

            if (itemData.getImages().size() == 0)
                return;
            ImageLoader.load(mContext, ((NewsHolder) holder).getImageView(), itemData.getImages().get(0));
        }
    }

    public void addNews(ZhihuLatestNews news) {
        mLatestNewsList.add(news);
        mZhihuStoryList.addAll(news.getStories());
    }

    public List<ZhihuLatestNews> getLatestNewsList() {
        return mLatestNewsList;
    }

    public List<ZhihuLatestNews.ZhihuStory> getZhihuStoryList() {
        return mZhihuStoryList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mZhihuStoryList.size() == 0)
            return mZhihuStoryList.size() + 1;
        return mZhihuStoryList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_POSTER;
        else if (position == mZhihuStoryList.size() + 1)
            return TYPE_LOADING;
        else
            return TYPE_ITEM;
    }

    public interface OnItemClickListener {

        void onPosterClick(ZhihuLatestNews.ZhihuTopStory topStory);

        void onNewsClick(ZhihuLatestNews.ZhihuStory story);
    }

    public static class PosterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.posterView)
        PosterView posterView;

        public PosterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public PosterView getPosterView() {
            return posterView;
        }
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.pic)
        ImageView imageView;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
