package com.navyblue.sport_matcher_profiles.profile.controller

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.dto.ProfileResponse
import com.navyblue.sport_matcher_profiles.profile.service.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profiles")
class ProfileController(
	private val profileService: ProfileService,
) {

	@PostMapping
	fun createProfile(@RequestBody request: CreateProfileRequest): ResponseEntity<ProfileResponse> {
		val profile = profileService.createProfile(request)

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(profile)
	}
}
