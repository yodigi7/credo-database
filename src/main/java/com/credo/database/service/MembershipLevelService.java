package com.credo.database.service;

import com.credo.database.domain.MembershipLevel;
import com.credo.database.repository.MembershipLevelRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MembershipLevel}.
 */
@Service
@Transactional
public class MembershipLevelService {

    private final Logger log = LoggerFactory.getLogger(MembershipLevelService.class);

    private final MembershipLevelRepository membershipLevelRepository;

    public MembershipLevelService(MembershipLevelRepository membershipLevelRepository) {
        this.membershipLevelRepository = membershipLevelRepository;
    }

    /**
     * Save a membershipLevel.
     *
     * @param membershipLevel the entity to save.
     * @return the persisted entity.
     */
    public MembershipLevel save(MembershipLevel membershipLevel) {
        log.debug("Request to save MembershipLevel : {}", membershipLevel);
        return membershipLevelRepository.save(membershipLevel);
    }

    /**
     * Partially update a membershipLevel.
     *
     * @param membershipLevel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MembershipLevel> partialUpdate(MembershipLevel membershipLevel) {
        log.debug("Request to partially update MembershipLevel : {}", membershipLevel);

        return membershipLevelRepository
            .findById(membershipLevel.getId())
            .map(
                existingMembershipLevel -> {
                    if (membershipLevel.getLevel() != null) {
                        existingMembershipLevel.setLevel(membershipLevel.getLevel());
                    }
                    if (membershipLevel.getCost() != null) {
                        existingMembershipLevel.setCost(membershipLevel.getCost());
                    }

                    return existingMembershipLevel;
                }
            )
            .map(membershipLevelRepository::save);
    }

    /**
     * Get all the membershipLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MembershipLevel> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipLevels");
        return membershipLevelRepository.findAll(pageable);
    }

    /**
     * Get one membershipLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MembershipLevel> findOne(Long id) {
        log.debug("Request to get MembershipLevel : {}", id);
        return membershipLevelRepository.findById(id);
    }

    /**
     * Delete the membershipLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MembershipLevel : {}", id);
        membershipLevelRepository.deleteById(id);
    }
}
