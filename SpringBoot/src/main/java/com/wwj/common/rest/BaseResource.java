package com.wwj.common.rest;

import com.wwj.common.util.HeaderUtil;
import com.wwj.common.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public abstract class BaseResource {
    private static final Logger log = LoggerFactory.getLogger(BaseResource.class);
    @Value("${spring.application.name}")
    private String APPLICATION_NAME;
    private String entityName = this.getClass().getSimpleName();

    protected BaseResource resource(String entityName) {
        this.entityName = entityName;
        return this;
    }

    protected ResponseEntity createSuccess(Object entityObj) {
        HttpHeaders headers = HeaderUtil.createEntityCreationAlert(this.APPLICATION_NAME, this.entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for create success!");
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    protected ResponseEntity deleteSuccess() {
        HttpHeaders headers = HeaderUtil.createEntityDeletionAlert(this.APPLICATION_NAME, this.entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for delete success!");
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).build();
    }

    protected ResponseEntity updateSuccess(Object entityObj) {
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(this.APPLICATION_NAME, this.entityName, "");
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for update success : \n" + entityObj);
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).body(entityObj);
    }

    protected ResponseEntity pagedResponse(Page page) {
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for paged response : \n" + page);
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).body(page.getContent());
    }

    protected ResponseEntity pagedResponse(String baseUrl, Page page) {
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, baseUrl);
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for paged response : \n" + page);
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).body(page.getContent());
    }

    protected ResponseEntity listResponse(List list) {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for list response : \n" + list);
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).body(list);
    }

    protected ResponseEntity entityResponse(Object entity) {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        if (entity == null) {
            log.warn("REST response for entity response not found!");
            return ResponseEntity.notFound().headers(headers).build();
        }
        log.debug("REST response for entity response : \n" + entity);
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).body(entity);
    }

    protected ResponseEntity entityResponse(Optional maybeResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        if (maybeResponse.isPresent()) {
            Object entity = maybeResponse.get();
            log.debug("REST response for entity response : \n" + entity);
            return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).body(entity);
        }
        log.warn("REST response for entity response not found!");
        return ResponseEntity.notFound().headers(headers).build();
    }

    protected ResponseEntity successResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers = HeaderUtil.interfaceVersion(headers);
        log.debug("REST response for success response!");
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(headers)).build();
    }
}
