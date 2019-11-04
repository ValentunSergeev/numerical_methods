import javafx.scene.chart.XYChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Color
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun launch(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: (suspend CoroutineScope.(Throwable) -> Unit)?,
    context: CoroutineContext = EmptyCoroutineContext
) {
    GlobalScope.launch(Dispatchers.Main + context) {
        try {
            tryBlock(this)
        } catch (e: Throwable) {
            catchBlock?.invoke(this, e)
        }
    }
}

fun launch(tryBlock: suspend CoroutineScope.() -> Unit, context: CoroutineContext = EmptyCoroutineContext) {
    launch(tryBlock, null, context)
}

fun XYChart.Series<*, *>.setColor(color: Color) {
    val line = node.lookup(".chart-series-line")

    val rgb = "${color.red}, ${color.green}, ${color.blue}"

    line.style = "-fx-stroke: rgba($rgb, 1.0);"
}