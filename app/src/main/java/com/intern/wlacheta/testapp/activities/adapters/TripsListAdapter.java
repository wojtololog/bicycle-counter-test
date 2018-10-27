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
    private final String startTripDate = "Start date: %s";
    private final String endTripDate = "End date: %s";

    class TripsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripIDItemView;
        private final TextView tripStartDateView;
        private final TextView tripEndDateView;

        private TripsViewHolder(View itemView) {
            super(itemView);
            tripIDItemView = itemView.findViewById(R.id.tripID);
            tripStartDateView = itemView.findViewById(R.id.startTripDate);
            tripEndDateView = itemView.findViewById(R.id.endTripDate);
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
            holder.tripIDItemView.setText(String.valueOf(position + 1));
            holder.tripStartDateView.setText(String.format(startTripDate,DateConverter.fromTimeStampToString(currentTrip.getStartTripTimestamp())));
            holder.tripEndDateView.setText(String.format(endTripDate,DateConverter.fromTimeStampToString(currentTrip.getEndTripTimestamp())));
        } else {
            // Covers the case of data not being ready yet.
            holder.tripIDItemView.setText("No ModelTrip");
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
