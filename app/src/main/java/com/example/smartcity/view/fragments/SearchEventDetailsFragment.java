package com.example.smartcity.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.smartcity.R;
import com.example.smartcity.viewmodel.SearchEventViewModel;

import java.util.Objects;

public class SearchEventDetailsFragment extends Fragment {

    private SearchEventViewModel searchEventViewModel;
    private TextView name, firstName, creationDate, eventDate, address, categoryName, categoryDescription,
                     nbPlayer, eventDescription;
    private Button joinButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_details, container, false);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.details);

        name = root.findViewById(R.id.creator_name);
        firstName = root.findViewById(R.id.search_creator_firstname);
        creationDate = root.findViewById(R.id.creation_date);
        eventDate = root.findViewById(R.id.event_date_details);
        address = root.findViewById(R.id.complete_address);
        categoryName = root.findViewById(R.id.game_category_name_details);
        categoryDescription = root.findViewById(R.id.game_category_description);
        eventDescription = root.findViewById(R.id.event_description_details);
        nbPlayer = root.findViewById(R.id.nb_player);
        joinButton = root.findViewById(R.id.join_button);

        return root;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        searchEventViewModel = new ViewModelProvider(requireActivity()).get(SearchEventViewModel.class);

        searchEventViewModel.getChosenEvent().observe(getViewLifecycleOwner(), event -> {

            firstName.setText(event.getCreator().getFirstname());
            firstName.setEnabled(false);

            name.setText(event.getCreator().getName());
            name.setEnabled(false);

            creationDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getCreationDate()));
            creationDate.setEnabled(false);

            eventDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getEventDate()).toString());
            eventDate.setEnabled(false);

            address.setText(event.getAddress().fullAddress());
            address.setEnabled(false);

            categoryName.setText(event.getGameCategory().getLabel());
            categoryName.setEnabled(false);

            categoryDescription.setText(event.getGameCategory().getDescription());
            categoryDescription.setEnabled(false);

            eventDescription.setText(event.getEventDescription());
            eventDescription.setEnabled(false);

            nbPlayer.setText(String.format("%d", event.getNbMaxPlayer()));
            nbPlayer.setEnabled(false);


            joinButton.setOnClickListener(v -> {
                searchEventViewModel.joinEvent(Objects.requireNonNull(searchEventViewModel.getChosenEvent().getValue()));
                Navigation.findNavController(v).navigate(R.id.action_eventDetailsFragment_to_nav_events);
            });
        });
    }
}