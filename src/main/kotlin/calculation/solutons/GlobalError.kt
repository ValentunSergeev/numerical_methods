package calculation.solutons

import calculation.solutons.base.BaseSolution
import calculation.solutons.base.Solution
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.math.abs

class GlobalError(
    private val name: String,
    private val analytical: BaseSolution,
    private val estimation: BaseSolution
) : Solution {
    override suspend fun solveAsync(config: SolveConfig): Deferred<XYChart.Series<Number, Number>> = GlobalScope.async {
        require(config.minStepsCount < config.maxStepsCount)
        require(config.xInitial < config.xFinal)

        estimation.init(config)
        analytical.init(config)

        val series = XYChart.Series<Number, Number>()

        val start = config.minStepsCount
        val end = config.maxStepsCount

        val stepSize = 1

        var i = start

        val listSize = abs(end - start) + 1

        val list = ArrayList<XYChart.Data<Number, Number>>(listSize)

        var newConfig: SolveConfig

        while (i <= end) {
            newConfig = config.copy(maxStepsCount = i)

            estimation.init(newConfig)

            val y = compute(i, config.xInitial, config.xFinal)

            if (y.isFinite()) {
                list.add(XYChart.Data(i, y))
            }

            i += stepSize
        }

        val data = FXCollections.observableArrayList(list)
        series.data = data

        series.name = name

        series
    }

    private fun compute(currentStepsCount: Int, xInitial: Double, xFinal: Double): Double {
        var i = xInitial
        var estimationValue = 0.0

        val stepSize = abs(xFinal - xInitial) / currentStepsCount

        while (i <= xFinal) {
            estimationValue = estimation.compute(i)

            i += stepSize
        }

        val lastAnalyticalValue = analytical.compute(i - stepSize)

        return abs(lastAnalyticalValue - estimationValue)
    }
}