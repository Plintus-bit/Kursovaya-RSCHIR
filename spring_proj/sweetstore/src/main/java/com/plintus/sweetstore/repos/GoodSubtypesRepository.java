package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.GoodSubtypes;
import com.plintus.sweetstore.domain.GoodTypes;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoodSubtypesRepository extends CrudRepository<GoodSubtypes, Long> {
    List<GoodSubtypes> findByName(String name);
}
