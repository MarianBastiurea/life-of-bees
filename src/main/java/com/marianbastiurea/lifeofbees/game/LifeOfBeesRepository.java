package com.marianbastiurea.lifeofbees.game;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LifeOfBeesRepository extends MongoRepository<LifeOfBees, String> {
    Optional<LifeOfBees> findByGameId(String gameId);

    List<LifeOfBees> findByUserIdAndGameType(String userId, String gameType);

    List<LifeOfBees> findByGameType(String gameType);

}