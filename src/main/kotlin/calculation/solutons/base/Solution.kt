package calculation.solutons.base

import javafx.scene.chart.XYChart
import kotlinx.coroutines.Deferred
import model.MainModel

const val SOLUTION_ANALYTICAL = "Analytical"
const val SOLUTION_EULER = "Euler"
const val SOLUTION_IMPROVED_EULER = "Improved Euler"
const val SOLUTION_KUTTA = "Runge Kutta"

const val ERROR_EULER = "Euler Error"
const val ERROR_IMPROVED_EULER = "Improved Euler Error"
const val ERROR_KUTTA = "Runge Kutta Error"

interface Solution {
    suspend fun solveAsync(params: MainModel.SolutionParams): Deferred<XYChart.Series<Number, Number>>
}