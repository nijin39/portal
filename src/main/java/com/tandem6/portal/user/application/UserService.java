package com.tandem6.portal.user.application;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tandem6.portal.repository.search.UserSearchRepository;
import com.tandem6.portal.user.domain.User;
import com.tandem6.portal.user.domain.UserRepository;

/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    public UserService(UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }

    /**
     * Save a user.
     *
     * @param user the entity to save
     * @return the persisted entity
     */
    public User save(User user) {
        log.debug("Request to save User : {}", user);
        User result = userRepository.save(user);
        userSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return userRepository.findAll(pageable);
    }

    /**
     * Get one user by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public User findOne(Long id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findOne(id);
    }

    /**
     * Delete the user by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        userRepository.delete(id);
        userSearchRepository.delete(id);
    }

    /**
     * Search for the user corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<User> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Users for query {}", query);
        Page<User> result = userSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
