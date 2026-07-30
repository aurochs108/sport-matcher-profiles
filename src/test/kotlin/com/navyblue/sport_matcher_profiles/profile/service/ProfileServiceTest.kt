package com.navyblue.sport_matcher_profiles.profile.service

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.entity.FavoriteSport
import com.navyblue.sport_matcher_profiles.profile.entity.Profile
import com.navyblue.sport_matcher_profiles.profile.repository.ProfileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProfileServiceTest {
	private val profileRepository: ProfileRepository = mock()
	private val service = ProfileService(profileRepository)

	@Test
	fun `createProfile normalizes name removes duplicate sports saves profile and returns response`() {
		// given
		val request = CreateProfileRequest(
			name = " Alex ",
			favoriteSports = listOf(
				FavoriteSport.BIKE,
				FavoriteSport.BIKE,
				FavoriteSport.PING_PONG,
			),
			profileImageUrl = "https://example.com/alex.jpg",
		)
		whenever(profileRepository.save(any())).thenAnswer { invocation -> invocation.getArgument(0) }

		// when
		val response = service.createProfile(request)

		// then
		val profileCaptor = argumentCaptor<Profile>()
		verify(profileRepository).save(profileCaptor.capture())
		val savedProfile = profileCaptor.firstValue
		assertThat(savedProfile.name).isEqualTo("Alex")
		assertThat(savedProfile.favoriteSports).containsExactly(FavoriteSport.BIKE, FavoriteSport.PING_PONG)
		assertThat(savedProfile.profileImageUrl).isEqualTo("https://example.com/alex.jpg")
		assertThat(response.id).isEqualTo(savedProfile.id)
		assertThat(response.name).isEqualTo(savedProfile.name)
		assertThat(response.favoriteSports).isEqualTo(savedProfile.favoriteSports)
		assertThat(response.profileImageUrl).isEqualTo(savedProfile.profileImageUrl)
	}
}
