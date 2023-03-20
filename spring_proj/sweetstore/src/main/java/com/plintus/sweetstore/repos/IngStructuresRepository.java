package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.IngStructures;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IngStructuresRepository extends CrudRepository<IngStructures, Integer> {
    @Query(
            value = "SELECT ing_id FROM ing_structures i WHERE i.article = :article",
            nativeQuery = true
    )
    public List<Integer> findIngIdByArticle(Integer article);
}
