package controller

import calculation.Evaluator
import view.MainView
import launch
import tornadofx.Controller

class MainController : Controller() {
    public val dataModel = model.MainModel()

    val evaluator = Evaluator()

    init {
        recalculate()
    }

    fun xInitialChanged(newValue: Double?) {
        newValue?.let {
            dataModel.solutionParams.xInitial.set(newValue)

            recalculate()
        }
    }

    fun yInitialChanged(newValue: Double?) {
        newValue?.let {
            dataModel.solutionParams.yInitial.set(newValue)

            recalculate()
        }
    }

    fun xFinalChanged(newValue: Double?) {
        newValue?.let {
            dataModel.solutionParams.xFinal.set(newValue)

            recalculate()
        }
    }

    fun stepsNumberChanged(newValue: Int?) {
        newValue?.let {
            dataModel.solutionParams.stepsNumber.set(newValue)

            recalculate()
        }
    }

    fun eVisibilityChanged(newValue: Boolean) {
        dataModel.graphParams.isEVisible.set(newValue)
    }

    fun ieVisibilityChanged(newValue: Boolean) {
        dataModel.graphParams.isIEVisible.set(newValue)
    }

    fun rkVisibilityChanged(newValue: Boolean) {
        dataModel.graphParams.isRKVisible.set(newValue)
    }

    private fun recalculate() {
        launch ({
            dataModel.loading.set(true)

            val result = evaluator.calculate(dataModel.solutionParams)

            dataModel.loading.set(false)
            dataModel.solution.set(result)
        }, {
            dataModel.loading.set(false)
        })
    }
}
