package calculation.solutons.base

import calculation.solutons.SolveConfig
import javafx.scene.chart.XYChart
import kotlinx.coroutines.Deferred
import model.MainModel

const val SOLUTION_ANALYTICAL = "Analytical"
const val SOLUTION_EULER = "Euler"
const val SOLUTION_IMPROVED_EULER = "Improved Euler"
const val SOLUTION_KUTTA = "Runge Kutta"

const val ERROR_LOCAL_EULER = "Euler"
const val ERROR_LOCAL_IMPROVED_EULER = "Improved Euler"
const val ERROR_LOCAL_KUTTA = "Runge Kutta"

const val ERROR_GLOBAL_EULER = "Euler"
const val ERROR_GLOBAL_IMPROVED_EULER = "Improved Euler"
const val ERROR_GLOBAL_KUTTA = "Runge Kutta"

interface Solution {
    suspend fun solveAsync(config: SolveConfig): Deferred<XYChart.Series<Number, Number>>
}