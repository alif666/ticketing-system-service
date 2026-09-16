package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    @Query("select u.clientId from UserEntity u where u.id=:id")
    Optional<Long> clientId(@org.springframework.data.repository.query.Param("id") long id);

    @Query("select u from UserEntity u where lower(u.email) like lower(concat('%',:q,'%')) or lower(u.name) like lower(concat('%',:q,'%'))")
    Page<UserEntity> search(@org.springframework.data.repository.query.Param("q") String q, Pageable pageable);
}
