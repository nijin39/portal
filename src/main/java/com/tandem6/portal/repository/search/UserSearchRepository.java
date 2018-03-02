package com.tandem6.portal.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.tandem6.portal.user.domain.User;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
