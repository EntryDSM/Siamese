package kr.hs.entrydsm.admin.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("adminImageRepository")
public interface ImageRepository extends CrudRepository<Image, Long> {

}
