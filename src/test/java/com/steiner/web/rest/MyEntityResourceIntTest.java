package com.steiner.web.rest;

import com.steiner.CrudApp;

import com.steiner.domain.MyEntity;
import com.steiner.repository.MyEntityRepository;
import com.steiner.web.rest.errors.ExceptionTranslator;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MyEntityResource REST controller.
 *
 * @see MyEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrudApp.class)
public class MyEntityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    @Autowired
    private MyEntityRepository myEntityRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restMyEntityMockMvc;

    private MyEntity myEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MyEntityResource myEntityResource = new MyEntityResource(myEntityRepository);
        this.restMyEntityMockMvc = MockMvcBuilders.standaloneSetup(myEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyEntity createEntity() {
        MyEntity myEntity = new MyEntity()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return myEntity;
    }

    @Before
    public void initTest() {
        myEntityRepository.deleteAll();
        myEntity = createEntity();
    }

    @Test
    public void createMyEntity() throws Exception {
        int databaseSizeBeforeCreate = myEntityRepository.findAll().size();

        // Create the MyEntity
        restMyEntityMockMvc.perform(post("/api/my-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myEntity)))
            .andExpect(status().isCreated());

        // Validate the MyEntity in the database
        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeCreate + 1);
        MyEntity testMyEntity = myEntityList.get(myEntityList.size() - 1);
        assertThat(testMyEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyEntity.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    public void createMyEntityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myEntityRepository.findAll().size();

        // Create the MyEntity with an existing ID
        myEntity.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyEntityMockMvc.perform(post("/api/my-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myEntity)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = myEntityRepository.findAll().size();
        // set the field null
        myEntity.setName(null);

        // Create the MyEntity, which fails.

        restMyEntityMockMvc.perform(post("/api/my-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myEntity)))
            .andExpect(status().isBadRequest());

        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = myEntityRepository.findAll().size();
        // set the field null
        myEntity.setCode(null);

        // Create the MyEntity, which fails.

        restMyEntityMockMvc.perform(post("/api/my-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myEntity)))
            .andExpect(status().isBadRequest());

        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllMyEntities() throws Exception {
        // Initialize the database
        myEntityRepository.save(myEntity);

        // Get all the myEntityList
        restMyEntityMockMvc.perform(get("/api/my-entities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myEntity.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    public void getMyEntity() throws Exception {
        // Initialize the database
        myEntityRepository.save(myEntity);

        // Get the myEntity
        restMyEntityMockMvc.perform(get("/api/my-entities/{id}", myEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(myEntity.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    public void getNonExistingMyEntity() throws Exception {
        // Get the myEntity
        restMyEntityMockMvc.perform(get("/api/my-entities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMyEntity() throws Exception {
        // Initialize the database
        myEntityRepository.save(myEntity);
        int databaseSizeBeforeUpdate = myEntityRepository.findAll().size();

        // Update the myEntity
        MyEntity updatedMyEntity = myEntityRepository.findOne(myEntity.getId());
        updatedMyEntity
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);

        restMyEntityMockMvc.perform(put("/api/my-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMyEntity)))
            .andExpect(status().isOk());

        // Validate the MyEntity in the database
        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeUpdate);
        MyEntity testMyEntity = myEntityList.get(myEntityList.size() - 1);
        assertThat(testMyEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyEntity.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    public void updateNonExistingMyEntity() throws Exception {
        int databaseSizeBeforeUpdate = myEntityRepository.findAll().size();

        // Create the MyEntity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMyEntityMockMvc.perform(put("/api/my-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myEntity)))
            .andExpect(status().isCreated());

        // Validate the MyEntity in the database
        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteMyEntity() throws Exception {
        // Initialize the database
        myEntityRepository.save(myEntity);
        int databaseSizeBeforeDelete = myEntityRepository.findAll().size();

        // Get the myEntity
        restMyEntityMockMvc.perform(delete("/api/my-entities/{id}", myEntity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MyEntity> myEntityList = myEntityRepository.findAll();
        assertThat(myEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyEntity.class);
        MyEntity myEntity1 = new MyEntity();
        myEntity1.setId("id1");
        MyEntity myEntity2 = new MyEntity();
        myEntity2.setId(myEntity1.getId());
        assertThat(myEntity1).isEqualTo(myEntity2);
        myEntity2.setId("id2");
        assertThat(myEntity1).isNotEqualTo(myEntity2);
        myEntity1.setId(null);
        assertThat(myEntity1).isNotEqualTo(myEntity2);
    }
}
