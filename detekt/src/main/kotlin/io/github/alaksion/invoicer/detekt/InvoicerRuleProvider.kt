package io.github.alaksion.invoicer.detekt

import io.github.alaksion.invoicer.detekt.rules.SampleRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class InvoicerRuleProvider : RuleSetProvider {

    override val ruleSetId: String = "Invoicer-RuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            id = ruleSetId,
            rules = listOf(SampleRule(config))
        )
    }
}