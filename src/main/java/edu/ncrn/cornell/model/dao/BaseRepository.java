package edu.ncrn.cornell.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
 
@NoRepositoryBean
interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

    public List<T> findAll();
    //TODO: for impls,  possible canonical implementation (remove when implemented):
    //    List<T> findAll() {
    //        Iterable<T> all = findAll();
    //        List<T> target = new ArrayList<>();
    //        all.forEach(target::add);
    //        return target;
    //    };
     
}