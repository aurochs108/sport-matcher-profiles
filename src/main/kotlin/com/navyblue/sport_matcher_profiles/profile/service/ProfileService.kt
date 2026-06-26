package com.navyblue.sport_matcher_profiles.profile.service

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.dto.ProfileResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ProfileService(
	private val profileRepository: ProfileRepository,
) {

	fun createProfile(request: CreateProfileRequest): ProfileResponse {
		val name = request.name?.trim()
		if (name.isNullOrEmpty()) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile name is required")
		}

		val favoriteSports = request.favoriteSports
			.map { FavoriteSport.fromLabel(it) }
			.distinct()

		if (favoriteSports.isEmpty()) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Select at least one favorite sport")
		}

		return profileRepository.save(
			Profile(
				id = UUID.randomUUID(),
				name = name,
				favoriteSports = favoriteSports,
				profileImageUrl = request.profileImageUrl?.trim()?.takeIf { it.isNotEmpty() },
			),
		).toResponse()
	}
}

private fun Profile.toResponse() = ProfileResponse(
	id = id,
	name = name,
	favoriteSports = favoriteSports.map { it.label },
	profileImageUrl = profileImageUrl,
)
