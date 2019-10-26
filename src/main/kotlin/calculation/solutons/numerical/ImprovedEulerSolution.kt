package calculation.solutons.numerical

import calculation.solutons.base.SOLUTION_IMPROVED_EULER

class ImprovedEulerSolution : NumericalSolution() {
    override val name = SOLUTION_IMPROVED_EULER

    override fun compute(x: Double): Double {
        val result = yNext

        val m1 = computeImplicit(x, yNext)
        val m2 = computeImplicit(x + step, yNext + step * m1)

        yNext += step * (m1 + m2) / 2

        return result
    }
}