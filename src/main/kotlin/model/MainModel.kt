package model

import calculation.data.SolutionResult
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import tornadofx.div
import tornadofx.minus
import java.util.concurrent.Callable

class MainModel {
    val solutionParams = SolutionParams()
    val graphParams = GraphParams()

    val solution = SimpleObjectProperty(SolutionResult())

    val graphData = Bindings.createObjectBinding(
        Callable {
            val result = FXCollections.observableArrayList<XYChart.Series<Number, Number>>()

            result.addAll(solution.value.data)

            result
        },

        solution
    )

    val loading = SimpleBooleanProperty()

    class SolutionParams {
        val xInitial = SimpleDoubleProperty(1.0)

        val yInitial = SimpleDoubleProperty(-2.0)

        val xFinal = SimpleDoubleProperty(7.0)

        val stepsNumber = SimpleIntegerProperty(10)

        val stepSize = (xFinal - xInitial) / stepsNumber
    }

    class GraphParams {
        val isEVisible = SimpleBooleanProperty(true)
        val isIEVisible = SimpleBooleanProperty(true)
        val isRKVisible = SimpleBooleanProperty(true)

        val isAnalyticalVisible = SimpleBooleanProperty(true)
    }
}