package com.navyblue.sport_matcher_profiles.profile.service

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

enum class FavoriteSport(
	val label: String,
	val aliases: Set<String> = emptySet(),
) {
	BIKE("Bike"),
	CLIMBING("Climbing"),
	FOOTBALL("Football"),
	HOCKEY("Hockey"),
	PING_PONG("Ping Pong"),
	RUNNING("Running"),
	TENNIS("Tennis"),
	VOLLEYBALL("Volleyball", aliases = setOf("Voleyball")),
	;

	companion object {
		fun fromLabel(label: String): FavoriteSport {
			val normalizedLabel = label.trim()
			return entries.firstOrNull {
				it.label.equals(normalizedLabel, ignoreCase = true) ||
					it.aliases.any { alias -> alias.equals(normalizedLabel, ignoreCase = true) }
			}
				?: throw ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Unsupported favorite sport: $normalizedLabel",
				)
		}
	}
}
