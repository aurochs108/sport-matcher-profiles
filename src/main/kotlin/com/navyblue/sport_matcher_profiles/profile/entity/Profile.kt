package com.navyblue.sport_matcher_profiles.profile.entity

import java.util.UUID

data class Profile(
	val id: UUID,
	val name: String,
	val favoriteSports: List<FavoriteSport>,
	val profileImageUrl: String,
)
