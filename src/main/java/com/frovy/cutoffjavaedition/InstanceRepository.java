package com.frovy.cutoffjavaedition;

import org.springframework.data.repository.CrudRepository;

import com.frovy.cutoffjavaedition.Instance;

public interface InstanceRepository extends CrudRepository<Instance, Integer> {
	Iterable<Instance> findAll();
	boolean existsById(String id);
	Instance findById(String id);
}
