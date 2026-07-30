package com.navyblue.sport_matcher_profiles.profile.dto

import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CreateProfileRequest(
	@field:NotBlank(message = "Profile name is required")
	val name: String,

	@field:NotEmpty(message = "Select at least one favorite sport")
	val favoriteSports: Set<FavoriteSport>,

	@field:NotBlank(message = "Profile image URL is required")
	@field:Size(max = 2048, message = "Profile image URL must not exceed 2048 characters")
	val profileImageUrl: String,
)
