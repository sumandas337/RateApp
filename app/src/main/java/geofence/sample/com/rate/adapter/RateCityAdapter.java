package geofence.sample.com.rate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import java.util.ArrayList;

import geofence.sample.com.R;
import geofence.sample.com.model.City;
import geofence.sample.com.rate.IRateContract;
import geofence.sample.com.viewutils.FontTextView;

/**
 * Created by sumandas on 05/12/2016.
 */

public class RateCityAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> implements IRateContract.IRateAdapterView {

    private ArrayList<City> mCities=new ArrayList<>();

    private Context mContext;
    private CityRatedListener mListener;

    public RateCityAdapter(Context context,ArrayList<City> cities,CityRatedListener cityRatedListener){
        mContext=context;
        mCities=cities;
        mListener=cityRatedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate_card,
                parent, false);
        return new CityHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        City city=mCities.get(position);
        ((CityHolder)holder).city=city;
        if(city.isLocationWithinCity){
            ((CityHolder)holder).itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ((CityHolder)holder).mRateBar.setEnabled(true);
        }else{
            ((CityHolder)holder).itemView.setBackgroundColor(mContext.getResources().getColor(R.color.quaternary_grey));
            ((CityHolder)holder).mRateBar.setEnabled(false);
        }
        ((CityHolder)holder).mCityName.setText(city.mCityName);
        ((CityHolder)holder).mCityDescription.setText(city.mCityDesc);

        ((CityHolder)holder).mRateBar.setRating(city.mRating);

    }
    @Override
    public int getItemCount() {
        return mCities.size();
    }

    @Override
    public void onCityChanged(City city) {
        if(city==null){
            for(City city1:mCities){
                if(city1.isLocationWithinCity){
                    city1.isLocationWithinCity=false;
                    break;
                }
            }
        }else{
            for(City city1:mCities){
                if(city1.mCityName.equals(city.mCityName)){
                    city1.isLocationWithinCity=true;
                }else{
                    city1.isLocationWithinCity=false;
                }
            }
        }
        notifyDataSetChanged();
    }


    private class CityHolder extends RecyclerView.ViewHolder {

        City city;
        RatingBar mRateBar;
        FontTextView mCityName;
        FontTextView mCityDescription;

        CityHolder(View itemView, CityRatedListener listener) {
            super(itemView);
            mRateBar = (RatingBar) itemView.findViewById(R.id.city_rating_bar);
            mCityName = (FontTextView) itemView.findViewById(R.id.city_name);
            mCityDescription=(FontTextView)itemView.findViewById(R.id.city_description);
            mRateBar.setOnRatingBarChangeListener((ratingBar, v, b) -> listener.onCityRated(city, v));
        ;
        }
    }

    public interface CityRatedListener{
        void onCityRated(City city,float rating);
    }

}
