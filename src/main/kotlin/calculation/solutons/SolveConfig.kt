package calculation.solutons

import model.MainModel
import kotlin.math.abs

data class SolveConfig(
    val xInitial: Double,
    val xFinal: Double,
    val minStepsCount: Int,
    val maxStepsCount: Int,
    val yInitial: Double
) {
    companion object

    val xStepSize = abs(xFinal - xInitial) / maxStepsCount
}

fun SolveConfig.Companion.from(params: MainModel.SolutionParams) = SolveConfig(
    params.xInitial.value,
    params.xFinal.value,
    params.minStepsNumber.value,
    params.maxStepsNumber.value,
    params.yInitial.value
)