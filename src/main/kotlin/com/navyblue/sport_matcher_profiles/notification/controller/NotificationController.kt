package com.navyblue.sport_matcher_profiles.notification.controller

import com.navyblue.sport_matcher_profiles.notification.dto.CreateNotificationRequest
import com.navyblue.sport_matcher_profiles.notification.dto.NotificationResponse
import com.navyblue.sport_matcher_profiles.notification.dto.NotificationsResponse
import com.navyblue.sport_matcher_profiles.notification.service.NotificationService
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/profiles/{profileId}/notifications")
class NotificationController(private val notificationService: NotificationService) {
	@PostMapping
	fun createNotification(
		@PathVariable profileId: UUID,
		@Valid @RequestBody request: CreateNotificationRequest,
	): ResponseEntity<NotificationResponse> = ResponseEntity
		.status(HttpStatus.CREATED)
		.body(notificationService.createNotification(profileId, request))

	@GetMapping
	fun getNotifications(
		@PathVariable profileId: UUID,
		@RequestParam(defaultValue = "20") @Min(1) @Max(100) limit: Int,
		@RequestParam(required = false) cursor: String?,
	): NotificationsResponse = notificationService.getNotifications(profileId, limit, cursor)

	@DeleteMapping("/{notificationId}")
	fun deleteNotification(
		@PathVariable profileId: UUID,
		@PathVariable notificationId: UUID,
	): ResponseEntity<Void> {
		notificationService.deleteNotification(profileId, notificationId)
		return ResponseEntity.noContent().build()
	}
}
