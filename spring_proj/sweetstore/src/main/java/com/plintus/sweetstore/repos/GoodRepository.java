package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.GoodSubtypes;
import com.plintus.sweetstore.domain.Goods;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GoodRepository extends CrudRepository<Goods, Integer> {
    List<Goods> findBySubtypeIn(Collection<GoodSubtypes> subtypes);
    Optional<Goods> findByArticle(Integer article);
    @Query(
            value = "SELECT * FROM goods WHERE article in :article",
            nativeQuery = true
    )
    List<Goods> findAllByArticleIn(Collection<Integer> article);

    @Query(
            value = "SELECT article FROM goods",
            nativeQuery = true
    )
    List<Integer> findAllArticle();
}
