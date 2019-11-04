package calculation.solutons.base

import calculation.solutons.SolveConfig
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.math.abs

abstract class BaseSolution : Solution {
    override suspend fun solveAsync(config: SolveConfig) = GlobalScope.async {
        require(config.minStepsCount < config.maxStepsCount)
        require(config.xInitial < config.xFinal)

        init(config)

        val series = XYChart.Series<Number, Number>()

        val start = config.xInitial
        val end = config.xFinal
        val stepSize = config.xStepSize

        var i: Double = start

        val listSize = (abs(end - start) / stepSize).toInt() + 1

        val list = ArrayList<XYChart.Data<Number, Number>>(listSize)

        while (i <= end) {
            val y = compute(i)

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

    open fun init(config: SolveConfig) {
        // STUB
    }

    abstract fun compute(x: Double): Double

    abstract val name: String
}