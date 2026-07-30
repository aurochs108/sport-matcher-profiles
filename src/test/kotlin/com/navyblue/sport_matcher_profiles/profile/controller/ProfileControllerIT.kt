package com.navyblue.sport_matcher_profiles.profile.controller

import com.navyblue.sport_matcher_profiles.infrastructure.PostgresContainerSupport
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerIT(
	@Autowired private val mockMvc: MockMvc,
) : PostgresContainerSupport() {

	@Test
	fun `createProfile returns HTTP 201 with normalized persisted profile`() {
		mockMvc
			.post("/profiles") {
				contentType = MediaType.APPLICATION_JSON
				content = """
					{
					  "name": " Alex ",
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
	}
}
