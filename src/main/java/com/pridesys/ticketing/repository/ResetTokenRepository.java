package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.*;

public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, Long> {
    void deleteByUserId(long userId);

    @Modifying
    @Query("update ResetTokenEntity t set t.usedAt=:now where t.userId=:user and t.usedAt is null")
    void invalidateOutstanding(@Param("user") long user, @Param("now") Instant now);

    Optional<ResetTokenEntity> findByTokenHash(String hash);
}
