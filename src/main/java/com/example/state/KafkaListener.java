package com.example.state;

import com.example.state.event.CommonEvent;
import com.example.state.event.KeeperDispatched;
import com.example.state.event.PopulationChanged;
import com.example.state.event.SpaceCreated;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


@Service
public class KafkaListener {

    final Integer MAX_POPULATION = 10;

    @Autowired
    private StateRepository stateRepository;

    @StreamListener(Processor.INPUT)
    public void onEvent(@Payload String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CommonEvent event = null;

        try {
            event = objectMapper.readValue(message, CommonEvent.class);

            if (event.getEventType().equals(SpaceCreated.class.getSimpleName())) {
                SpaceCreated spaceCreated = new SpaceCreated((HashMap<String, Object>) event.getContent());
                State state = new State();
                state.setSpace(spaceCreated.getSpaceId());
                state.setPopulate(0);
                stateRepository.save(state);
            } else if (event.getEventType().equals(KeeperDispatched.class.getSimpleName())) {
                KeeperDispatched keeperDispatched = new KeeperDispatched((HashMap<String, Object>) event.getContent());
                if(keeperDispatched.getSpace() != null) {
                    Optional<State> optionalState = stateRepository.findById(keeperDispatched.getSpace());
                    if(optionalState.isPresent()) {
                        State state = optionalState.get();
                        state.setSpace(keeperDispatched.getSpace());
                        state.setKeeper(keeperDispatched.getName());
                        stateRepository.save(state);
                    }
                } else {
                    Optional<State> optionalState = stateRepository.findByKeeper(keeperDispatched.getName());
                    if(optionalState.isPresent()) {
                        State state = optionalState.get();
                        state.setKeeper(null);
                        stateRepository.save(state);
                    }
                }



            } else if (event.getEventType().equals(PopulationChanged.class.getSimpleName())) {
                PopulationChanged populationChanged = new PopulationChanged((HashMap<String, Object>) event.getContent());
                Optional<State> optionalState = stateRepository.findById(populationChanged.getSpaceId());
                if(optionalState.isPresent()) {
                    State state = optionalState.get();
                    state.setPopulate(populationChanged.getPopulation());
                    stateRepository.save(state);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
