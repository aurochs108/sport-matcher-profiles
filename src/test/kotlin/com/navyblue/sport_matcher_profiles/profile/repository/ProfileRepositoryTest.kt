package com.navyblue.sport_matcher_profiles.profile.repository

import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class ProfileRepositoryTest {
	private val repository = ProfileRepository()

	@Test
	fun `saves and returns profile`() {
		// given
		val profile = Profile(
			id = UUID.randomUUID(),
			name = "Alex",
			favoriteSports = listOf("Bike"),
			profileImageUrl = "https://example.com/alex.jpg",
		)

		// when
		val savedProfile = repository.save(profile)

		// then
		assertThat(savedProfile).isSameAs(profile)
	}
}
