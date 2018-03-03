package com.tandem6.portal.role.application.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.tandem6.portal.role.application.dto.RoleDTO;
import com.tandem6.portal.role.domain.Role;

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
