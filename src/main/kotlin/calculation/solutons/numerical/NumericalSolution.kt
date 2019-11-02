package calculation.solutons.numerical

import calculation.solutons.base.BaseSolution
import model.MainModel
import kotlin.math.pow

abstract class NumericalSolution : BaseSolution() {
    protected var step: Double = 0.0
    protected var yNext: Double = 0.0

    override fun init(params: MainModel.SolutionParams) {
        step = params.stepSize.value
        yNext = params.yInitial.value
    }

    protected fun computeImplicit(x: Double, y: Double): Double {
        return Math.E.pow(y) - 2 / x
    }
}