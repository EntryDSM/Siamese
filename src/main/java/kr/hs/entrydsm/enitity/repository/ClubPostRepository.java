package kr.hs.entrydsm.enitity.repository;

import kr.hs.entrydsm.enitity.Club;
import kr.hs.entrydsm.enitity.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubPostRepository extends CrudRepository<Post, Long> {

    @Override
    @Modifying
    @Query("DELETE FROM tbl_post post WHERE post.id = ?1")
    void deleteById(Long aLong);

    List<Post> findAllByClub(Club club);
}
