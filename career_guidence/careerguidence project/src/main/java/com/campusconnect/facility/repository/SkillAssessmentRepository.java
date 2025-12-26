package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.SkillAssessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillAssessmentRepository extends JpaRepository<SkillAssessment, Long> {
    
    List<SkillAssessment> findByIsActiveTrueOrderByCreatedDateDesc();
    
    List<SkillAssessment> findBySkillCategoryAndIsActiveTrue(String skillCategory);
    
    Page<SkillAssessment> findByIsActiveTrue(Pageable pageable);
    
    @Query("SELECT DISTINCT s.skillCategory FROM SkillAssessment s WHERE s.isActive = true ORDER BY s.skillCategory")
    List<String> findDistinctSkillCategories();
    
    @Query("SELECT s FROM SkillAssessment s WHERE s.difficultyLevel = :level AND s.isActive = true")
    List<SkillAssessment> findByDifficultyLevel(@Param("level") String level);
    
    @Query("SELECT s FROM SkillAssessment s WHERE s.assessmentTitle LIKE %:searchTerm% OR s.skillCategory LIKE %:searchTerm% AND s.isActive = true")
    Page<SkillAssessment> searchAssessments(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(s) FROM SkillAssessment s WHERE s.skillCategory = :category AND s.isActive = true")
    Long countAssessmentsByCategory(@Param("category") String category);
}