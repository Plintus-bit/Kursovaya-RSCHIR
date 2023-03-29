package com.plintus.sweetstore.repos;

import com.plintus.sweetstore.domain.Ingredients;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface IngredientsRepository extends CrudRepository<Ingredients, Integer> {
    @Query(
            value = "SELECT name FROM ingredients i WHERE i.id in :ids",
            nativeQuery = true
    )
    public List<String> findNameByIdIn(Collection<Integer> ids);
    @Query(
            value = "SELECT name FROM ingredients",
            nativeQuery = true
    )
    public List<String> findAllName();
    public Ingredients findByName(String name);
    @Query(
            value = "SELECT * FROM ingredients i WHERE i.name in :names or i.id in :ids",
            nativeQuery = true
    )
    public List<Ingredients> findAllByNameInOrIdIn(Collection<String> names,
                                                       Collection<Integer> ids);
    @Query(
            value = "SELECT * FROM ingredients i WHERE i.name in :names",
            nativeQuery = true
    )
    public List<Ingredients> findAllByNameIn(Collection<String> names);
    @Query(
            value = "SELECT * FROM ingredients i WHERE i.id in :ids",
            nativeQuery = true
    )
    public List<Ingredients> findAllByIdIn(Collection<Integer> ids);
    @Query(
            value = "SELECT * FROM ingredients",
            nativeQuery = true
    )
    List<Ingredients> findAllIngs();
}
