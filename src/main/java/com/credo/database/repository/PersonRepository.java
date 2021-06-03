package com.credo.database.repository;

import com.credo.database.domain.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Person entity.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    @Query(
        value = "select distinct person from Person person left join fetch person.organizations",
        countQuery = "select count(distinct person) from Person person"
    )
    Page<Person> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct person from Person person left join fetch person.organizations")
    List<Person> findAllWithEagerRelationships();

    @Query("select person from Person person left join fetch person.organizations where person.id =:id")
    Optional<Person> findOneWithEagerRelationships(@Param("id") Long id);
}
