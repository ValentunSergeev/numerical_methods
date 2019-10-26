package calculation.solutons.numerical

import calculation.solutons.base.BaseSolution
import calculation.solutons.base.SOLUTION_EULER
import model.MainModel
import kotlin.math.pow

open class EulerSolution : NumericalSolution() {
    override val name = SOLUTION_EULER

    override fun compute(x: Double): Double {
        val result = yNext

        yNext += computeImplicit(x, yNext) * step

        return result
    }
}