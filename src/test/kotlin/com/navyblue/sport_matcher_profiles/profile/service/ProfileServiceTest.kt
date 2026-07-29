package com.navyblue.sport_matcher_profiles.profile.service

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProfileServiceTest {
	private val profileRepository = RecordingProfileRepository()
	private val service = ProfileService(profileRepository)

	@Test
	fun `normalizes request saves profile and returns response`() {
		val request = CreateProfileRequest(
			name = " Alex ",
			favoriteSports = listOf("Bike", " Bike ", " Ping Pong "),
			profileImageUrl = " https://example.com/alex.jpg ",
		)

		val response = service.createProfile(request)

		val savedProfile = profileRepository.savedProfile

		assertEquals("Alex", savedProfile.name)
		assertEquals(listOf("Bike", "Ping Pong"), savedProfile.favoriteSports)
		assertEquals("https://example.com/alex.jpg", savedProfile.profileImageUrl)
		assertEquals(savedProfile.id, response.id)
		assertEquals(savedProfile.name, response.name)
		assertEquals(savedProfile.favoriteSports, response.favoriteSports)
		assertEquals(savedProfile.profileImageUrl, response.profileImageUrl)
	}
}

private class RecordingProfileRepository : ProfileRepository() {
	lateinit var savedProfile: Profile

	override fun save(profile: Profile): Profile {
		savedProfile = profile
		return profile
	}
}
