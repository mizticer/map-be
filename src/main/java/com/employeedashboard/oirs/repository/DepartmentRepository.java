package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.model.Department;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface DepartmentRepository {
    @Select("SELECT d.id, d.department_name " +
            "FROM app.department d")
    @Results(id = "departmentResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "department_name")
    })
    List<Department> findAll();

    @Insert("INSERT INTO app.department (id, department_name)"
            + "VALUES (#{id}, #{name})")
    void save(Department department);

    @Select("SELECT EXISTS(SELECT 1 FROM app.department WHERE LOWER(department_name)=LOWER(#{name}))")
    boolean checkDepartmentExists(@Param("name") String name);
}