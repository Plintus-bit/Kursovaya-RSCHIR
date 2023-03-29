package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.Nutritions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

public interface NutritionsRepository extends CrudRepository<Nutritions, Integer> {
    @Query(
            value = "SELECT * FROM nutritions WHERE good_id=:id",
            nativeQuery = true
    )
    Optional<Nutritions> findByGoodId(Integer id);
    @Query(
            value = "SELECT good_id FROM nutritions",
            nativeQuery = true
    )
    List<Integer> findAllGoodId();

    @Query(
            value = "SELECT * FROM nutritions",
            nativeQuery = true
    )
    List<Nutritions> findAllNutrs();
    @Query(
            value = "SELECT * FROM nutritions WHERE good_id in :ids",
            nativeQuery = true
    )
    List<Nutritions> findAllByGoodIdIn(Collection<Integer> ids);
}
