package com.employeedashboard.oirs.dto.squad;

import com.employeedashboard.oirs.model.Squad;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class SquadResponse {
    private UUID id;
    private String name;

    public static SquadResponse of(Squad squad) {

        return SquadResponse.builder()
                .id(squad.getId())
                .name(squad.getName())
                .build();
    }
}
