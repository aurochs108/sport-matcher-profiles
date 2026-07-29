package com.navyblue.sport_matcher_profiles.profile.service

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

class ProfileRepositoryTest {
	private val repository = ProfileRepository()

	@Test
	fun `saves and returns profile`() {
		val profile = Profile(
			id = UUID.randomUUID(),
			name = "Alex",
			favoriteSports = listOf("Bike"),
			profileImageUrl = "https://example.com/alex.jpg",
		)

		val savedProfile = repository.save(profile)

		assertSame(profile, savedProfile)
	}
}
