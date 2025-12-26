package com.campusconnect.facility.repository;

import com.campusconnect.facility.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    
    Optional<UserAccount> findByUsername(String username);
    
    Optional<UserAccount> findByEmailAddress(String emailAddress);
    
    Optional<UserAccount> findByUsernameOrEmailAddress(String username, String emailAddress);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmailAddress(String emailAddress);
    
    List<UserAccount> findByUserRoleAndIsAccountActiveTrue(String userRole);
    
    List<UserAccount> findByUserRole(com.campusconnect.facility.model.UserRole userRole);
    
    @Query("SELECT u FROM UserAccount u WHERE u.userRole = 'MENTOR' AND u.isAccountActive = true AND u.isEmailVerified = true")
    Page<UserAccount> findAvailableMentors(Pageable pageable);
    
    @Query("SELECT u FROM UserAccount u WHERE u.userRole = 'MENTOR' AND u.expertiseAreas LIKE %:skillArea% AND u.isAccountActive = true")
    List<UserAccount> findMentorsByExpertiseArea(@Param("skillArea") String skillArea);
    
    @Query("SELECT u FROM UserAccount u WHERE u.userRole = 'STUDENT' AND u.isAccountActive = true ORDER BY u.registrationDate DESC")
    Page<UserAccount> findRecentStudents(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserAccount u WHERE u.userRole = :role AND u.isAccountActive = true")
    Long countActiveUsersByRole(@Param("role") String role);
    
    @Query("SELECT u FROM UserAccount u WHERE u.fullName LIKE %:searchTerm% OR u.username LIKE %:searchTerm% OR u.emailAddress LIKE %:searchTerm%")
    Page<UserAccount> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
}