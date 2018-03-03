package com.tandem6.portal.usergroup.application;

import com.tandem6.portal.repository.search.UsergroupSearchRepository;
import com.tandem6.portal.usergroup.domain.Usergroup;
import com.tandem6.portal.usergroup.domain.UsergroupRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Usergroup.
 */
@Service
@Transactional
public class UsergroupService {

    private final Logger log = LoggerFactory.getLogger(UsergroupService.class);

    private final UsergroupRepository usergroupRepository;

    private final UsergroupSearchRepository usergroupSearchRepository;

    public UsergroupService(UsergroupRepository usergroupRepository, UsergroupSearchRepository usergroupSearchRepository) {
        this.usergroupRepository = usergroupRepository;
        this.usergroupSearchRepository = usergroupSearchRepository;
    }

    /**
     * Save a usergroup.
     *
     * @param usergroup the entity to save
     * @return the persisted entity
     */
    public Usergroup save(Usergroup usergroup) {
        log.debug("Request to save Usergroup : {}", usergroup);
        Usergroup result = usergroupRepository.save(usergroup);
        usergroupSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the usergroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Usergroup> findAll(Pageable pageable) {
        log.debug("Request to get all Usergroups");
        return usergroupRepository.findAll(pageable);
    }

    /**
     * Get one usergroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Usergroup findOne(Long id) {
        log.debug("Request to get Usergroup : {}", id);
        return usergroupRepository.findOne(id);
    }

    /**
     * Delete the usergroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Usergroup : {}", id);
        usergroupRepository.delete(id);
        usergroupSearchRepository.delete(id);
    }

    /**
     * Search for the usergroup corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Usergroup> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Usergroups for query {}", query);
        Page<Usergroup> result = usergroupSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
