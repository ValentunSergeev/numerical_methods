package calculation.solutons

import calculation.solutons.base.BaseSolution
import model.MainModel
import kotlin.math.abs

class LocalError(
    override val name: String,
    private val ideal: BaseSolution,
    private val estimation: BaseSolution
) : BaseSolution() {
    private var previousGlobalError = 0.0

    override fun init(params: MainModel.SolutionParams) {
        ideal.init(params)
        estimation.init(params)
    }

    override fun compute(x: Double): Double {
        val newGlobalError = ideal.compute(x) - estimation.compute(x)

        val result = newGlobalError - previousGlobalError

        previousGlobalError = newGlobalError

        return result
    }
}