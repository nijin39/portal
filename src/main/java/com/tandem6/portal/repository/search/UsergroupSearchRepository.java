package com.tandem6.portal.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.tandem6.portal.usergroup.domain.Usergroup;

/**
 * Spring Data Elasticsearch repository for the Usergroup entity.
 */
public interface UsergroupSearchRepository extends ElasticsearchRepository<Usergroup, Long> {
}
