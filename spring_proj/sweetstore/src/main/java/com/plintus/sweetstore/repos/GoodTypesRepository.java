package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.GoodTypes;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoodTypesRepository extends CrudRepository<GoodTypes, Long> {
    List<GoodTypes> findByName(String name);
}
