package calculation

import calculation.data.SolutionResult
import calculation.solutons.*
import calculation.solutons.base.*
import calculation.solutons.numerical.EulerSolution
import calculation.solutons.numerical.ImprovedEulerSolution
import calculation.solutons.numerical.RungeKuttaSolution
import model.MainModel

class Evaluator {
    suspend fun calculate(config: SolveConfig): SolutionResult {
        val solutions = listOf(
            AnalyticalSolution(),
            EulerSolution(), ImprovedEulerSolution(), RungeKuttaSolution()
        ).map { it.solveAsync(config).await() }

        val localErrors = listOf(
            LocalError(ERROR_LOCAL_EULER, AnalyticalSolution(), EulerSolution()),
            LocalError(ERROR_LOCAL_IMPROVED_EULER, AnalyticalSolution(), ImprovedEulerSolution()),
            LocalError(ERROR_LOCAL_KUTTA, AnalyticalSolution(), RungeKuttaSolution())
        ).map { it.solveAsync(config).await() }

        val globalErrors = listOf(
            GlobalError(ERROR_GLOBAL_EULER, AnalyticalSolution(), EulerSolution()),
            GlobalError(ERROR_GLOBAL_IMPROVED_EULER, AnalyticalSolution(), ImprovedEulerSolution()),
            GlobalError(ERROR_GLOBAL_KUTTA, AnalyticalSolution(), RungeKuttaSolution())
        ).map { it.solveAsync(config).await() }

        return SolutionResult(solutions, localErrors, globalErrors)
    }
}