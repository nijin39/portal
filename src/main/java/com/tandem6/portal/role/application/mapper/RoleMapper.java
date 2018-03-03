package com.tandem6.portal.role.application.mapper;

import com.tandem6.portal.domain.*;
import com.tandem6.portal.role.application.dto.RoleDTO;
import com.tandem6.portal.role.domain.Role;

import org.mapstruct.*;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {



    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
