package calculation.solutons.numerical

import calculation.solutons.base.SOLUTION_EULER

open class EulerSolution : NumericalSolution() {
    override val name = SOLUTION_EULER

    override fun compute(x: Double): Double {
        val result = yNext

        yNext += computeImplicit(x, yNext) * step

        return result
    }
}