package controller

import calculation.Evaluator
import calculation.solutons.SolveConfig
import calculation.solutons.from
import kotlinx.coroutines.Job
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

    fun maxStepsNumberChanged(newValue: Int?) {
        newValue?.let {
            dataModel.solutionParams.maxStepsNumber.set(newValue)

            recalculate()
        }
    }

    private fun recalculate() {
        val config = SolveConfig.from(dataModel.solutionParams)

        if (!isValid(config)) return

        cancelPreviousCalculations()

        launch ({
            dataModel.loading.set(true)

            val result = evaluator.calculate(config)

            dataModel.loading.set(false)
            dataModel.solution.set(result)
        }, {
            dataModel.loading.set(false)
        }, calculationJob)
    }

    private fun isValid(config: SolveConfig) = with(config) {
        xInitial < xFinal && minStepsCount < maxStepsCount && minStepsCount > 0
    }

    private fun cancelPreviousCalculations() {
        if (!calculationJob.isCompleted)
            calculationJob.cancel()

        calculationJob = Job()
    }

    fun minStepsNumberChanged(newValue: Int?) {
        newValue?.let {
            dataModel.solutionParams.minStepsNumber.set(newValue)

            recalculate()
        }
    }


}
