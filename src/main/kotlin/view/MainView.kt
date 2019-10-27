package view

import calculation.data.Borders
import calculation.solutons.base.*
import controller.MainController
import javafx.beans.binding.Bindings
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TextField
import tornadofx.*
import java.util.concurrent.Callable

private const val GRID_STEPS_COUNT = 10

class MainView : View("De Assigment") {
    private val controller by inject<MainController>()

    private val xInitialDescription = solutionParams().xInitial.stringBinding { "Initial x is %.3f".format(it) }
    private val yInitialDescription = solutionParams().yInitial.stringBinding { "Initial y is %.3f".format(it) }

    private val xFinalDescription = solutionParams().xFinal.stringBinding { "Final x is %.2f".format(it) }
    private val stepSizeDescription = solutionParams().stepSize.stringBinding { "Step size is %.5f".format(it) }
    private val stepCountDescription = solutionParams().stepsNumber.stringBinding { "Step count is %d".format(it) }

    private val graphParams = controller.dataModel.graphParams
    private val solution = controller.dataModel.solution

    override val root = borderpane {
        right {
            text {
                bind(xInitialDescription)

                paddingAll = 20
            }

            text {
                bind(yInitialDescription)

                paddingAll = 20
            }

            text {
                bind(xFinalDescription)

                paddingAll = 20
            }

            text {
                bind(stepCountDescription)

                paddingAll = 20
            }


            text {
                bind(stepSizeDescription)

                paddingAll = 20
            }

            form {
                fieldset("IVP") {
                    field("X initial") {
                        textfield("1").onTextChanged {
                            controller.xInitialChanged(it.toDoubleOrNull())
                        }
                    }

                    field("Y initial") {
                        textfield("-2").onTextChanged {
                            controller.yInitialChanged(it.toDoubleOrNull())
                        }
                    }
                }

                fieldset("Solution Parameters") {
                    field("X final") {
                        textfield("7").onTextChanged {
                            controller.xFinalChanged(it.toDoubleOrNull())
                        }
                    }

                    field("Steps Count") {
                        textfield("10").onTextChanged {
                            controller.stepsNumberChanged(it.toIntOrNull())
                        }
                    }
                }

                fieldset("Visibility") {
                    field("Analytical") {
                        checkbox(property = graphParams.isAnalyticalVisible)
                    }

                    field("Euler method") {
                        checkbox(property = graphParams.isEVisible)
                    }

                    field("Improved Euler method") {
                        checkbox(property = graphParams.isIEVisible)
                    }

                    field("Runge Kutta method") {
                        checkbox(property = graphParams.isRKVisible)
                    }
                }
            }
        }

        center {
            vbox {
                linechart("Solutions", configureXAxis(), configureYAxis { solution.value.solutionBorders }) {
                    createSymbols = false

                    dataProperty().bind(controller.dataModel.solutionData)
                    visibleWhen(Bindings.not(controller.dataModel.loading))

                    dataProperty().onChange { newValue ->
                        newValue?.forEach { series ->
                            val binder = when (series.name) {
                                SOLUTION_ANALYTICAL -> graphParams.isAnalyticalVisible
                                SOLUTION_EULER -> graphParams.isEVisible
                                SOLUTION_IMPROVED_EULER -> graphParams.isIEVisible
                                SOLUTION_KUTTA -> graphParams.isRKVisible
                                else -> null
                            }

                            series.node.visibleProperty().bind(binder)
                        }
                    }
                }

                text("Computing...").visibleProperty().bind(controller.dataModel.loading)
            }
        }

        left {
            linechart("Errors", configureXAxis(), configureYAxis { solution.value.errorBorders }) {
                createSymbols = false

                dataProperty().bind(controller.dataModel.errorsData)
                visibleWhen(Bindings.not(controller.dataModel.loading))

                dataProperty().onChange { newValue ->
                    newValue?.forEach { series ->
                        val binder = when (series.name) {
                            ERROR_EULER -> graphParams.isEVisible
                            ERROR_IMPROVED_EULER -> graphParams.isIEVisible
                            ERROR_KUTTA -> graphParams.isRKVisible
                            else -> null
                        }

                        series.node.visibleProperty().bind(binder)
                    }
                }


            }
        }
    }

    private fun configureXAxis(): Axis<Number> {
        val solutionParams = solutionParams()

        val tickProperty = (solutionParams.xFinal - solutionParams.xInitial) / GRID_STEPS_COUNT

        val result =
            NumberAxis(
                solutionParams.xInitial.value,
                solutionParams.xFinal.value,
                tickProperty.value
            )

        result.tickUnitProperty().bind(tickProperty)
        result.lowerBoundProperty().bind(solutionParams.xInitial)
        result.upperBoundProperty().bind(solutionParams.xFinal)

        return result
    }

    private fun configureYAxis(borderGetter: () -> Borders): Axis<Number> {
        val model = controller.dataModel
        val initialOBorders = borderGetter.invoke()

        val result = NumberAxis(
            initialOBorders.min,
            initialOBorders.max,
            (initialOBorders.min - initialOBorders.max) / GRID_STEPS_COUNT
        )

        val minYBinding = Bindings.createDoubleBinding(Callable { borderGetter.invoke().min }, model.solution)
        val maxYBinding = Bindings.createDoubleBinding(Callable { borderGetter.invoke().max }, model.solution)

        val stepBinding = (maxYBinding - minYBinding) / GRID_STEPS_COUNT

        result.lowerBoundProperty().bind(minYBinding - stepBinding)
        result.upperBoundProperty().bind(maxYBinding + stepBinding)
        result.tickUnitProperty().bind(stepBinding)

        return result
    }

    private fun solutionParams() = controller.dataModel.solutionParams
}

fun TextField.onTextChanged(event: (String) -> Unit) {
    textProperty().onChange {
        event.invoke(it ?: " ")
    }
}