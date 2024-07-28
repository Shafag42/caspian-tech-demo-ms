package com.caspiantech.user.ms.dao.repository;


import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity,Long> {
  Optional<UserEntity> findByEmail(String email);

  List<UserEntity> findByRegionIgnoreCaseContaining(String region);

  List<UserEntity> findByAccountStatus(AccountStatus accountStatus);

  boolean existsByEmail(String email);

  @Query("SELECT u.region, AVG(u.salary) FROM UserEntity u GROUP BY u.region ORDER BY AVG(u.salary) DESC")
  List<Object[]> findRegionsWithHighestAverageSalary();

}
