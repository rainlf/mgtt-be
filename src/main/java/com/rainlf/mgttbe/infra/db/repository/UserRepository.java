package com.rainlf.mgttbe.infra.db.repository;

import com.rainlf.mgttbe.infra.db.dataobj.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Integer> {

    Optional<UserDO> findByOpenId(String openid);

    @Modifying
    @Query("update UserDO u set u.points = u.points + :point where u.id = :userId")
    int updateUserPoint(@Param("userId") Integer userId, @Param("point") Integer point);

    UserDO findByIdAndIsDeleted(Integer userId, int i);
}
