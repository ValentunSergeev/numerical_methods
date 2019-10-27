package calculation.data

import javafx.scene.chart.XYChart

class SolutionResult(
    val solutions: List<XYChart.Series<Number, Number>> = listOf(),
    val errors: List<XYChart.Series<Number, Number>> = listOf()
) {
    lateinit var solutionBorders : Borders
    lateinit var errorBorders : Borders

    init {
        findBorders()
    }

    private fun findBorders() {
        val errorPoints = findAllPoints(errors)

        val minYError = errorPoints.min() ?: 0.0
        val maxYError = errorPoints.max() ?: 0.0

        errorBorders = Borders(minYError, maxYError)

        val solutionPoints = findAllPoints(solutions)

        val minYSolution = solutionPoints.min() ?: 0.0
        val maxYSolution = solutionPoints.max() ?: 0.0

        solutionBorders = Borders(minYSolution, maxYSolution)
    }

    private fun findAllPoints(data: List<XYChart.Series<Number, Number>>): List<Double> {
        return data.map { series ->
            series.data.map { entry ->
                entry.yValue as Double
            }
        }.flatten()
    }
}

class Borders(val min: Double = 0.0, val max: Double = 0.0)