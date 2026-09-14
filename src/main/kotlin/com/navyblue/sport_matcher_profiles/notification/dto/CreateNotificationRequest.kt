package com.navyblue.sport_matcher_profiles.notification.dto

import jakarta.validation.constraints.NotBlank

data class CreateNotificationRequest(
	@field:NotBlank(message = "Notification title is required")
	val title: String,
	@field:NotBlank(message = "Notification message is required")
	val message: String,
)
