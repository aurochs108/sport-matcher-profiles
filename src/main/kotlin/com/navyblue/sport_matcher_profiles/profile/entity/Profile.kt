package com.navyblue.sport_matcher_profiles.profile.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "profiles")
class Profile(
	@Id
	val id: UUID = UUID.randomUUID(),
	@Column(nullable = false)
	val name: String,
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "profile_favorite_sports",
		joinColumns = [JoinColumn(name = "profile_id", nullable = false)],
	)
	@Enumerated(EnumType.STRING)
	@Column(name = "favorite_sport", nullable = false)
	val favoriteSports: Set<FavoriteSport>,
	@Column(name = "profile_image_url", nullable = false, length = 2048)
	val profileImageUrl: String,
)
