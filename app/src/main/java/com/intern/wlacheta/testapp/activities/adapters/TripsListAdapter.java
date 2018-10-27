package com.intern.wlacheta.testapp.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;

import java.util.List;

public class TripsListAdapter extends RecyclerView.Adapter<TripsListAdapter.TripsViewHolder>  {
    private String tripItemInfo = "ID: %d  StartDate: %s";

    class TripsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripItemView;

        private TripsViewHolder(View itemView) {
            super(itemView);
            tripItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Trip> trips; // Cached copy of words

    public TripsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TripsListAdapter.TripsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripsViewHolder holder, int position) {
        if (trips != null) {
            Trip currentTrip = trips.get(position);
            holder.tripItemView.setText(String.format(tripItemInfo,currentTrip.getId(),DateConverter.fromTimeStampToString(currentTrip.getStartTripTimestamp())));
        } else {
            // Covers the case of data not being ready yet.
            holder.tripItemView.setText("No ModelTrip");
        }
    }

    public void setAllTrips(List<Trip> trips){
        this.trips = trips;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.trips != null)
            return this.trips.size();
        else return 0;
    }
}
