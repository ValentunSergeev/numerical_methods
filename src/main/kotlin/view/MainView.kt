package view

import calculation.data.Borders
import calculation.solutons.base.*
import controller.MainController
import javafx.beans.binding.Binding
import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.chart.Axis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.max
import java.lang.Double.min
import java.util.concurrent.Callable

private const val GRID_STEPS_COUNT = 10

private const val UPPER_BORDER : Double = 100_000.toDouble()

class MainView : View("De Assigment") {
    private val controller by inject<MainController>()

    private val graphParams = controller.dataModel.graphParams
    private val solutionParams = controller.dataModel.solutionParams
    private val solution = controller.dataModel.solution

    override val root = borderpane {
        right {
            form {
                fieldset("IVP") {
                    field("X initial") {
                        textfield(solutionParams.xInitial.stringValue()).onTextChanged {
                            controller.xInitialChanged(it.toDoubleOrNull())
                        }
                    }

                    field("Y initial") {
                        textfield(solutionParams.yInitial.stringValue()).onTextChanged {
                            controller.yInitialChanged(it.toDoubleOrNull())
                        }
                    }
                }

                fieldset("Solution Parameters") {
                    field("X final") {
                        textfield(solutionParams.xFinal.stringValue()).onTextChanged {
                            controller.xFinalChanged(it.toDoubleOrNull())
                        }
                    }

                    field("Min steps Count") {
                        textfield(solutionParams.minStepsNumber.stringValue()).onTextChanged {
                            controller.minStepsNumberChanged(it.toIntOrNull())
                        }
                    }

                    field("Max steps Count") {
                        textfield(solutionParams.maxStepsNumber.stringValue()).onTextChanged {
                            controller.maxStepsNumberChanged(it.toIntOrNull())
                        }
                    }
                }

                fieldset("Visibility") {
                    field("Analytical") {
                        val c = checkbox(property = graphParams.isAnalyticalVisible)
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
                linechart("Solutions", configureCommonXAxis(), configureYAxis(controller.dataModel.solutionYBordersProp)) {
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
            vbox {
                linechart("Local Errors", configureCommonXAxis(), configureYAxis(controller.dataModel.localErrorYBordersProp)) {
                    createSymbols = false

                    dataProperty().bind(controller.dataModel.localErrorsData)
                    visibleWhen(Bindings.not(controller.dataModel.loading))

                    dataProperty().onChange { newValue ->
                        newValue?.forEach { series ->
                            val binder = when (series.name) {
                                ERROR_LOCAL_EULER -> graphParams.isEVisible
                                ERROR_LOCAL_IMPROVED_EULER -> graphParams.isIEVisible
                                ERROR_LOCAL_KUTTA -> graphParams.isRKVisible
                                else -> null
                            }

                            series.node.visibleProperty().bind(binder)
                        }
                    }
                }

                linechart("Global Errors", configureGlobalErrorAxis(), configureYAxis (controller.dataModel.globalErrorYBordersProp)) {
                    createSymbols = false

                    dataProperty().bind(controller.dataModel.globalErrorsData)
                    visibleWhen(Bindings.not(controller.dataModel.loading))

                    dataProperty().onChange { newValue ->
                        newValue?.forEach { series ->
                            val binder = when (series.name) {
                                ERROR_GLOBAL_EULER -> graphParams.isEVisible
                                ERROR_GLOBAL_IMPROVED_EULER -> graphParams.isIEVisible
                                ERROR_GLOBAL_KUTTA -> graphParams.isRKVisible
                                else -> null
                            }

                            series.node.visibleProperty().bind(binder)
                        }
                    }
                }
            }
        }
    }

    private fun configureGlobalErrorAxis(): Axis<Number> {
        val tickProperty = (solutionParams.maxStepsNumber - solutionParams.minStepsNumber) / GRID_STEPS_COUNT

        val result =
            NumberAxis(
                "Steps count",
                solutionParams.minStepsNumber.doubleValue(),
                solutionParams.maxStepsNumber.doubleValue(),
                tickProperty.doubleValue()
            )

        result.tickUnitProperty().bind(tickProperty)
        result.lowerBoundProperty().bind(solutionParams.minStepsNumber)
        result.upperBoundProperty().bind(solutionParams.maxStepsNumber)

        return result
    }

    private fun configureCommonXAxis(): Axis<Number> {
        val tickProperty = (solutionParams.xFinal - solutionParams.xInitial) / GRID_STEPS_COUNT

        val result =
            NumberAxis(
                "X",
                solutionParams.xInitial.value,
                solutionParams.xFinal.value,
                tickProperty.value
            )

        result.tickUnitProperty().bind(tickProperty)
        result.lowerBoundProperty().bind(solutionParams.xInitial)
        result.upperBoundProperty().bind(solutionParams.xFinal)

        return result
    }


    private fun configureYAxis(bordersBinding: Binding<Borders>): Axis<Number> {
        val initialOBorders = bordersBinding.value

        val result = NumberAxis(
            "Y",
            initialOBorders.min,
            initialOBorders.max,
            (initialOBorders.min - initialOBorders.max) / GRID_STEPS_COUNT
        )

        val minYBinding = Bindings.createDoubleBinding(Callable { max(-UPPER_BORDER, bordersBinding.value.min) }, bordersBinding)
        val maxYBinding = Bindings.createDoubleBinding(Callable { min(UPPER_BORDER, bordersBinding.value.max) }, bordersBinding)

        val stepBinding = (maxYBinding - minYBinding) / GRID_STEPS_COUNT

        result.lowerBoundProperty().bind(minYBinding - stepBinding)
        result.upperBoundProperty().bind(maxYBinding + stepBinding)
        result.tickUnitProperty().bind(stepBinding)

        return result
    }
}

fun TextField.onTextChanged(event: (String) -> Unit) {
    textProperty().onChange {
        event.invoke(it ?: " ")
    }
}

fun <T> Property<T>.stringValue() = value.toString()