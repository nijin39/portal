package com.tandem6.portal.repository;

import org.springframework.stereotype.Repository;

import com.tandem6.portal.role.domain.Role;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
