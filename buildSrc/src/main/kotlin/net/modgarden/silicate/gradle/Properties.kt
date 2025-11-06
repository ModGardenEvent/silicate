package net.modgarden.silicate.gradle

import java.net.URI

object Properties {
	const val GROUP = "lgbt.greenhouse.silicate"
	const val MOD_NAME = "Silicate"
	const val ARCHIVES_NAME = "silicate"
	const val MOD_ID = "silicate"
	const val MOD_AUTHOR = "Greenhouse"
	val MOD_CONTRIBUTORS = listOf("Sylv (Maintainer)", "MerchantCalico")
	const val DESCRIPTION = "A library providing powerful client-server-agnostic predicates with a simple API."
	const val LICENSE = "MPL-2.0"

	const val MODRINTH_PAGE = "https://modrinth.com/mod/silicate"
	const val MODRINTH_PROJECT_ID = "vaQ7oWvL"

	val FORGEJO_HOST = URI("https://git.greenhouse.lgbt")
	const val FORGEJO_REPO = "Modding/silicate"
	const val FORGEJO_COMITISH = Versions.MINECRAFT

	val JAVAC_ARGS = listOf(
		"--add-reads", "mixinextras.common=ALL-UNNAMED"
	)
}
