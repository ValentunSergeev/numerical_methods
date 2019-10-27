package calculation

import calculation.data.SolutionResult
import calculation.solutons.AnalyticalSolution
import calculation.solutons.LocalError
import calculation.solutons.base.ERROR_EULER
import calculation.solutons.base.ERROR_IMPROVED_EULER
import calculation.solutons.base.ERROR_KUTTA
import calculation.solutons.numerical.EulerSolution
import calculation.solutons.numerical.ImprovedEulerSolution
import calculation.solutons.numerical.RungeKuttaSolution
import model.MainModel

class Evaluator {
    suspend fun calculate(params: MainModel.SolutionParams): SolutionResult {
        val solutions = listOf(
            AnalyticalSolution(),
            EulerSolution(), ImprovedEulerSolution(), RungeKuttaSolution()
        ).map { it.solveAsync(params).await() }

        val errors = listOf(
            LocalError(ERROR_EULER, AnalyticalSolution(), EulerSolution()),
            LocalError(ERROR_IMPROVED_EULER, AnalyticalSolution(), ImprovedEulerSolution()),
            LocalError(ERROR_KUTTA, AnalyticalSolution(), RungeKuttaSolution())
        ).map { it.solveAsync(params).await() }

        return SolutionResult(solutions, errors)
    }
}