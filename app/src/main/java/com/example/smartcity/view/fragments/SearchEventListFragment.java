package com.example.smartcity.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity.R;
import com.example.smartcity.data.model.Event;
import com.example.smartcity.utils.OnItemSelectedListener;
import com.example.smartcity.viewmodel.SearchEventViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchEventListFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private SearchEventViewModel searchEventViewModel;
    private RecyclerView eventRecyclerView;
    private TabLayout tabLayout;
    private ConstraintLayout mapLayout;
    private GoogleMap googleMap;

    private OnMapReadyCallback callback = googleMap -> {
        SearchEventListFragment.this.googleMap = googleMap;

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.4669, 4.86746), 10));
        googleMap.setMinZoomPreference(10);
        googleMap.setOnInfoWindowClickListener(SearchEventListFragment.this);
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_event, container, false);

        eventRecyclerView = root.findViewById(R.id.event_list);
        mapLayout = root.findViewById(R.id.map_layout);

        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("tabPosition", 0);

        searchEventViewModel.loadEvents();

        EventAdapter adapter = new EventAdapter();

        searchEventViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            new AsyncEventLocationGetter(requireActivity()).execute(events.toArray(new Event[] {}));
            adapter.setEvents(events);
        });

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        eventRecyclerView.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tab_layout);
        Objects.requireNonNull(tabLayout.getTabAt(position)).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentPosition = tab.getPosition();

                if (currentPosition == 0) { // list
                    eventRecyclerView.setVisibility(View.VISIBLE);
                    mapLayout.setVisibility(View.GONE);
                } else { // map
                    eventRecyclerView.setVisibility(View.GONE);
                    mapLayout.setVisibility(View.VISIBLE);
                }

                eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                eventRecyclerView.setAdapter(adapter);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //voluntary ignored
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //voluntary ignored
            }
        });

        searchEventViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null){
                Toast.makeText(requireContext(), R.string.error_appear, Toast.LENGTH_LONG).show();
            }
        });

        searchEventViewModel.getStatusCode().observe(getViewLifecycleOwner(), statusCode -> {
            if(statusCode == 418){
                Toast.makeText(requireContext(), R.string.event_full, Toast.LENGTH_LONG).show();
            }
            else if(statusCode == 409)
                Toast.makeText(requireContext(), R.string.event_already_in, Toast.LENGTH_LONG).show();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchEventViewModel = new ViewModelProvider(requireActivity()).get(SearchEventViewModel.class);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Event clickedEvent = searchEventViewModel.getEvents().getValue().stream()
                .filter(event -> marker.getTitle().equals(event.getCreator() + " = " + event.getGameCategory().getLabel()))
                .collect(Collectors.toCollection(ArrayList::new)).get(0);

        searchEventViewModel.setChosenEvent(clickedEvent);
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_events_to_eventDetailsFragment);
    }


    private class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView creator, category, description, date;

        public EventViewHolder(@NonNull View itemView, OnItemSelectedListener listener) {
            super(itemView);
            creator = itemView.findViewById(R.id.creatorName);
            category = itemView.findViewById(R.id.game_category);
            description = itemView.findViewById(R.id.event_description);
            date = itemView.findViewById(R.id.event_date);

            itemView.setOnClickListener(e -> {
                int currentPosition = getAdapterPosition();
                listener.onItemSelected(currentPosition);
            });
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<Event> events;

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_item, parent, false);
            return new EventViewHolder(linearLayout, position -> {
                searchEventViewModel.setChosenEvent(events.get(position));
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_events_to_eventDetailsFragment);
            });
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = events.get(position);

            holder.date.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getEventDate()));
            holder.description.setText(event.getEventDescription());
            holder.category.setText(event.getGameCategory().getLabel());
            holder.creator.setText(event.getCreator().getName());
        }

        @Override
        public int getItemCount() {
            return events == null ? 0 : events.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setEvents(List<Event> events) {
            this.events = events;
            notifyDataSetChanged();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncEventLocationGetter extends AsyncTask<Event, Void, ArrayList<LatLng>> {
        private Geocoder geocoder;

        public AsyncEventLocationGetter(Context context) {
            super();

            geocoder = new Geocoder(context, Locale.FRENCH);
        }

        @Override
        protected ArrayList<LatLng> doInBackground(Event... events) {
            ArrayList<LatLng> locations = new ArrayList<>();

            for (Event event : events) {
                try {
                    Address address = geocoder.getFromLocationName(event.getAddress().fullAddress(), 1).get(0);
                    locations.add(new LatLng(address.getLatitude(), address.getLongitude()));
                } catch (IOException ignored) {}
            }

            return locations;
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> locations) {
            for (int i = 0; i < locations.size(); i++) {
                Event event = Objects.requireNonNull(searchEventViewModel.getEvents().getValue()).get(i);

                googleMap.addMarker(
                        new MarkerOptions()
                                .position(locations.get(i))
                                .title(event.getCreator() + " = " + event.getGameCategory().getLabel())
                                .snippet(event.getAddress().fullAddress())
                );
            }
        }
    }
}
