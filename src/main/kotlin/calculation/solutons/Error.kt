package calculation.solutons

import calculation.solutons.base.BaseSolution
import model.MainModel
import kotlin.math.abs

class Error(
    override val name: String,
    private val ideal: BaseSolution,
    private val estimation: BaseSolution
) : BaseSolution() {

    override fun init(params: MainModel.SolutionParams) {
        ideal.init(params)
        estimation.init(params)
    }

    override fun compute(x: Double) = abs(ideal.compute(x) - estimation.compute(x))
}