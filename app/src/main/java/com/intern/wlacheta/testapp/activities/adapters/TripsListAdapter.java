package com.intern.wlacheta.testapp.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.intern.wlacheta.testapp.R;
import com.intern.wlacheta.testapp.database.entities.Trip;
import com.intern.wlacheta.testapp.database.utils.DateConverter;

import java.util.List;

public class TripsListAdapter extends RecyclerView.Adapter<TripsListAdapter.TripsViewHolder>  {
    private final String startTripDate = "Start date: %s";
    private final String endTripDate = "End date: %s";

    private final LayoutInflater mInflater;
    private List<Trip> trips; // Cached copy of words

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onDeleteIconClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class TripsViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripStartDateView;
        private final TextView tripEndDateView;
        private final ImageView deleteIcon;

        private TripsViewHolder(View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            tripStartDateView = itemView.findViewById(R.id.startTripDate);
            tripEndDateView = itemView.findViewById(R.id.endTripDate);
            deleteIcon = itemView.findViewById(R.id.delete_icon);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onDeleteIconClick(position);
                        }
                    }
                }
            });
        }
    }

    public TripsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TripsListAdapter.TripsViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(TripsViewHolder holder, int position) {
        if (trips != null) {
            Trip currentTrip = trips.get(position);
            holder.tripStartDateView.setText(currentTrip.getStartTripDate());
            //holder.tripStartDateView.setText(String.format(startTripDate,DateConverter.fromTimeStampToString(currentTrip.getStartTripTimestamp())));
            holder.tripEndDateView.setText(String.format(endTripDate,DateConverter.fromTimeStampToString(currentTrip.getEndTripTimestamp())));
        } else {
            // Covers the case of data not being ready yet.
            holder.tripStartDateView.setText("No TripModel");
        }
    }

    public void setAllTrips(List<Trip> trips){
        this.trips = trips;
        notifyDataSetChanged();
    }

    public Trip getTripWithPosition(int position) {
        return trips.get(position);
    }

    @Override
    public int getItemCount() {
        if (this.trips != null)
            return this.trips.size();
        else return 0;
    }
}
