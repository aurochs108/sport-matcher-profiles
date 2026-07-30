package com.navyblue.sport_matcher_profiles.profile.service

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.dto.ProfileResponse
import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import com.navyblue.sport_matcher_profiles.profile.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProfileService(
	private val profileRepository: ProfileRepository,
) {

	fun createProfile(request: CreateProfileRequest): ProfileResponse {
		val name = request.name.trim()

		return profileRepository.save(
			Profile(
				id = UUID.randomUUID(),
				name = name,
				favoriteSports = request.favoriteSports,
				profileImageUrl = request.profileImageUrl,
			),
		).toResponse()
	}
}

private fun Profile.toResponse(): ProfileResponse {
	return ProfileResponse(
		id = id,
		name = name,
		favoriteSports = favoriteSports,
		profileImageUrl = profileImageUrl,
	)
}
