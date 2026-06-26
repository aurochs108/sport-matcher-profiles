package com.navyblue.sport_matcher_profiles.profile.dto

data class CreateProfileRequest(
	val name: String? = null,
	val favoriteSports: List<String> = emptyList(),
	val profileImageUrl: String? = null,
)
