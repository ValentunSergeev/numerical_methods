package controller

import calculation.Evaluator
import kotlinx.coroutines.Job
import view.MainView
import launch
import tornadofx.Controller

class MainController : Controller() {
    public val dataModel = model.MainModel()

    private var calculationJob = Job()

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

    private fun recalculate() {
        cancelPreviousCalculations()

        launch ({
            dataModel.loading.set(true)

            val result = evaluator.calculate(dataModel.solutionParams)

            dataModel.loading.set(false)
            dataModel.solution.set(result)
        }, {
            dataModel.loading.set(false)
        }, calculationJob)
    }

    private fun cancelPreviousCalculations() {
        if (!calculationJob.isCompleted)
            calculationJob.cancel()

        calculationJob = Job()
    }


}
