package com.group2.Tiger_Talks.backend.repsitory.User;

import com.group2.Tiger_Talks.backend.model.UserTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTemplateRepository extends JpaRepository<UserTemplate, String> {

    Optional<UserTemplate> findUserTemplateByEmail(String email);

    Optional<UserTemplate> findUserTemplateByBannerID(int bannerId);

    Optional<UserTemplate> findUserTemplateByUserName(String userName);

}
