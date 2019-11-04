package calculation.solutons

import calculation.solutons.base.BaseSolution
import model.MainModel

class LocalError(
    override val name: String,
    private val ideal: BaseSolution,
    private val estimation: BaseSolution
) : BaseSolution() {
    private var previousGlobalError = 0.0

    override fun init(config: SolveConfig) {
        ideal.init(config)
        estimation.init(config)
    }

    override fun compute(x: Double): Double {
        val newGlobalError = ideal.compute(x) - estimation.compute(x)

        val result = newGlobalError - previousGlobalError

        previousGlobalError = newGlobalError

        return result
    }
}