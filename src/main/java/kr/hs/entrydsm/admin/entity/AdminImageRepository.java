package kr.hs.entrydsm.admin.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminImageRepository extends CrudRepository<Image, Long> {

}