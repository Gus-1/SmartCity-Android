package com.example.smartcity.view.fragments;

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
import com.example.smartcity.viewmodel.MyEventViewModel;

import java.util.Objects;

public class MyEventDetailsFragment extends Fragment {

    private MyEventViewModel myEventViewModel;
    private TextView eventDate, eventDescription, eventAddress, eventCategoryLabel,
            eventCategoryDescription, eventIsVerified, eventAdminNote;
    private Button deleteButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_event_details, container, false);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Details");

        eventDate = root.findViewById(R.id.myEventDate);
        eventDescription = root.findViewById(R.id.myEventDescription);
        eventAddress = root.findViewById(R.id.myEventAddresse);
        eventCategoryLabel = root.findViewById(R.id.myEventCategoryName);
        eventCategoryDescription = root.findViewById(R.id.myEventCategoryDescription);
        eventIsVerified = root.findViewById(R.id.myEventIsVerified);
        eventAdminNote = root.findViewById(R.id.myEventAdminNote);
        deleteButton = root.findViewById(R.id.myEventDeleteButton);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myEventViewModel = new ViewModelProvider(requireActivity()).get(MyEventViewModel.class);

        myEventViewModel.getChosenEvent().observe(getViewLifecycleOwner(), event -> {
            eventDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getEventDate()));
            eventDate.setEnabled(false);

            eventDescription.setText(event.getEventDescription());
            eventDescription.setEnabled(false);

            eventAddress.setText(event.getAddress().fullAddress());
            eventAddress.setEnabled(false);

            eventCategoryLabel.setText(event.getGameCategory().getLabel());
            eventCategoryLabel.setEnabled(false);

            eventCategoryDescription.setText(event.getGameCategory().getDescription());
            eventCategoryDescription.setEnabled(false);

            eventIsVerified.setText(event.getVerified()? R.string.yes : R.string.no);
            eventIsVerified.setEnabled(false);

            eventAdminNote.setText(event.getAdminMessage() == null ? "Aucun message" : event.getAdminMessage());
            eventAdminNote.setEnabled(false);




            if (!event.getCreator().getEmail().equals(Objects.requireNonNull(myEventViewModel.getUser().getValue()).getEmail())) {
                deleteButton.setText(R.string.quit_event);
                deleteButton.setOnClickListener(v -> {
                    myEventViewModel.quitEvent(event.getId());
                    Navigation.findNavController(v).navigate(R.id.action_eventDetailsFragment_to_nav_myEvents);
                });
            } else {
                deleteButton.setOnClickListener(v -> {
                    myEventViewModel.deleteEvent(event.getId());
                    Navigation.findNavController(v).navigate(R.id.action_eventDetailsFragment_to_nav_myEvents);
                });
            }
        });
    }
}
