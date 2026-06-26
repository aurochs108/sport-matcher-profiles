package com.navyblue.sport_matcher_profiles.profile.controller

import com.navyblue.sport_matcher_profiles.profile.dto.CreateProfileRequest
import com.navyblue.sport_matcher_profiles.profile.dto.ProfileResponse
import com.navyblue.sport_matcher_profiles.profile.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/profiles")
class ProfileController(
	private val profileService: ProfileService,
) {

	@PostMapping
	fun createProfile(@RequestBody request: CreateProfileRequest): ResponseEntity<ProfileResponse> {
		val profile = profileService.createProfile(request)
		val location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(profile.id)
			.toUri()

		return ResponseEntity
			.created(location)
			.body(profile)
	}
}
