package com.firecode.elktest.biz.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.firecode.elktest.biz.domain.Role;

/**
 * 角色数据DAO
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findRolesByUserId(Long userId);
}
