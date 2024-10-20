package com.employeedashboard.oirs.controller;

import com.employeedashboard.oirs.dto.squad.SquadRequest;
import com.employeedashboard.oirs.dto.squad.SquadResponse;
import com.employeedashboard.oirs.service.SquadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/squad")
public class SquadController {
    private final SquadService squadService;

    @GetMapping
    public List<SquadResponse> findAll() {
        return squadService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public SquadResponse save(@Valid @RequestBody SquadRequest squadRequest) {
        return squadService.save(squadRequest);
    }
}
