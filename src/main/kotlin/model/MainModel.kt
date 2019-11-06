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

    val solutionYBordersProp = Bindings.createObjectBinding(
        Callable {
            solution.value.calculateBorders()

            solution.value.solutionBorders
        }, graphParams.isAnalyticalVisible, graphParams.isEVisible, graphParams.isIEVisible, graphParams.isRKVisible, solution
    )

    val localErrorYBordersProp = Bindings.createObjectBinding(
        Callable {
            solution.value.calculateBorders()

            solution.value.localErrorBorders
        }, graphParams.isAnalyticalVisible, graphParams.isEVisible, graphParams.isIEVisible, graphParams.isRKVisible, solution
    )

    val globalErrorYBordersProp = Bindings.createObjectBinding(
        Callable {
            solution.value.calculateBorders()

            solution.value.globalErrorBorders
        }, graphParams.isAnalyticalVisible, graphParams.isEVisible, graphParams.isIEVisible, graphParams.isRKVisible, solution
    )

    val solutionData = Bindings.createObjectBinding(
        Callable {
            val result = FXCollections.observableArrayList<XYChart.Series<Number, Number>>()

            result.addAll(solution.value.solutions)

            result
        },

        solution
    )

    val localErrorsData =  Bindings.createObjectBinding(
        Callable {
            val result = FXCollections.observableArrayList<XYChart.Series<Number, Number>>()

            result.addAll(solution.value.localErrors)

            result
        },

        solution
    )

    val globalErrorsData = Bindings.createObjectBinding(
        Callable {
            val result = FXCollections.observableArrayList<XYChart.Series<Number, Number>>()

            result.addAll(solution.value.globalErrors)

            result
        },

        solution
    )

    val loading = SimpleBooleanProperty()

    class SolutionParams {
        val xInitial = SimpleDoubleProperty(1.0)

        val yInitial = SimpleDoubleProperty(-2.0)

        val xFinal = SimpleDoubleProperty(7.0)

        val minStepsNumber = SimpleIntegerProperty(10)
        val maxStepsNumber = SimpleIntegerProperty(100)

        val stepSize = (xFinal - xInitial) / maxStepsNumber
    }

    class GraphParams {
        val isEVisible = SimpleBooleanProperty(true)
        val isIEVisible = SimpleBooleanProperty(true)
        val isRKVisible = SimpleBooleanProperty(true)

        val isAnalyticalVisible = SimpleBooleanProperty(true)
    }
}