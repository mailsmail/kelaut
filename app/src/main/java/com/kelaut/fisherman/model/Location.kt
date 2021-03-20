package com.kelaut.fisherman.model

import java.io.Serializable

data class Location(
        val district: String = "",
        val city: String = "",
        val province: String = "",
        val detail: String = ""
): Serializable