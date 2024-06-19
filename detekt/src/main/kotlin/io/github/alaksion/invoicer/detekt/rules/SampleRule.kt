package io.github.alaksion.invoicer.detekt.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtClass

class SampleRule(config: Config) : Rule(config) {

    override val issue: Issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Maintainability,
        debt = Debt(mins = 1),
        description = "This rule reports classes containing usecase on its name."
    )

    override fun visitClass(klass: KtClass) {
        if (klass.name.orEmpty().lowercase().contains("usecase")) {

        }
    }
}