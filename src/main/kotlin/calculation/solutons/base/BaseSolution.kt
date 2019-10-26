package calculation.solutons.base

import javafx.scene.chart.XYChart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import model.MainModel
import step
import tornadofx.data

abstract class BaseSolution: Solution {
    override suspend fun solveAsync(params: MainModel.SolutionParams) = GlobalScope.async {
        init(params)

        val series = XYChart.Series<Number, Number>()

        val start = params.xInitial.value
        val end = params.xFinal.value
        val stepSize = params.stepSize.value

        for (i in start..end step stepSize) {
            val y = compute(i)

            if (y.isNaN()) continue

            series.data(i, y)
        }

        series.name = name

        series
    }

    open fun init(params: MainModel.SolutionParams) {
        // STUB
    }

    abstract fun compute(x: Double): Double

    abstract val name: String
}