package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.GoodSubtypes;
import com.plintus.sweetstore.domain.GoodTypes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface GoodSubtypesRepository extends CrudRepository<GoodSubtypes, Integer> {
    List<GoodSubtypes> findByName(String name);
    List<GoodSubtypes> findByParentId(Integer parentId);
    @Query(
            value = "SELECT * FROM good_subtypes gs WHERE gs.name in :names",
            nativeQuery = true
    )
    List<GoodSubtypes> findAllByNameIn(Collection<String> names);
}
