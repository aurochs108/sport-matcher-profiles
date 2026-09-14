package com.navyblue.sport_matcher_profiles.notification.dto

import java.time.Instant

data class NotificationResponse(
	val id: String,
	val title: String,
	val message: String,
	val read: Boolean,
	val createdAt: Instant,
)

data class NotificationsResponse(
	val notifications: List<NotificationResponse>,
	val nextCursor: String?,
)
