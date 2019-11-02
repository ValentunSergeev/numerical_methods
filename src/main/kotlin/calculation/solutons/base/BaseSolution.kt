package calculation.solutons.base

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import model.MainModel
import tornadofx.data
import kotlin.math.abs

abstract class BaseSolution : Solution {
    override suspend fun solveAsync(params: MainModel.SolutionParams) = GlobalScope.async {
        init(params)

        val series = XYChart.Series<Number, Number>()

        val start = params.xInitial.value
        val end = params.xFinal.value
        val stepSize = params.stepSize.value

        var i = start

        val listSize = (abs(end - start) / stepSize).toInt() + 1

        val list = ArrayList<XYChart.Data<Number, Number>>(listSize)

        while (i <= end) {
            val y = compute(i)

            if (y.isNaN()) continue

            list.add(XYChart.Data(i, y))

            i += stepSize;
        }

        val data = FXCollections.observableArrayList(list)
        series.data = data

        series.name = name

        series
    }

    open fun init(params: MainModel.SolutionParams) {
        // STUB
    }

    abstract fun compute(x: Double): Double

    abstract val name: String
}