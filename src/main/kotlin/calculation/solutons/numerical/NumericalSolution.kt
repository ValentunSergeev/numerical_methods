package calculation.solutons.numerical

import calculation.solutons.SolveConfig
import calculation.solutons.base.BaseSolution
import kotlin.math.pow

abstract class NumericalSolution : BaseSolution() {
    protected var step: Double = 0.0
    protected var yNext: Double = 0.0

    override fun init(config: SolveConfig) {
        step = config.xStepSize
        yNext = config.yInitial
    }

    protected fun computeImplicit(x: Double, y: Double): Double {
        return Math.E.pow(y) - 2 / x
    }
}