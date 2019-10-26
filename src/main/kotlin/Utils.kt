import javafx.scene.chart.XYChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Color


infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}

fun launch(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: (suspend CoroutineScope.(Throwable) -> Unit)?
) {
    GlobalScope.launch(Dispatchers.Main) {
        try {
            tryBlock(this)
        } catch (e: Throwable) {
            catchBlock?.invoke(this, e)
        }
    }
}

fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
    launch(tryBlock, null)
}

fun XYChart.Series<*, *>.setColor(color: Color) {
    val line = node.lookup(".chart-series-line")

    val rgb = "${color.red}, ${color.green}, ${color.blue}"

    line.style = "-fx-stroke: rgba($rgb, 1.0);";
}