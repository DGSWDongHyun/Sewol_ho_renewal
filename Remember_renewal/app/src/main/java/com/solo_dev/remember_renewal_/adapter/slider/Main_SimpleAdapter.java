package com.solo_dev.remember_renewal_.adapter.slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.solo_dev.remember_renewal_.R;
import com.solo_dev.remember_renewal_.view.data.slider.Main_SimpleItems;

import java.util.ArrayList;
import java.util.List;

public class Main_SimpleAdapter extends
        SliderViewAdapter<Main_SimpleAdapter.SliderAdapterVH> {

    private Context context;
    private List<Main_SimpleItems> mSliderItems = new ArrayList<>();

    public Main_SimpleAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<Main_SimpleItems> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Main_SimpleItems sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_simpleslider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        switch (position) {
            case 0:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_1)
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_2)
                        .into(viewHolder.imageViewBackground);
                break;
            case 2:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_3)
                        .into(viewHolder.imageViewBackground);
                break;
            case 3:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_4)
                        .into(viewHolder.imageViewBackground);
                break;
            case 4:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_5)
                        .into(viewHolder.imageViewBackground);
                break;
            case 5:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_6)
                        .into(viewHolder.imageViewBackground);
                break;
            case 6:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_7)
                        .into(viewHolder.imageViewBackground);
                break;
            case 7:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_8)
                        .into(viewHolder.imageViewBackground);
                break;
            default:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.images_9)
                        .into(viewHolder.imageViewBackground);
                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 9;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}