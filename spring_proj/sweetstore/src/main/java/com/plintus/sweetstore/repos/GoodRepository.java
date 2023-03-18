package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.Goods;
import org.springframework.data.repository.CrudRepository;

public interface GoodRepository extends CrudRepository<Goods, Long> {
}
