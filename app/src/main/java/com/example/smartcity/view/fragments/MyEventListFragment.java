package com.example.smartcity.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity.R;
import com.example.smartcity.data.model.Event;

import com.example.smartcity.utils.OnItemSelectedListener;
import com.example.smartcity.viewmodel.MyEventViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

public class MyEventListFragment extends Fragment {

    private MyEventViewModel myEventViewModel;
    private RecyclerView eventRecyclerView;
    private TabLayout tabLayout;
    private Button createEventButton;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_events, container, false);

        eventRecyclerView = root.findViewById(R.id.my_event_list);

        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int position = sharedPreferences.getInt("myEventTabPosition", 0);

        myEventViewModel.loadCreatorsEvents();
        myEventViewModel.loadJoinedEvents();

        EventAdapter adapter = new EventAdapter();

        if (position == 1){
            myEventViewModel.getEventsJoined().observe(getViewLifecycleOwner(), adapter::setEvents);
        } else {
            myEventViewModel.getEventsCreator().observe(getViewLifecycleOwner(), adapter::setEvents);
        }

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        eventRecyclerView.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tab_layout_my_events);
        Objects.requireNonNull(tabLayout.getTabAt(position)).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                int currentPosition = tab.getPosition();
                editor.putInt("myEventTabPosition", currentPosition);
                editor.apply();

                if (currentPosition == 1){
                    myEventViewModel.getEventsJoined().observe(getViewLifecycleOwner(), adapter::setEvents);
                } else {
                    myEventViewModel.getEventsCreator().observe(getViewLifecycleOwner(), adapter::setEvents);
                }

                eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                eventRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Voluntary ignored
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Voluntary ignored
            }
        });

        myEventViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                Toast.makeText(requireContext(), "Une erreur est apparue", Toast.LENGTH_LONG).show();
            }
        });

        myEventViewModel.getStatusCode().observe(getViewLifecycleOwner(), statusCode -> {
            if (statusCode == 404)
                Toast.makeText(requireContext(), R.string.no_event_found, Toast.LENGTH_LONG).show();
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myEventViewModel = new ViewModelProvider(requireActivity()).get(MyEventViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createEventButton = view.findViewById(R.id.my_event_create_event);
        createEventButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_myEvent_to_nav_createEventFragment));
    }

    private class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventDate, eventDescription, eventCreator, nbPlayer;

        public EventViewHolder(@NonNull View itemView, OnItemSelectedListener listener) {
            super(itemView);
            eventDate = itemView.findViewById(R.id.my_event_date);
            eventDescription = itemView.findViewById(R.id.my_event_description);
            eventCreator = itemView.findViewById(R.id.my_event_creator);
            nbPlayer = itemView.findViewById(R.id.my_event_nb_player);

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
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_event_item, parent, false);
            return new EventViewHolder(linearLayout, position -> {
                myEventViewModel.setChosenEvent(events.get(position));
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_myEvents_to_myEventDetailsFragment);
            });
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = events.get(position);

            holder.eventDate.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getEventDate()));
            holder.eventDescription.setText(event.getEventDescription());
            holder.eventCreator.setText(event.getCreator().toString());
            holder.nbPlayer.setText(String.format("%d", event.getNbMaxPlayer()));
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
}