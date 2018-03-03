package com.tandem6.portal.web.rest;

import com.tandem6.portal.PortalApp;
import com.tandem6.portal.usergroup.application.UsergroupService;
import com.tandem6.portal.usergroup.domain.Usergroup;
import com.tandem6.portal.usergroup.domain.UsergroupRepository;
import com.tandem6.portal.usergroup.ui.UsergroupResource;
import com.tandem6.portal.repository.search.UsergroupSearchRepository;
import com.tandem6.portal.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tandem6.portal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UsergroupResource REST controller.
 *
 * @see UsergroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PortalApp.class)
public class UsergroupResourceIntTest {

    @Autowired
    private UsergroupRepository usergroupRepository;

    @Autowired
    private UsergroupService usergroupService;

    @Autowired
    private UsergroupSearchRepository usergroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUsergroupMockMvc;

    private Usergroup usergroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UsergroupResource usergroupResource = new UsergroupResource(usergroupService);
        this.restUsergroupMockMvc = MockMvcBuilders.standaloneSetup(usergroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usergroup createEntity(EntityManager em) {
        Usergroup usergroup = new Usergroup();
        return usergroup;
    }

    @Before
    public void initTest() {
        usergroupSearchRepository.deleteAll();
        usergroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsergroup() throws Exception {
        int databaseSizeBeforeCreate = usergroupRepository.findAll().size();

        // Create the Usergroup
        restUsergroupMockMvc.perform(post("/api/usergroups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usergroup)))
            .andExpect(status().isCreated());

        // Validate the Usergroup in the database
        List<Usergroup> usergroupList = usergroupRepository.findAll();
        assertThat(usergroupList).hasSize(databaseSizeBeforeCreate + 1);
        Usergroup testUsergroup = usergroupList.get(usergroupList.size() - 1);

        // Validate the Usergroup in Elasticsearch
        Usergroup usergroupEs = usergroupSearchRepository.findOne(testUsergroup.getId());
        assertThat(usergroupEs).isEqualToIgnoringGivenFields(testUsergroup);
    }

    @Test
    @Transactional
    public void createUsergroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usergroupRepository.findAll().size();

        // Create the Usergroup with an existing ID
        usergroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsergroupMockMvc.perform(post("/api/usergroups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usergroup)))
            .andExpect(status().isBadRequest());

        // Validate the Usergroup in the database
        List<Usergroup> usergroupList = usergroupRepository.findAll();
        assertThat(usergroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUsergroups() throws Exception {
        // Initialize the database
        usergroupRepository.saveAndFlush(usergroup);

        // Get all the usergroupList
        restUsergroupMockMvc.perform(get("/api/usergroups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usergroup.getId().intValue())));
    }

    @Test
    @Transactional
    public void getUsergroup() throws Exception {
        // Initialize the database
        usergroupRepository.saveAndFlush(usergroup);

        // Get the usergroup
        restUsergroupMockMvc.perform(get("/api/usergroups/{id}", usergroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usergroup.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUsergroup() throws Exception {
        // Get the usergroup
        restUsergroupMockMvc.perform(get("/api/usergroups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsergroup() throws Exception {
        // Initialize the database
        usergroupService.save(usergroup);

        int databaseSizeBeforeUpdate = usergroupRepository.findAll().size();

        // Update the usergroup
        Usergroup updatedUsergroup = usergroupRepository.findOne(usergroup.getId());
        // Disconnect from session so that the updates on updatedUsergroup are not directly saved in db
        em.detach(updatedUsergroup);

        restUsergroupMockMvc.perform(put("/api/usergroups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUsergroup)))
            .andExpect(status().isOk());

        // Validate the Usergroup in the database
        List<Usergroup> usergroupList = usergroupRepository.findAll();
        assertThat(usergroupList).hasSize(databaseSizeBeforeUpdate);
        Usergroup testUsergroup = usergroupList.get(usergroupList.size() - 1);

        // Validate the Usergroup in Elasticsearch
        Usergroup usergroupEs = usergroupSearchRepository.findOne(testUsergroup.getId());
        assertThat(usergroupEs).isEqualToIgnoringGivenFields(testUsergroup);
    }

    @Test
    @Transactional
    public void updateNonExistingUsergroup() throws Exception {
        int databaseSizeBeforeUpdate = usergroupRepository.findAll().size();

        // Create the Usergroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUsergroupMockMvc.perform(put("/api/usergroups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usergroup)))
            .andExpect(status().isCreated());

        // Validate the Usergroup in the database
        List<Usergroup> usergroupList = usergroupRepository.findAll();
        assertThat(usergroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUsergroup() throws Exception {
        // Initialize the database
        usergroupService.save(usergroup);

        int databaseSizeBeforeDelete = usergroupRepository.findAll().size();

        // Get the usergroup
        restUsergroupMockMvc.perform(delete("/api/usergroups/{id}", usergroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean usergroupExistsInEs = usergroupSearchRepository.exists(usergroup.getId());
        assertThat(usergroupExistsInEs).isFalse();

        // Validate the database is empty
        List<Usergroup> usergroupList = usergroupRepository.findAll();
        assertThat(usergroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUsergroup() throws Exception {
        // Initialize the database
        usergroupService.save(usergroup);

        // Search the usergroup
        restUsergroupMockMvc.perform(get("/api/_search/usergroups?query=id:" + usergroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usergroup.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usergroup.class);
        Usergroup usergroup1 = new Usergroup();
        usergroup1.setId(1L);
        Usergroup usergroup2 = new Usergroup();
        usergroup2.setId(usergroup1.getId());
        assertThat(usergroup1).isEqualTo(usergroup2);
        usergroup2.setId(2L);
        assertThat(usergroup1).isNotEqualTo(usergroup2);
        usergroup1.setId(null);
        assertThat(usergroup1).isNotEqualTo(usergroup2);
    }
}
