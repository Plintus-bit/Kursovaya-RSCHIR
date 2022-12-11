package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.Good;
import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<Good, Long> {
}
