package view

import calculation.solutons.base.*
import controller.MainController
import javafx.beans.binding.Bindings
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TextField
import tornadofx.*
import java.util.concurrent.Callable

class MainView : View("De Assigment") {
    private val controller by inject<MainController>()

    private val xInitialDescription = solutionParams().xInitial.stringBinding { "Initial x is %.3f".format(it) }
    private val yInitialDescription = solutionParams().yInitial.stringBinding { "Initial y is %.3f".format(it) }

    private val xFinalDescription = solutionParams().xFinal.stringBinding { "Final x is %.2f".format(it) }
    private val stepSizeDescription = solutionParams().stepSize.stringBinding { "Step size is %.5f".format(it) }
    private val stepCountDescription = solutionParams().stepsNumber.stringBinding { "Step count is %d".format(it) }

    private val graphParams = controller.dataModel.graphParams

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
                linechart("Solutions", configureXAxis(), configureYAxis()) {
                    createSymbols = false

                    dataProperty().bind(controller.dataModel.graphData)
                    visibleWhen(Bindings.not(controller.dataModel.loading))

                    dataProperty().onChange { newValue ->
                        newValue?.forEach { series ->
                            val binder = when (series.name) {
                                SOLUTION_ANALYTICAL -> graphParams.isAnalyticalVisible
                                SOLUTION_EULER, ERROR_EULER -> graphParams.isEVisible
                                SOLUTION_IMPROVED_EULER, ERROR_IMPROVED_EULER -> graphParams.isIEVisible
                                SOLUTION_KUTTA, ERROR_KUTTA -> graphParams.isRKVisible
                                else -> null
                            }

                            series.node.visibleProperty().bind(binder)
                        }
                    }
                }

                text("Computing...").visibleProperty().bind(controller.dataModel.loading)
            }
        }
    }

    private fun configureXAxis(): Axis<Number> {
        val solutionParams = solutionParams()

        val tickProperty = (solutionParams.xFinal - solutionParams.xInitial) / 10

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

    private fun configureYAxis(): Axis<Number> {
        val model = controller.dataModel

        val solutionValue = model.solution.value

        val result = NumberAxis(solutionValue.minY, solutionValue.maxY, (solutionValue.maxY - solutionValue.minY) / 10)

        val minYBinding = Bindings.createDoubleBinding(Callable { model.solution.value.minY }, model.solution)
        val maxYBinding = Bindings.createDoubleBinding(Callable { model.solution.value.maxY }, model.solution)

        val stepBinding = (maxYBinding - minYBinding) / 10

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