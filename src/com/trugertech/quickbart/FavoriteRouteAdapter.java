package com.trugertech.quickbart;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FavoriteRouteAdapter extends ArrayAdapter<FavoriteRoute> {
	
	private ArrayList<FavoriteRoute> routes;
	private Context mContext;
	private Long favoriteId;
	
	public FavoriteRouteAdapter(Context context, int textViewResourceID, 
			ArrayList<FavoriteRoute> routes, Long favoriteId){
		super(context, textViewResourceID, routes);
		this.routes = routes;
		this.mContext = context;
		this.favoriteId = favoriteId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			LayoutInflater vi = LayoutInflater.from(this.mContext);
			v = vi.inflate(R.layout.favorite_schedule_item, null);
		}
		FavoriteRoute fav = routes.get(position);
		if(fav != null){
			TextView tvDepart = (TextView) v.findViewById(R.id.favoriteInfoDepart);
			TextView tvDest = (TextView) v.findViewById(R.id.favoriteInfoDest);
			TextView tvFare = (TextView) v.findViewById(R.id.favoriteFare);
			
			tvDepart.setText(fav.getOriginTime());
			tvDest.setText(fav.getDestinationTime());
			tvFare.setText(fav.getFare());
		}
		return v;
	}
	
	/**
	 * Gets the ID of the favorite route.
	 * @return
	 */
	public Long getFavoriteId(){
		return this.favoriteId;
	}
	

}
