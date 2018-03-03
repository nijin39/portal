package com.tandem6.portal.usergroup.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.tandem6.portal.usergroup.application.UsergroupService;
import com.tandem6.portal.usergroup.domain.Usergroup;
import com.tandem6.portal.web.rest.errors.BadRequestAlertException;
import com.tandem6.portal.web.rest.util.HeaderUtil;
import com.tandem6.portal.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Usergroup.
 */
@RestController
@RequestMapping("/api")
public class UsergroupResource {

    private final Logger log = LoggerFactory.getLogger(UsergroupResource.class);

    private static final String ENTITY_NAME = "usergroup";

    private final UsergroupService usergroupService;

    public UsergroupResource(UsergroupService usergroupService) {
        this.usergroupService = usergroupService;
    }

    /**
     * POST  /usergroups : Create a new usergroup.
     *
     * @param usergroup the usergroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usergroup, or with status 400 (Bad Request) if the usergroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/usergroups")
    @Timed
    public ResponseEntity<Usergroup> createUsergroup(@RequestBody Usergroup usergroup) throws URISyntaxException {
        log.debug("REST request to save Usergroup : {}", usergroup);
        if (usergroup.getId() != null) {
            throw new BadRequestAlertException("A new usergroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Usergroup result = usergroupService.save(usergroup);
        return ResponseEntity.created(new URI("/api/usergroups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usergroups : Updates an existing usergroup.
     *
     * @param usergroup the usergroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usergroup,
     * or with status 400 (Bad Request) if the usergroup is not valid,
     * or with status 500 (Internal Server Error) if the usergroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/usergroups")
    @Timed
    public ResponseEntity<Usergroup> updateUsergroup(@RequestBody Usergroup usergroup) throws URISyntaxException {
        log.debug("REST request to update Usergroup : {}", usergroup);
        if (usergroup.getId() == null) {
            return createUsergroup(usergroup);
        }
        Usergroup result = usergroupService.save(usergroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, usergroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usergroups : get all the usergroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of usergroups in body
     */
    @GetMapping("/usergroups")
    @Timed
    public ResponseEntity<List<Usergroup>> getAllUsergroups(Pageable pageable) {
        log.debug("REST request to get a page of Usergroups");
        Page<Usergroup> page = usergroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usergroups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /usergroups/:id : get the "id" usergroup.
     *
     * @param id the id of the usergroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usergroup, or with status 404 (Not Found)
     */
    @GetMapping("/usergroups/{id}")
    @Timed
    public ResponseEntity<Usergroup> getUsergroup(@PathVariable Long id) {
        log.debug("REST request to get Usergroup : {}", id);
        Usergroup usergroup = usergroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(usergroup));
    }

    /**
     * DELETE  /usergroups/:id : delete the "id" usergroup.
     *
     * @param id the id of the usergroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/usergroups/{id}")
    @Timed
    public ResponseEntity<Void> deleteUsergroup(@PathVariable Long id) {
        log.debug("REST request to delete Usergroup : {}", id);
        usergroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/usergroups?query=:query : search for the usergroup corresponding
     * to the query.
     *
     * @param query the query of the usergroup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/usergroups")
    @Timed
    public ResponseEntity<List<Usergroup>> searchUsergroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Usergroups for query {}", query);
        Page<Usergroup> page = usergroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/usergroups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
