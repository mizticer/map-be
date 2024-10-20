package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.model.Employee;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Mapper
@Repository
public interface EmployeeRepository {

    @Insert("INSERT INTO app.employee (id, first_name, last_name, photo, role, department, username, password, address,squad, permission)"
            + "VALUES (#{id}, #{firstName}, #{lastName}, #{photo}, #{role}, #{department}, #{username}, #{password}, #{address.id}, #{squad}, #{permission})")
    void save(Employee employee);

    @Select({
            "<script>",
            "SELECT e.id AS employee_id, e.first_name, e.last_name, e.photo, e.role, e.department, e.squad, e.username, e.permission, a.id AS address_id, a.city, a.street, a.state, a.country, a.postcode ",
            "FROM app.employee e ",
            "INNER JOIN app.address a ON e.address = a.id ",
            "<where>",
            "<if test='cities != null and cities.size() > 0'>",
            " AND a.city IN ",
            "<foreach item='city' collection='cities' open='(' separator=',' close=')'>",
            "#{city}",
            "</foreach>",
            "</if>",
            "<if test='squads != null and squads.size() > 0'>",
            " AND e.squad IN ",
            "<foreach item='squad' collection='squads' open='(' separator=',' close=')'>",
            "#{squad}",
            "</foreach>",
            "</if>",
            "</where>",
            "</script>"
    })
    @Results(id = "employeeResultMap", value = {
            @Result(property = "id", column = "employee_id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "photo", column = "photo"),
            @Result(property = "role", column = "role"),
            @Result(property = "department", column = "department"),
            @Result(property = "squad", column = "squad"),
            @Result(property = "username", column = "username"),
            @Result(property = "permission", column = "permission"),
            @Result(property = "address.id", column = "address_id"),
            @Result(property = "address.city", column = "city"),
            @Result(property = "address.street", column = "street"),
            @Result(property = "address.state", column = "state"),
            @Result(property = "address.country", column = "country"),
            @Result(property = "address.postcode", column = "postcode"),
    })
    List<Employee> findAll(@Param("cities") List<String> cities, @Param("squads") List<String> squads);

    @Select("SELECT EXISTS(SELECT 1 FROM app.employee WHERE first_name=#{firstName} AND last_name=#{lastName} AND role=#{role} AND department=#{department})")
    boolean checkEmployeeExists(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("role") String role, @Param("department") String department);

    @Select("SELECT * FROM app.employee WHERE username=#{username}")
    Optional<Employee> findByUsername(@Param("username") String username);

    @Select("SELECT e.id AS employee_id, e.first_name, e.last_name, e.photo, e.role, e.department, e.squad, e. username, e.permission, a.id AS address_id, a.city, a.street, a.state, a.country, a.postcode " +
            "FROM app.employee e " +
            "INNER JOIN app.address a ON e.address = a.id WHERE e.id = #{id}")
    @ResultMap("employeeResultMap")
    Optional<Employee> findById(@Param("id") UUID id);

    @Delete("DELETE FROM app.employee WHERE id = #{id}")
    void delete(@Param("id") UUID id);

    @Update("UPDATE app.employee " +
            "SET first_name = #{firstName}, " +
            "last_name = #{lastName}, " +
            "photo = #{photo}, " +
            "role = #{role}, " +
            "department = #{department}, " +
            "squad = #{squad}, " +
            "permission = #{permission}, " +
            "address = #{address.id} " +
            "WHERE id = #{id}")
    void editByAdminUser(Employee employee);

    @Update("UPDATE app.employee " +
            "SET photo = #{photo}, " +
            "address = #{address.id} " +
            "WHERE id = #{id}")
    void editByRegularUser(Employee employee);

}

