package com.navyblue.sport_matcher_profiles.profile.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerIntegrationTest(
	@Autowired private val mockMvc: MockMvc,
) {

	@Test
	fun `creates profile`() {
		mockMvc.post("/profiles") {
			contentType = MediaType.APPLICATION_JSON
			content = """
				{
				  "name": " Alex ",
				  "favoriteSports": ["Bike", " Ping Pong "],
				  "profileImageUrl": " https://example.com/alex.jpg "
				}
			""".trimIndent()
		}
			.andExpect {
				status { isCreated() }
				jsonPath("$.id") { exists() }
				jsonPath("$.name") { value("Alex") }
				jsonPath("$.favoriteSports[0]") { value("Bike") }
				jsonPath("$.favoriteSports[1]") { value("Ping Pong") }
				jsonPath("$.profileImageUrl") { value("https://example.com/alex.jpg") }
			}
	}

	@Test
	fun `rejects profile with blank name`() {
		mockMvc.post("/profiles") {
			contentType = MediaType.APPLICATION_JSON
			content = """
				{
				  "name": " ",
				  "favoriteSports": ["Bike"],
				  "profileImageUrl": "https://example.com/alex.jpg"
				}
			""".trimIndent()
		}
			.andExpect {
				status { isBadRequest() }
			}
	}

	@Test
	fun `rejects profile without favorite sports`() {
		mockMvc.post("/profiles") {
			contentType = MediaType.APPLICATION_JSON
			content = """
				{
				  "name": "Alex",
				  "favoriteSports": [],
				  "profileImageUrl": "https://example.com/alex.jpg"
				}
			""".trimIndent()
		}
			.andExpect {
				status { isBadRequest() }
			}
	}

	@Test
	fun `rejects profile with blank image URL`() {
		mockMvc.post("/profiles") {
			contentType = MediaType.APPLICATION_JSON
			content = """
				{
				  "name": "Alex",
				  "favoriteSports": ["Bike"],
				  "profileImageUrl": " "
				}
			""".trimIndent()
		}
			.andExpect {
				status { isBadRequest() }
			}
	}
}
