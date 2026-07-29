package com.navyblue.sport_matcher_profiles.profile.controller

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.dto.ProfileResponse
import com.navyblue.sport_matcher_profiles.profile.service.ProfileService
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import java.util.UUID
import kotlin.test.assertEquals

class ProfileControllerTest {
	private val profileService = mock(ProfileService::class.java)
	private val controller = ProfileController(profileService)

	@Test
	fun `creates profile through service and returns created response`() {
		val request = CreateProfileRequest(
			name = "Alex",
			favoriteSports = listOf("Bike", "Ping Pong"),
			profileImageUrl = "https://example.com/alex.jpg",
		)
		val profile = ProfileResponse(
			id = UUID.randomUUID(),
			name = request.name,
			favoriteSports = request.favoriteSports,
			profileImageUrl = request.profileImageUrl,
		)
		`when`(profileService.createProfile(request)).thenReturn(profile)

		val response = controller.createProfile(request)

		assertEquals(HttpStatus.CREATED, response.statusCode)
		assertSame(profile, response.body)
		verify(profileService).createProfile(request)
	}
}
