package com.example.smartcity.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartcity.R;
import com.example.smartcity.data.model.GameCategory;
import com.example.smartcity.viewmodel.MyEventViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateMyEventFragment extends Fragment {
    private MyEventViewModel myEventViewModel;
    private EditText eventDescription, nbPlayer, eventStreet, eventStreetNumber, eventCity, eventPostCode, eventCountry;
    private DatePicker eventDatePicker;
    private Spinner categorySpinner;
    private Button confirmUpdate;
    private Button cancelUpdate;


    public UpdateMyEventFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myEventViewModel = new ViewModelProvider(requireActivity()).get(MyEventViewModel.class);

        myEventViewModel.loadGameCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_my_event, container, false);

        eventDescription = root.findViewById(R.id.eventToUpdateDescription);
        nbPlayer = root.findViewById(R.id.eventToUpdatePlayerNumber);
        eventStreet = root.findViewById(R.id.eventToUpdateStreet);
        eventStreetNumber = root.findViewById(R.id.eventToUpdateStreetNumber);
        eventCity = root.findViewById(R.id.eventToUpdateCity);
        eventPostCode = root.findViewById(R.id.eventToUpdatePostalCode);
        eventCountry = root.findViewById(R.id.eventToUpdateCountry);

        eventDatePicker = root.findViewById(R.id.eventToUpdateDatePicker);
        categorySpinner = root.findViewById(R.id.eventToUpdateCategorySpinner);

        confirmUpdate = root.findViewById(R.id.updateEventConfirmButton);
        cancelUpdate = root.findViewById(R.id.updateEventCancelButton);

        myEventViewModel.getGameCategories().observe(this.getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item, categories));
            }
        });

        myEventViewModel.getChosenEvent().observe(this.getViewLifecycleOwner(), event -> {
            List<GameCategory> categories = myEventViewModel.getGameCategories().getValue();

            while (categories == null) {
                categories = myEventViewModel.getGameCategories().getValue();
            }

            eventDescription.setText(event.getEventDescription());
            nbPlayer.setText(event.getNbMaxPlayer());
            eventStreet.setText(event.getAddress().getStreet());
            eventStreetNumber.setText(event.getAddress().getNumber());
            eventCity.setText(event.getAddress().getCity());
            eventPostCode.setText(event.getAddress().getPostalcode());
            eventCountry.setText(event.getAddress().getCountry());

            eventDatePicker.updateDate(
                    event.getEventDate().get(Calendar.YEAR),
                    event.getEventDate().get(Calendar.MONTH),
                    event.getEventDate().get(Calendar.DAY_OF_MONTH)
            );

            categorySpinner.setSelection(categories.indexOf(event.getGameCategory()));
        });

        cancelUpdate.setOnClickListener(view -> requireActivity().onBackPressed());

        confirmUpdate.setOnClickListener(view -> {
            myEventViewModel.updateEvent(
                    (GameCategory) categorySpinner.getSelectedItem(), eventDatePicker.getYear(),
                    eventDatePicker.getMonth(), eventDatePicker.getDayOfMonth(), eventDescription.getText().toString(),
                    Integer.parseInt(nbPlayer.getText().toString()), eventStreet.getText().toString(),
                    Integer.parseInt(eventStreetNumber.getText().toString()), eventCity.getText().toString(),
                    Integer.parseInt(eventPostCode.getText().toString()), eventCountry.getText().toString());

            Navigation.findNavController(view).navigate(R.id.action_updateMyEventFragment_to_myEventDetailsFragment);

        });

        myEventViewModel.getStatusCode().observe(this.getViewLifecycleOwner(), status -> {
            if (status == 200) {
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireContext(), R.string.server_error, Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}