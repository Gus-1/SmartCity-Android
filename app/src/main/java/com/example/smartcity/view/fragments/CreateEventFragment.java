package com.example.smartcity.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.smartcity.R;
import com.example.smartcity.data.model.GameCategory;
import com.example.smartcity.viewmodel.CreateEventViewModel;

import java.util.Objects;

public class CreateEventFragment extends Fragment {

    private EditText eventDescription, nbPlayer, eventStreet, eventStreetNumber, eventCity, eventPostCode, eventCountry;
    private DatePicker eventDate;
    private Spinner category;
    private Button createEvent;
    private CreateEventViewModel createEventViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_event, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.event_creation);

        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);

        eventDescription = view.findViewById(R.id.createEventDescription);
        nbPlayer = view.findViewById(R.id.createEventNbPlayer);
        eventStreet = view.findViewById(R.id.createEventStreet);
        eventStreetNumber = view.findViewById(R.id.createEventStreetNumber);
        eventCity = view.findViewById(R.id.createEventCity);
        eventPostCode = view.findViewById(R.id.createEventPostCode);
        eventCountry = view.findViewById(R.id.createEventCountry);
        eventDate = view.findViewById(R.id.createEventDatePicker);
        category = view.findViewById(R.id.createEventCategorySpinner);
        createEvent = view.findViewById(R.id.createEventButton);
        createEvent.setEnabled(false);

        createEventViewModel.loadGameCategory();

        createEventViewModel.getGameCategory().observe(getViewLifecycleOwner(), gameCategories -> {
            if (gameCategories != null) {
                category.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item, gameCategories));
            }
        });

        createEventViewModel.getCreateEventFormState().observe(getViewLifecycleOwner(), createEventFormState -> {
            if (createEventFormState == null) {
                return;
            }
            createEvent.setEnabled(createEventFormState.isDataValid());
            if(createEventFormState.getDescriptionError() != null){
                eventDescription.setError(getString(createEventFormState.getDescriptionError()));
            }
            if(createEventFormState.getStreetError() != null){
                eventStreet.setError(getString(createEventFormState.getStreetError()));
            }
            if(createEventFormState.getNumberError() != null){
                eventStreetNumber.setError(getString(createEventFormState.getNumberError()));
            }
            if(createEventFormState.getCityError() != null){
                eventCity.setError(getString(createEventFormState.getCityError()));
            }
            if(createEventFormState.getPostCodeError() != null){
                eventPostCode.setError(getString(createEventFormState.getPostCodeError()));
            }
            if(createEventFormState.getCountryError() != null){
                eventCountry.setError(getString(createEventFormState.getCountryError()));
            }
            if(createEventFormState.getNbPlayerError() != null){
                nbPlayer.setError(getString(createEventFormState.getNbPlayerError()));
            }
        });

        TextWatcher afterTextChagedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Voluntary ignored
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Voluntary ignored
            }

            @Override
            public void afterTextChanged(Editable s) {
                createEventViewModel.addEventDataChanged(eventDescription.getText().toString(), eventStreet.getText().toString(),
                        eventStreetNumber.getText().toString(), eventCity.getText().toString(), eventPostCode.getText().toString(),
                        eventCountry.getText().toString(), nbPlayer.getText().toString());
            }
        };

        eventDescription.addTextChangedListener(afterTextChagedListener);
        eventStreet.addTextChangedListener(afterTextChagedListener);
        eventStreetNumber.addTextChangedListener(afterTextChagedListener);
        eventCity.addTextChangedListener(afterTextChagedListener);
        eventPostCode.addTextChangedListener(afterTextChagedListener);
        eventCountry.addTextChangedListener(afterTextChagedListener);
        nbPlayer.addTextChangedListener(afterTextChagedListener);

        createEvent.setOnClickListener(v -> {
            createEventViewModel.addEvent(((GameCategory) category.getSelectedItem()),
                    eventDate.getYear(),eventDate.getMonth(),eventDate.getDayOfMonth(),
                    eventDescription.getText().toString(), Integer.parseInt(nbPlayer.getText().toString()), eventStreet.getText().toString(),
                    Integer.parseInt(eventStreetNumber.getText().toString()), eventCity.getText().toString(), Integer.parseInt(eventPostCode.getText().toString()),
                    eventCountry.getText().toString());

            Toast.makeText(requireContext(), R.string.event_created, Toast.LENGTH_LONG).show();
            Navigation.findNavController(v).navigate(R.id.action_createEventFragment_to_nav_myEvents);
        });
    }
}
