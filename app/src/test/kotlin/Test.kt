import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

class Test {

    @Test
    fun tryEmitTest() {
        val myStateFlow = MutableStateFlow(0)
        myStateFlow.tryEmit(2)
        myStateFlow.tryEmit(3)
        println(myStateFlow.value)
    }
}