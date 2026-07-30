package com.navyblue.sport_matcher_profiles.profile.controller

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.dto.ProfileResponse
import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import com.navyblue.sport_matcher_profiles.profile.service.ProfileService
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

@WebMvcTest(ProfileController::class)
class ProfileControllerTest {
	@Autowired
	lateinit var mockMvc: MockMvc

	@MockitoBean
	lateinit var profileService: ProfileService

	@Test
	fun `createProfile returns HTTP 201 with profile on successful creation`() {
		whenever(profileService.createProfile(any())).thenReturn(
			ProfileResponse(
				id = UUID.randomUUID(),
				name = "Alex",
				favoriteSports = setOf(FavoriteSport.BIKE, FavoriteSport.PING_PONG),
				profileImageUrl = "https://example.com/alex.jpg",
			),
		)

		mockMvc
			.post("/profiles") {
				contentType = MediaType.APPLICATION_JSON
				content = """
					{
					  "name": "Alex",
					  "favoriteSports": ["Bike", "Ping Pong"],
					  "profileImageUrl": "https://example.com/alex.jpg"
					}
				""".trimIndent()
			}.andExpect {
				status { isCreated() }
				jsonPath("$.id") { exists() }
				jsonPath("$.name") { value("Alex") }
				jsonPath("$.favoriteSports") { value(containsInAnyOrder("Bike", "Ping Pong")) }
				jsonPath("$.profileImageUrl") { value("https://example.com/alex.jpg") }
			}

		verify(profileService).createProfile(
			CreateProfileRequest(
				name = "Alex",
				favoriteSports = setOf(FavoriteSport.BIKE, FavoriteSport.PING_PONG),
				profileImageUrl = "https://example.com/alex.jpg",
			),
		)
	}

	@Test
	fun `createProfile returns HTTP 400 when favorite sport is unknown`() {
		mockMvc
			.post("/profiles") {
				contentType = MediaType.APPLICATION_JSON
				content = """
					{
					  "name": "Alex",
					  "favoriteSports": ["Chess"],
					  "profileImageUrl": "https://example.com/alex.jpg"
					}
				""".trimIndent()
			}.andExpect {
				status { isBadRequest() }
			}

		verify(profileService, never()).createProfile(any())
	}

	@Test
	fun `createProfile returns HTTP 400 when name is blank`() {
		mockMvc
			.post("/profiles") {
				contentType = MediaType.APPLICATION_JSON
				content = """
					{
					  "name": " ",
					  "favoriteSports": ["Bike"],
					  "profileImageUrl": "https://example.com/alex.jpg"
					}
				""".trimIndent()
			}.andExpect {
				status { isBadRequest() }
			}

		verify(profileService, never()).createProfile(any())
	}

	@Test
	fun `createProfile returns HTTP 400 when favorite sports are empty`() {
		mockMvc
			.post("/profiles") {
				contentType = MediaType.APPLICATION_JSON
				content = """
					{
					  "name": "Alex",
					  "favoriteSports": [],
					  "profileImageUrl": "https://example.com/alex.jpg"
					}
				""".trimIndent()
			}.andExpect {
				status { isBadRequest() }
			}

		verify(profileService, never()).createProfile(any())
	}

	@Test
	fun `createProfile returns HTTP 400 when profile image URL is blank`() {
		mockMvc
			.post("/profiles") {
				contentType = MediaType.APPLICATION_JSON
				content = """
					{
					  "name": "Alex",
					  "favoriteSports": ["Bike"],
					  "profileImageUrl": " "
					}
				""".trimIndent()
			}.andExpect {
				status { isBadRequest() }
			}

		verify(profileService, never()).createProfile(any())
	}
}
