package com.navyblue.sport_matcher_profiles.notification.service

import com.navyblue.sport_matcher_profiles.notification.dto.NotificationResponse
import com.navyblue.sport_matcher_profiles.notification.dto.NotificationsResponse
import com.navyblue.sport_matcher_profiles.notification.dto.CreateNotificationRequest
import com.navyblue.sport_matcher_profiles.notification.entity.Notification
import com.navyblue.sport_matcher_profiles.notification.repository.NotificationRepository
import com.navyblue.sport_matcher_profiles.profile.repository.ProfileRepository
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64
import java.util.UUID

@Service
class NotificationService(
	private val profileRepository: ProfileRepository,
	private val notificationRepository: NotificationRepository,
) {
	fun createNotification(profileId: UUID, request: CreateNotificationRequest): NotificationResponse {
		val profile = profileRepository.findById(profileId)
			.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found") }
		val notification = notificationRepository.save(
			Notification(
				id = UUID.randomUUID().toString(),
				profile = profile,
				title = request.title.trim(),
				message = request.message.trim(),
				read = false,
				createdAt = Instant.now(),
			),
		)
		return NotificationResponse(
			notification.id,
			notification.title,
			notification.message,
			notification.read,
			notification.createdAt,
		)
	}

	fun getNotifications(profileId: UUID, limit: Int, cursor: String?): NotificationsResponse {
		if (!profileRepository.existsById(profileId)) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found")
		}
		val pageable = PageRequest.of(0, limit + 1)
		val notifications = cursor?.let {
			val decodedCursor = decodeCursor(it)
			notificationRepository.findNextPage(profileId, decodedCursor.createdAt, decodedCursor.id, pageable)
		}
			?: notificationRepository.findByProfileIdOrderByCreatedAtDescIdDesc(profileId, pageable)
		val hasNextPage = notifications.size > limit
		val page = notifications.take(limit)

		return NotificationsResponse(
			notifications = page.map { NotificationResponse(it.id, it.title, it.message, it.read, it.createdAt) },
			nextCursor = if (hasNextPage) encodeCursor(page.last().createdAt, page.last().id) else null,
		)
	}

	private fun encodeCursor(createdAt: Instant, id: String): String =
		Base64.getUrlEncoder().withoutPadding().encodeToString("$createdAt|$id".toByteArray(StandardCharsets.UTF_8))

	private fun decodeCursor(cursor: String): Cursor = try {
		val parts = String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8).split("|", limit = 2)
		if (parts.size != 2 || parts[1].isEmpty()) throw IllegalArgumentException()
		Cursor(Instant.parse(parts[0]), parts[1])
	} catch (_: IllegalArgumentException) {
		throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid cursor")
	}

	private data class Cursor(val createdAt: Instant, val id: String)
}
