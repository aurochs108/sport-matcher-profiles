package com.navyblue.sport_matcher_profiles.notification.entity

import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "notifications")
class Notification(
	@Id
	val id: String,
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "profile_id", nullable = false)
	val profile: Profile,
	@Column(nullable = false)
	val title: String,
	@Column(nullable = false)
	val message: String,
	@Column(nullable = false)
	val read: Boolean,
	@Column(name = "created_at", nullable = false)
	val createdAt: Instant,
) {
	protected constructor() : this(
		id = "",
		profile = Profile(name = "", favoriteSports = emptySet(), profileImageUrl = ""),
		title = "",
		message = "",
		read = false,
		createdAt = Instant.EPOCH,
	)
}
