package com.credo.database.service;

import com.credo.database.domain.NameTag;
import com.credo.database.repository.NameTagRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NameTag}.
 */
@Service
@Transactional
public class NameTagService {

    private final Logger log = LoggerFactory.getLogger(NameTagService.class);

    private final NameTagRepository nameTagRepository;

    public NameTagService(NameTagRepository nameTagRepository) {
        this.nameTagRepository = nameTagRepository;
    }

    /**
     * Save a nameTag.
     *
     * @param nameTag the entity to save.
     * @return the persisted entity.
     */
    public NameTag save(NameTag nameTag) {
        log.debug("Request to save NameTag : {}", nameTag);
        return nameTagRepository.save(nameTag);
    }

    /**
     * Partially update a nameTag.
     *
     * @param nameTag the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NameTag> partialUpdate(NameTag nameTag) {
        log.debug("Request to partially update NameTag : {}", nameTag);

        return nameTagRepository
            .findById(nameTag.getId())
            .map(
                existingNameTag -> {
                    if (nameTag.getNameTag() != null) {
                        existingNameTag.setNameTag(nameTag.getNameTag());
                    }

                    return existingNameTag;
                }
            )
            .map(nameTagRepository::save);
    }

    /**
     * Get all the nameTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NameTag> findAll(Pageable pageable) {
        log.debug("Request to get all NameTags");
        return nameTagRepository.findAll(pageable);
    }

    /**
     * Get one nameTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NameTag> findOne(Long id) {
        log.debug("Request to get NameTag : {}", id);
        return nameTagRepository.findById(id);
    }

    /**
     * Delete the nameTag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NameTag : {}", id);
        nameTagRepository.deleteById(id);
    }
}
