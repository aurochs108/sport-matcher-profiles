package com.navyblue.sport_matcher_profiles.profile.repository

import com.navyblue.sport_matcher_profiles.infrastructure.PostgresContainerSupport
import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class ProfileRepositoryTest(
	@Autowired private val repository: ProfileRepository,
	@Autowired private val entityManager: EntityManager,
) : PostgresContainerSupport() {

	@Test
	@Transactional
	fun `persists and loads profile`() {
		// given
		val profile = Profile(
			name = "Alex",
			favoriteSports = setOf(FavoriteSport.BIKE, FavoriteSport.PING_PONG),
			profileImageUrl = "https://example.com/alex.jpg",
		)

		// when
		repository.saveAndFlush(profile)
		entityManager.clear()
		val savedProfile = repository.findById(profile.id).orElseThrow()

		// then
		assertThat(savedProfile.id).isEqualTo(profile.id)
		assertThat(savedProfile.name).isEqualTo("Alex")
		assertThat(savedProfile.favoriteSports)
			.containsExactlyInAnyOrder(FavoriteSport.BIKE, FavoriteSport.PING_PONG)
		assertThat(savedProfile.profileImageUrl).isEqualTo("https://example.com/alex.jpg")
	}
}
