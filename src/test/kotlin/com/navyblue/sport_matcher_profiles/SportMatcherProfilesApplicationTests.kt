package com.navyblue.sport_matcher_profiles

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header

@SpringBootTest
@AutoConfigureMockMvc
class SportMatcherProfilesApplicationTests(
	@Autowired private val mockMvc: MockMvc,
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `creates profile`() {
		mockMvc.post("/profiles") {
			contentType = MediaType.APPLICATION_JSON
			content = """
				{
				  "name": "Alex",
				  "favoriteSports": ["Bike", "Ping Pong"],
				  "profileImageUrl": "https://example.com/alex.jpg"
				}
			""".trimIndent()
		}
			.andExpect {
				status { isCreated() }
				header().exists("Location")
				jsonPath("$.id") { exists() }
				jsonPath("$.name") { value("Alex") }
				jsonPath("$.favoriteSports[0]") { value("Bike") }
				jsonPath("$.favoriteSports[1]") { value("Ping Pong") }
				jsonPath("$.profileImageUrl") { value("https://example.com/alex.jpg") }
			}
	}

	@Test
	fun `rejects profile without selected sports`() {
		mockMvc.post("/profiles") {
			contentType = MediaType.APPLICATION_JSON
			content = """
				{
				  "name": "Alex",
				  "favoriteSports": []
				}
			""".trimIndent()
		}
			.andExpect {
				status { isBadRequest() }
			}
	}
}
