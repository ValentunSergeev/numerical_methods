package calculation.data

import javafx.scene.chart.XYChart

class SolutionResult(
    val solutions: List<XYChart.Series<Number, Number>> = listOf(),
    val localErrors: List<XYChart.Series<Number, Number>> = listOf(),
    val globalErrors: List<XYChart.Series<Number, Number>> = listOf()
) {
    lateinit var solutionBorders: Borders
    lateinit var localErrorBorders: Borders
    lateinit var globalErrorBorders: Borders

    init {
        calculateBorders()
    }

    internal fun calculateBorders() {
        val errorPoints = findAllPoints(localErrors)

        val minYLocalError = errorPoints.min() ?: 0.0
        val maxYLocalError = errorPoints.max() ?: 0.0

        localErrorBorders = Borders(minYLocalError, maxYLocalError)

        val solutionPoints = findAllPoints(solutions)

        val minYSolution = solutionPoints.min() ?: 0.0
        val maxYSolution = solutionPoints.max() ?: 0.0

        solutionBorders = Borders(minYSolution, maxYSolution)

        val globalErrorsPoints = findAllPoints(globalErrors)

        val minYGlobalError = globalErrorsPoints.min() ?: 0.0
        val maxYGlobalError = globalErrorsPoints.max() ?: 0.0

        globalErrorBorders = Borders(minYGlobalError, maxYGlobalError)
    }

    private fun findAllPoints(data: List<XYChart.Series<Number, Number>>): List<Double> {
        return data.filter {
            it.node?.isVisible ?: true
        }.map { series ->
            series.data.map { entry ->
                entry.yValue as Double
            }
        }.flatten()
    }
}

class Borders(val min: Double = 0.0, val max: Double = 0.0)