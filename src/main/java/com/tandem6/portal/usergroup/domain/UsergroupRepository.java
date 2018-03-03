package com.tandem6.portal.usergroup.domain;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Usergroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsergroupRepository extends JpaRepository<Usergroup, Long> {

}
