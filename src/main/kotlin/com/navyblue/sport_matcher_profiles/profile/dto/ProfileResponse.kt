package com.navyblue.sport_matcher_profiles.profile.dto

import java.util.UUID

data class ProfileResponse(
	val id: UUID,
	val name: String,
	val favoriteSports: List<String>,
	val profileImageUrl: String?,
)
