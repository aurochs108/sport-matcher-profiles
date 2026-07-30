package com.navyblue.sport_matcher_profiles.profile.repository

import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProfileRepository : JpaRepository<Profile, UUID>
