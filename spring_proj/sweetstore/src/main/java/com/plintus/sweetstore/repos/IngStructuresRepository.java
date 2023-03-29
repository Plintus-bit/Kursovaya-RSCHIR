package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.IngStructures;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface IngStructuresRepository extends CrudRepository<IngStructures, Integer> {
    @Query(
            value = "SELECT ing_id FROM ing_structures i WHERE i.good_id = :id",
            nativeQuery = true
    )
    public List<Integer> findIngIdByGoodId(Integer id);
    @Query(
            value = "SELECT * FROM ing_structures i WHERE i.good_id = :id",
            nativeQuery = true
    )
    List<IngStructures> findAllByGoodId(Integer id);
    @Query(
            value = "SELECT * FROM ing_structures i WHERE i.good_id in :id",
            nativeQuery = true
    )
    List<IngStructures> findAllByGoodId(Collection<Integer> id);
}
