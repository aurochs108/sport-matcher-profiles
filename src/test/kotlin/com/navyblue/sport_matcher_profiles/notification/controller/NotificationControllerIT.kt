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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.delete
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
	fun `creates an unread notification for an existing profile`() {
		val profile = profileRepository.save(Profile(name = "Alex", favoriteSports = setOf(FavoriteSport.BIKE), profileImageUrl = "image"))

		mockMvc.post("/profiles/${profile.id}/notifications") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"title":" New message ","message":" You have received a new message. "}"""
		}.andExpect {
			status { isCreated() }
			jsonPath("$.id") { exists() }
			jsonPath("$.title") { value("New message") }
			jsonPath("$.message") { value("You have received a new message.") }
			jsonPath("$.read") { value(false) }
			jsonPath("$.createdAt") { exists() }
		}

		mockMvc.get("/profiles/${profile.id}/notifications").andExpect {
			status { isOk() }
			jsonPath("$.notifications.length()") { value(1) }
			jsonPath("$.notifications[0].title") { value("New message") }
		}
	}

	@Test
	fun `rejects blank notification content`() {
		val profile = profileRepository.save(Profile(name = "Alex", favoriteSports = setOf(FavoriteSport.BIKE), profileImageUrl = "image"))

		mockMvc.post("/profiles/${profile.id}/notifications") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"title":" ","message":""}"""
		}.andExpect {
			status { isBadRequest() }
		}
	}

	@Test
	fun `returns 404 when creating for an unknown profile`() {
		mockMvc.post("/profiles/${UUID.randomUUID()}/notifications") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"title":"New message","message":"Content"}"""
		}.andExpect {
			status { isNotFound() }
		}
	}

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

	@Test
	fun `deletes a notification belonging to the profile`() {
		val profile = profileRepository.save(Profile(name = "Alex", favoriteSports = setOf(FavoriteSport.BIKE), profileImageUrl = "image"))
		val notificationId = UUID.randomUUID()
		notificationRepository.save(Notification(notificationId.toString(), profile, "Title", "Message", false, Instant.now()))

		mockMvc.delete("/profiles/${profile.id}/notifications/$notificationId").andExpect {
			status { isNoContent() }
		}

		mockMvc.get("/profiles/${profile.id}/notifications").andExpect {
			status { isOk() }
			jsonPath("$.notifications.length()") { value(0) }
		}
	}

	@Test
	fun `returns 404 when deleting a missing notification`() {
		val profile = profileRepository.save(Profile(name = "Alex", favoriteSports = setOf(FavoriteSport.BIKE), profileImageUrl = "image"))

		mockMvc.delete("/profiles/${profile.id}/notifications/${UUID.randomUUID()}").andExpect {
			status { isNotFound() }
		}
	}

	@Test
	fun `returns 404 when deleting for a missing profile`() {
		mockMvc.delete("/profiles/${UUID.randomUUID()}/notifications/${UUID.randomUUID()}").andExpect {
			status { isNotFound() }
		}
	}

	@Test
	fun `does not delete notification belonging to another profile`() {
		val profile = profileRepository.save(Profile(name = "Alex", favoriteSports = setOf(FavoriteSport.BIKE), profileImageUrl = "image"))
		val anotherProfile = profileRepository.save(Profile(name = "Sam", favoriteSports = setOf(FavoriteSport.RUNNING), profileImageUrl = "image"))
		val notificationId = UUID.randomUUID()
		notificationRepository.save(Notification(notificationId.toString(), anotherProfile, "Title", "Message", false, Instant.now()))

		mockMvc.delete("/profiles/${profile.id}/notifications/$notificationId").andExpect {
			status { isNotFound() }
		}

		assert(notificationRepository.existsById(notificationId.toString()))
	}
}
