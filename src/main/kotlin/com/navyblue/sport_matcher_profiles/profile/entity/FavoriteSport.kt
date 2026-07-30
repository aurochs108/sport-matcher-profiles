package com.navyblue.sport_matcher_profiles.profile.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class FavoriteSport(
	@get:JsonValue
	val label: String,
) {
	BIKE("Bike"),
	CLIMBING("Climbing"),
	FOOTBALL("Football"),
	HOCKEY("Hockey"),
	PING_PONG("Ping Pong"),
	RUNNING("Running"),
	TENNIS("Tennis"),
	VOLLEYBALL("Volleyball"),
	;

	companion object {
		@JvmStatic
		@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
		fun fromLabel(label: String): FavoriteSport {
			return entries.firstOrNull { it.label == label }
				?: throw IllegalArgumentException("Unknown favorite sport: $label")
		}
	}
}
