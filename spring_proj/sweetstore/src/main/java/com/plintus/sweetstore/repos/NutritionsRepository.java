package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.Nutritions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;

public interface NutritionsRepository extends CrudRepository<Nutritions, Integer> {
    @Query(
            value = "SELECT * FROM nutritions WHERE good_id=:id",
            nativeQuery = true
    )
    Optional<Nutritions> findByGoodId(Integer id);
}
