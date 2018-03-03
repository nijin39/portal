package com.tandem6.portal.repository.search;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.tandem6.portal.role.domain.Role;

/**
 * Spring Data Elasticsearch repository for the Role entity.
 */
public interface RoleSearchRepository extends ElasticsearchRepository<Role, Long> {
}
