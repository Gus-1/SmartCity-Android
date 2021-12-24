package com.example.smartcity.service;

import android.text.format.DateFormat;
import android.util.Log;

import com.example.smartcity.data.model.Address;
import com.example.smartcity.data.model.Event;
import com.example.smartcity.data.model.GameCategory;
import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.dto.AddressDto;
import com.example.smartcity.repositories.web.dto.EventDto;
import com.example.smartcity.repositories.web.dto.GameCategoryDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    private static EventMapper instance = null;

    public static EventMapper getInstance(){
        if(instance == null){
            instance = new EventMapper();
        }
        return instance;
    }

    public List<Event> mapToEvent (List<EventDto> eventDto){
        return eventDto == null || eventDto.isEmpty() ? null :
                eventDto.stream().map(event -> {
                    try {
                        User user = new User(event.getCreator().getUserId(), event.getCreator().getFirstName(),
                                event.getCreator().getLastname(), event.getCreator().getBirthdate(),
                                event.getCreator().getAdmin(), event.getCreator().getEmail(),
                                event.getCreator().getPhotoPath());

                        GameCategory gameCategory = new GameCategory(event.getGameCategory().getGameCategoryId(),
                                event.getGameCategory().getLabel(), event.getGameCategory().getDescription());

                        Address address = new Address(event.getAddress().getId(), event.getAddress().getStreet(),
                                event.getAddress().getNumber(), event.getAddress().getPostalCode(),
                                event.getAddress().getCity(), event.getAddress().getCountry());

                        return new Event(event.getId(), user, gameCategory,
                                event.getCreationDate(), event.getEventDate(), address, event.getEventDescription(),
                                event.getVerified(), event.getNbMaxPlayer(), event.getAdminMessage());
                    } catch (ParseException e) {
                        Log.e("MapToEvent - Parsing error", e.getLocalizedMessage());
                    }
                    return null;
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    public EventDto mapToEventDto(Event event){
        return event == null ? null : new EventDto(
            new GameCategoryDto(
                event.getGameCategory().getGameCategoryId(),
                event.getGameCategory().getLabel(),
                event.getGameCategory().getDescription()
            ),

            (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getEventDate()),

            new AddressDto(
                event.getAddress().getStreet(),
                event.getAddress().getNumber(),
                event.getAddress().getCountry(),
                event.getAddress().getCity(),
                event.getAddress().getPostalcode()
            ),

            event.getEventDescription(),
            event.getVerified(),
            event.getNbMaxPlayer(),
            event.getAdminMessage()
        );
    }
}
