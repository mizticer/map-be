package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.model.Squad;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SquadRepository {
    @Select("SELECT s.id, s.squad_name " +
            "FROM app.squad s")
    @Results(id = "squadResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "squad_name")
    })
    List<Squad> findAll();

    @Insert("INSERT INTO app.squad (id, squad_name)"
            + "VALUES (#{id}, #{name})")
    void save(Squad squad);

    @Select("SELECT EXISTS(SELECT 1 FROM app.squad WHERE LOWER(squad_name)=LOWER(#{name}))")
    boolean checkSquadExists(@Param("name") String name);

}
