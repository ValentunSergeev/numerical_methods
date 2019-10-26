package calculation.solutons.numerical


import calculation.solutons.base.SOLUTION_KUTTA
class RungeKuttaSolution : NumericalSolution() {
    override val name = SOLUTION_KUTTA

    override fun compute(x: Double): Double {
        val result = yNext

        val halfStep = step / 2

        val k1 = computeImplicit(x, yNext)
        val k2 = computeImplicit(x + halfStep, yNext + halfStep * k1)
        val k3 = computeImplicit(x + halfStep, yNext + halfStep * k2)
        val k4 = computeImplicit(x + step, yNext + step * k3)

        yNext += step / 6 * (k1 + 2 * k2 + 2 * k3 + k4)

        return result
    }
}