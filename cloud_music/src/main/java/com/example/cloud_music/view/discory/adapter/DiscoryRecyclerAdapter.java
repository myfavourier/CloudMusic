package com.example.cloud_music.view.discory.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cloud_music.R;
import com.example.cloud_music.model.discory.RecommandBodyValue;
import com.example.lib_commin_ui.recyclerview.MultiItemTypeAdapter;
import com.example.lib_commin_ui.recyclerview.base.ItemViewDelegate;
import com.example.lib_commin_ui.recyclerview.base.ViewHolder;
import com.example.lib_image_loader.app.ImageLoaderManager;

import java.util.List;

/**
 * 发现页面数据adpater
 */
public class DiscoryRecyclerAdapter extends MultiItemTypeAdapter {

    public static final int PICTURE_TYPE = 0x01; //图片类型

    public DiscoryRecyclerAdapter(Context context, List<RecommandBodyValue> datas) {
        super(context, datas);
        addItemViewDelegate(PICTURE_TYPE, new PictureItemDelegate());
    }

    /**
     * 内部类图片类型item
     */
    private class PictureItemDelegate implements ItemViewDelegate<RecommandBodyValue> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_discory_list_picture_layout;
        }

        @Override
        public boolean isForViewType(RecommandBodyValue item, int position) {
            return item.type == DiscoryRecyclerAdapter.PICTURE_TYPE;
        }

        @Override
        public void convert(ViewHolder holder, RecommandBodyValue recommandBodyValue, int position) {
            TextView titleView = holder.getView(R.id.title_view);
            if (TextUtils.isEmpty(recommandBodyValue.title)) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(recommandBodyValue.title);
            }
            holder.setText(R.id.name_view, recommandBodyValue.text);
            holder.setText(R.id.play_view, recommandBodyValue.play);
            holder.setText(R.id.time_view, recommandBodyValue.time);
            holder.setText(R.id.zan_view, recommandBodyValue.zan);
            holder.setText(R.id.message_view, recommandBodyValue.msg);
            ImageView logo = holder.getView(R.id.logo_view);
            ImageLoaderManager.getInstance().displayImageForView(logo, recommandBodyValue.logo);
            ImageView avatar = holder.getView(R.id.author_view);
            ImageLoaderManager.getInstance().displayImageForCircle(avatar, recommandBodyValue.avatr);
        }
    }
}

