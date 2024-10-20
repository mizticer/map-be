package com.employeedashboard.oirs.service;

import com.employeedashboard.oirs.dto.squad.SquadRequest;
import com.employeedashboard.oirs.dto.squad.SquadResponse;
import com.employeedashboard.oirs.exception.SquadAlreadyExistException;
import com.employeedashboard.oirs.model.Squad;
import com.employeedashboard.oirs.repository.SquadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SquadService {
    private final SquadRepository squadRepository;

    @Transactional(readOnly = true)
    public List<SquadResponse> findAll() {
        return squadRepository.findAll()
                .stream()
                .map(SquadResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public SquadResponse save(SquadRequest squadRequest) {
        if (!squadRepository.checkSquadExists(squadRequest.getName())) {
            Squad squad = Squad.builder()
                    .id(UUID.randomUUID())
                    .name(squadRequest.getName())
                    .build();
            squadRepository.save(squad);
            return SquadResponse.of(squad);
        }
        throw new SquadAlreadyExistException(squadRequest.getName());
    }
}
