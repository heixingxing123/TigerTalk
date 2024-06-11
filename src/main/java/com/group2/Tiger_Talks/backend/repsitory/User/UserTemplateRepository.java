package com.group2.Tiger_Talks.backend.repsitory.User;

import com.group2.Tiger_Talks.backend.model.User.UserTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTemplateRepository extends JpaRepository<UserTemplate, Integer> {

    /**
     * Finds user templates if they exist by their id
     *
     * @param userId UserId to search by
     * @return An optional of a user template if one was found
     */
    Optional<UserTemplate> findUserTemplateByUserId(Integer userId);

    Optional<UserTemplate> findUserTemplateByEmail(String email);

    Optional<UserTemplate> findUserTemplateByBannerID(String bannerId);

}
