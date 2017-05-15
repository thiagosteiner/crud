package com.steiner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.domain.MyEntity;

import com.steiner.repository.MyEntityRepository;
import com.steiner.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MyEntity.
 */
@RestController
@RequestMapping("/api")
public class MyEntityResource {

    private final Logger log = LoggerFactory.getLogger(MyEntityResource.class);

    private static final String ENTITY_NAME = "myEntity";
        
    private final MyEntityRepository myEntityRepository;

    public MyEntityResource(MyEntityRepository myEntityRepository) {
        this.myEntityRepository = myEntityRepository;
    }

    /**
     * POST  /my-entities : Create a new myEntity.
     *
     * @param myEntity the myEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new myEntity, or with status 400 (Bad Request) if the myEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/my-entities")
    @Timed
    public ResponseEntity<MyEntity> createMyEntity(@Valid @RequestBody MyEntity myEntity) throws URISyntaxException {
        log.debug("REST request to save MyEntity : {}", myEntity);
        if (myEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new myEntity cannot already have an ID")).body(null);
        }
        MyEntity result = myEntityRepository.save(myEntity);
        return ResponseEntity.created(new URI("/api/my-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /my-entities : Updates an existing myEntity.
     *
     * @param myEntity the myEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated myEntity,
     * or with status 400 (Bad Request) if the myEntity is not valid,
     * or with status 500 (Internal Server Error) if the myEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/my-entities")
    @Timed
    public ResponseEntity<MyEntity> updateMyEntity(@Valid @RequestBody MyEntity myEntity) throws URISyntaxException {
        log.debug("REST request to update MyEntity : {}", myEntity);
        if (myEntity.getId() == null) {
            return createMyEntity(myEntity);
        }
        MyEntity result = myEntityRepository.save(myEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, myEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /my-entities : get all the myEntities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of myEntities in body
     */
    @GetMapping("/my-entities")
    @Timed
    public List<MyEntity> getAllMyEntities() {
        log.debug("REST request to get all MyEntities");
        List<MyEntity> myEntities = myEntityRepository.findAll();
        return myEntities;
    }

    /**
     * GET  /my-entities/:id : get the "id" myEntity.
     *
     * @param id the id of the myEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the myEntity, or with status 404 (Not Found)
     */
    @GetMapping("/my-entities/{id}")
    @Timed
    public ResponseEntity<MyEntity> getMyEntity(@PathVariable String id) {
        log.debug("REST request to get MyEntity : {}", id);
        MyEntity myEntity = myEntityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(myEntity));
    }

    /**
     * DELETE  /my-entities/:id : delete the "id" myEntity.
     *
     * @param id the id of the myEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/my-entities/{id}")
    @Timed
    public ResponseEntity<Void> deleteMyEntity(@PathVariable String id) {
        log.debug("REST request to delete MyEntity : {}", id);
        myEntityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

}
