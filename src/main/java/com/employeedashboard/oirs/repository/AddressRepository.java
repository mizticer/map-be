package com.employeedashboard.oirs.repository;

import com.employeedashboard.oirs.model.Address;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AddressRepository {
    @Insert("INSERT INTO app.address (id, city, street, country, state, postcode)"
            + "VALUES (#{id}, #{city}, #{street}, #{country}, #{state}, #{postcode})")
    void save(Address address);

    @Select("SELECT * FROM app.address")
    List<Address> findAll();

    @Select("SELECT EXISTS(SELECT 1 FROM app.address WHERE city=#{city} AND street=#{street} AND country=#{country} AND state=#{state} AND postcode=#{postcode})")
    boolean checkAddressExists(@Param("city") String city, @Param("street") String street, @Param("country") String country, @Param("state") String state, @Param("postcode") String postcode);

    @Select("SELECT * FROM app.address WHERE city = #{city} AND street = #{street} AND country = #{country} AND state = #{state} AND postcode = #{postcode}")
    Address getAddressByDetails(@Param("city") String city, @Param("street") String street, @Param("country") String country, @Param("state") String state, @Param("postcode") String postcode);

    @Update("UPDATE app.address " +
            "SET city = #{city}, street = #{street}, country = #{country}, " +
            "state = #{state}, postcode = #{postcode} " +
            "WHERE id = #{id}")
    void edit(Address address);

    @Select("SELECT DISTINCT city FROM app.address")
    List<String> findAllCities();
}

