package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.CareerRecommendation;
import com.campusconnect.facility.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRecommendationRepository extends JpaRepository<CareerRecommendation, Long> {
    
    List<CareerRecommendation> findByStudentUserAndRecommendationStatusOrderByMatchPercentageDesc(
        UserAccount studentUser, String recommendationStatus);
    
    List<CareerRecommendation> findByStudentUserOrderByGeneratedDateDesc(UserAccount studentUser);
    
    @Query("SELECT c FROM CareerRecommendation c WHERE c.studentUser = :user AND c.recommendationStatus = 'ACTIVE' ORDER BY c.matchPercentage DESC")
    List<CareerRecommendation> findActiveRecommendationsForUser(@Param("user") UserAccount user);
    
    @Query("SELECT DISTINCT c.careerPath FROM CareerRecommendation c WHERE c.recommendationStatus = 'ACTIVE' ORDER BY c.careerPath")
    List<String> findDistinctCareerPaths();
    
    @Query("SELECT c FROM CareerRecommendation c WHERE c.careerPath = :careerPath AND c.recommendationStatus = 'ACTIVE' ORDER BY c.matchPercentage DESC")
    List<CareerRecommendation> findByCareerPath(@Param("careerPath") String careerPath);
    
    @Query("SELECT COUNT(c) FROM CareerRecommendation c WHERE c.studentUser = :user AND c.recommendationStatus = 'ACTIVE'")
    Long countActiveRecommendationsForUser(@Param("user") UserAccount user);
    
    Page<CareerRecommendation> findByRecommendationStatusOrderByGeneratedDateDesc(String recommendationStatus, Pageable pageable);
    
    @Query("SELECT c FROM CareerRecommendation c WHERE c.matchPercentage >= :minPercentage AND c.recommendationStatus = 'ACTIVE' ORDER BY c.matchPercentage DESC")
    List<CareerRecommendation> findHighMatchRecommendations(@Param("minPercentage") Double minPercentage);
}