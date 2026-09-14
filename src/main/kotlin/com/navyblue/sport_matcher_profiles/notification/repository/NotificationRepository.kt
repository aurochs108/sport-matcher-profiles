package com.navyblue.sport_matcher_profiles.notification.repository

import com.navyblue.sport_matcher_profiles.notification.entity.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface NotificationRepository : JpaRepository<Notification, String> {
	fun findByProfileIdOrderByCreatedAtDescIdDesc(profileId: UUID, pageable: Pageable): List<Notification>
	@Query(
		"""
		select notification from Notification notification
		where notification.profile.id = :profileId
		  and (notification.createdAt < :createdAt
		       or (notification.createdAt = :createdAt and notification.id < :id))
		order by notification.createdAt desc, notification.id desc
		""",
	)
	fun findNextPage(
		@Param("profileId") profileId: UUID,
		@Param("createdAt") createdAt: Instant,
		@Param("id") id: String,
		pageable: Pageable,
	): List<Notification>
}
