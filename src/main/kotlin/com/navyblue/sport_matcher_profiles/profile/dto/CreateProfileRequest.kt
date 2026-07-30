package com.navyblue.sport_matcher_profiles.profile.dto

import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class CreateProfileRequest(
	@field:NotBlank(message = "Profile name is required")
	val name: String,

	@field:NotEmpty(message = "Select at least one favorite sport")
	val favoriteSports: List<FavoriteSport>,

	@field:NotBlank(message = "Profile image URL is required")
	val profileImageUrl: String,
)
