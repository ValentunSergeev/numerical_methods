package calculation

import calculation.solutons.base.ERROR_EULER
import calculation.solutons.base.ERROR_IMPROVED_EULER
import calculation.solutons.Error
import calculation.data.SolutionResult
import calculation.solutons.AnalyticalSolution
import calculation.solutons.base.ERROR_KUTTA
import calculation.solutons.numerical.EulerSolution
import calculation.solutons.numerical.ImprovedEulerSolution
import calculation.solutons.numerical.RungeKuttaSolution
import model.MainModel

class Evaluator {

    suspend fun calculate(params: MainModel.SolutionParams): SolutionResult {
        val solutions = listOf(AnalyticalSolution(),
            EulerSolution(), ImprovedEulerSolution(), RungeKuttaSolution()
        )

        val errors = listOf(
            Error(ERROR_EULER, AnalyticalSolution(), EulerSolution()),
            Error(ERROR_IMPROVED_EULER, AnalyticalSolution(), ImprovedEulerSolution()),
            Error(ERROR_KUTTA, AnalyticalSolution(), RungeKuttaSolution())
        )

        val result = (solutions + errors).map {
            it.solveAsync(params).await()
        }

        return SolutionResult(result)
    }
}