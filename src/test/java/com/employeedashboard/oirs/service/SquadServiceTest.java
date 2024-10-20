package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.dto.squad.SquadRequest;
import com.employeedashboard.oirs.dto.squad.SquadResponse;
import com.employeedashboard.oirs.model.Squad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SquadServiceTest extends BaseIntegrationTest {

    @Autowired
    private SquadService squadService;

    @Test
    @DisplayName("Should return a list of all squads")
    void findAll_shouldReturnListOfSquads() {
        List<SquadResponse> squadResponses = squadService.findAll();
        assertThat(squadResponses).isNotNull();
        assertEquals(4, squadResponses.size());
    }

    @Test
    @DisplayName("Should save a new squad")
    void save_shouldSaveNewSquad_whenInputDataIsCorrect() {
        //Given
        SquadRequest squadRequest = SquadRequest.builder()
                .name("testName")
                .build();
        UUID squadId = UUID.randomUUID();
        Squad squad = new Squad(squadId, "testName");
        //When
        SquadResponse savedSquad = squadService.save(squadRequest);
        //Then
        assertNotNull(savedSquad);
        assertEquals(savedSquad.getName(), squad.getName());
    }
}