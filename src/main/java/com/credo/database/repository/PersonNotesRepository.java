package com.credo.database.repository;

import com.credo.database.domain.PersonNotes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonNotes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonNotesRepository extends JpaRepository<PersonNotes, Long>, JpaSpecificationExecutor<PersonNotes> {}
