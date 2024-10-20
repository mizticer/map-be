package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.config.BaseIntegrationTest;
import com.employeedashboard.oirs.model.Squad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SquadRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private SquadRepository squadRepository;

    @Test
    @DisplayName("Finding all squads should return a list with squads count")
    void findAll_shouldReturnListOfSquads() {
        List<Squad> squadList = squadRepository.findAll();
        assertThat(squadList).isNotNull();
        assertEquals(4, squadList.size());
    }

    @Test
    @DisplayName("Inserting an squad should increase the squad count")
    void save_shouldInsertNewSquad_whenSavingValidSquad() {
        //Given
        UUID squadId = UUID.randomUUID();
        Squad squad = Squad.builder()
                .id(squadId)
                .name("testName")
                .build();
        //When
        squadRepository.save(squad);
        //Then
        List<Squad> squadList = squadRepository.findAll();
        assertEquals(5, squadList.size());
        assertEquals(squadList.get(4).getId(), squadId);
    }

    @Test
    @DisplayName("Checking whether an squad exists")
    void checkSquadExists_shouldReturnTrue_whenCheckingExistingSquad() {
        //Given
        String name = "404_squad";
        //When
        boolean squadExists = squadRepository.checkSquadExists(name);
        //Then
        assertTrue(squadExists);
    }
}