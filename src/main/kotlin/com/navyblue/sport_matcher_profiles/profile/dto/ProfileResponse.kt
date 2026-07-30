package com.navyblue.sport_matcher_profiles.profile.dto

import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import java.util.UUID

data class ProfileResponse(
	val id: UUID,
	val name: String,
	val favoriteSports: List<FavoriteSport>,
	val profileImageUrl: String,
)
