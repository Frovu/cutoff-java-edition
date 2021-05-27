package com.frovy.cutoffjavaedition;

import org.springframework.data.repository.CrudRepository;

import com.frovy.cutoffjavaedition.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
