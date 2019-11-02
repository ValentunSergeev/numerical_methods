package calculation.solutons

import calculation.solutons.base.BaseSolution
import calculation.solutons.base.SOLUTION_ANALYTICAL
import model.MainModel
import kotlin.math.E
import kotlin.math.log
import kotlin.math.pow


class AnalyticalSolution : BaseSolution() {
    override val name = SOLUTION_ANALYTICAL

    private var constant: Double = 0.0

    override fun init(params: MainModel.SolutionParams) {
        constant = findConstant(params.xInitial.value, params.yInitial.value)
    }

    override fun compute(x: Double): Double {
        return -log(constant * x * x + x, E)
    }

    private fun findConstant(xInitial: Double, yInitial: Double): Double {
        return 1 / (Math.E.pow(yInitial) * xInitial.pow(2)) - 1 / xInitial
    }
}