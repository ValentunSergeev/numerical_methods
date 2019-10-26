package calculation.data

import javafx.scene.chart.XYChart

class SolutionResult(val data: List<XYChart.Series<Number, Number>> = listOf()) {
    val minY = allPoints().min() ?: 0.0
    val maxY = allPoints().max() ?: 0.0

    private fun allPoints(): List<Double> {
        return data.map { series ->
            series.data.map { entry ->
                entry.yValue as Double
            }
        }.flatten()
    }
}