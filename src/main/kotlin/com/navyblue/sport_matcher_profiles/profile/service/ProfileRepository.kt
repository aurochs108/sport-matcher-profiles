package com.navyblue.sport_matcher_profiles.profile.service

import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class ProfileRepository {
	private val profiles = ConcurrentHashMap<UUID, Profile>()

	fun save(profile: Profile): Profile {
		profiles[profile.id] = profile
		return profile
	}
}
