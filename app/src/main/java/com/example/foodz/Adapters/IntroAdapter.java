package com.example.foodz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.foodz.R;
import com.example.foodz.model.IntroItems;

import java.util.List;

public class IntroAdapter extends PagerAdapter {

    Context mcontext;
    List<IntroItems> itemsList;

    public IntroAdapter(Context mcontext, List<IntroItems> itemsList) {
        this.mcontext = mcontext;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       LayoutInflater inflater= (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View layoutScreen=inflater.inflate(R.layout.layout_screen, null);
        ImageView imageslide=layoutScreen.findViewById(R.id.intro_image);
        ImageView logoslide=layoutScreen.findViewById(R.id.logo_inside);
        TextView title=layoutScreen.findViewById(R.id.intro_title);
        TextView description=layoutScreen.findViewById(R.id.intro_description);
        title.setText(itemsList.get(position).getTitle());
        logoslide.setImageResource(itemsList.get(position).getLogo());
        description.setText(itemsList.get(position).getDescription());
        imageslide.setImageResource(itemsList.get(position).getScreenImg());
        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View) object);
    }
}
