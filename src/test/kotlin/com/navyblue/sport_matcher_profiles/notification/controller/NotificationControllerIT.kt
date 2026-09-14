package com.navyblue.sport_matcher_profiles.notification.controller

import com.navyblue.sport_matcher_profiles.infrastructure.PostgresContainerSupport
import com.navyblue.sport_matcher_profiles.notification.entity.Notification
import com.navyblue.sport_matcher_profiles.notification.repository.NotificationRepository
import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import com.navyblue.sport_matcher_profiles.profile.repository.ProfileRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerIT(
	@Autowired private val mockMvc: MockMvc,
	@Autowired private val profileRepository: ProfileRepository,
	@Autowired private val notificationRepository: NotificationRepository,
) : PostgresContainerSupport() {
	@Test
	fun `returns notifications newest first with cursor pagination`() {
		val profile = profileRepository.save(Profile(name = "Alex", favoriteSports = setOf(FavoriteSport.BIKE), profileImageUrl = "image"))
		val newest = Instant.parse("2026-09-14T18:30:00Z")
		notificationRepository.saveAll(
			listOf(
				Notification("first", profile, "First", "Oldest", false, newest.minusSeconds(2)),
				Notification("second", profile, "Second", "Middle", true, newest.minusSeconds(1)),
				Notification("third", profile, "Third", "Newest", false, newest),
			),
		)

		val firstPage = mockMvc.get("/profiles/${profile.id}/notifications?limit=2").andExpect {
			status { isOk() }
			jsonPath("$.notifications[0].id") { value("third") }
			jsonPath("$.notifications[0].read") { value(false) }
			jsonPath("$.nextCursor") { exists() }
		}.andReturn()
		val cursor = Regex("\"nextCursor\":\"([^\"]+)\"").find(firstPage.response.contentAsString)!!.groupValues[1]

		mockMvc.get("/profiles/${profile.id}/notifications?limit=2&cursor=$cursor").andExpect {
			status { isOk() }
			jsonPath("$.notifications[0].id") { value("first") }
			jsonPath("$.nextCursor") { doesNotExist() }
		}
	}

	@Test
	fun `returns 404 for unknown profile`() {
		mockMvc.get("/profiles/${UUID.randomUUID()}/notifications").andExpect {
			status { isNotFound() }
		}
	}
}
