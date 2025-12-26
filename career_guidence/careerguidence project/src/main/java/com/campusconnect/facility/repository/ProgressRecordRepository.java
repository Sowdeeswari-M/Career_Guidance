package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.ProgressRecord;
import com.campusconnect.facility.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, Long> {
    
    List<ProgressRecord> findByUserAccountOrderByAchievedDateDesc(UserAccount userAccount);
    
    List<ProgressRecord> findByUserAccountAndAchievementCategoryOrderByAchievedDateDesc(
        UserAccount userAccount, String achievementCategory);
    
    @Query("SELECT p FROM ProgressRecord p WHERE p.userAccount = :user AND p.isVerified = true ORDER BY p.achievedDate DESC")
    List<ProgressRecord> findVerifiedAchievementsByUser(@Param("user") UserAccount user);
    
    @Query("SELECT SUM(p.pointsEarned) FROM ProgressRecord p WHERE p.userAccount = :user AND p.isVerified = true")
    Integer getTotalPointsByUser(@Param("user") UserAccount user);
    
    @Query("SELECT COUNT(p) FROM ProgressRecord p WHERE p.userAccount = :user AND p.achievementCategory = :category")
    Long countAchievementsByUserAndCategory(@Param("user") UserAccount user, @Param("category") String category);
    
    @Query("SELECT p FROM ProgressRecord p WHERE p.achievedDate BETWEEN :startDate AND :endDate ORDER BY p.achievedDate DESC")
    List<ProgressRecord> findAchievementsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);
    
    Page<ProgressRecord> findByIsVerifiedFalseOrderByAchievedDateDesc(Pageable pageable);
    
    @Query("SELECT p FROM ProgressRecord p WHERE p.userAccount = :user AND p.achievementType = :type ORDER BY p.achievedDate DESC")
    List<ProgressRecord> findByUserAndAchievementType(@Param("user") UserAccount user, @Param("type") String type);
    
    @Query("SELECT DISTINCT p.achievementType FROM ProgressRecord p WHERE p.userAccount = :user ORDER BY p.achievementType")
    List<String> findDistinctAchievementTypesByUser(@Param("user") UserAccount user);
}