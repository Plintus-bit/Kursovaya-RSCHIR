package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.GoodTypes;
import com.plintus.sweetstore.domain.Ingredients;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface GoodTypesRepository extends CrudRepository<GoodTypes, Integer> {
    List<GoodTypes> findByName(String name);

    @Query(
            value = "SELECT name FROM good_types",
            nativeQuery = true
    )
    List<String> findAllName();

    @Query(
            value = "SELECT * FROM good_types gt WHERE gt.name in :names",
            nativeQuery = true
    )
    List<GoodTypes> findAllByNameIn(Collection<String> names);
}
