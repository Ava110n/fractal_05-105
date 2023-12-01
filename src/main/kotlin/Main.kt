import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.math.sqrt

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    Canvas(modifier = Modifier.fillMaxSize(),
        onDraw = {
            var window = Window(this.size.width, this.size.height)
            Mandelbrot(this, window)
        }
    )
}

fun Mandelbrot(scope: DrawScope, w: Window){
    for(i in 0 .. scope.size.width.toInt()){
        for(j in 0 .. scope.size.height.toInt()){
            var s = Screen(i.toFloat(),j.toFloat())
            var d :Decart = s.ScrToDec(w)
            var c = Complex(d.x.toFloat(), d.y.toFloat())
            var iter = iteration(c)

            if(iter==1000){
                scope.drawCircle(Color.Black, radius = 1f,
                    center = Offset(s.x, s.y))
            }
        }
    }
}

fun iteration(c: Complex):Int{
    var iter = 0
    var z = Complex(0f,0f)
    var R = 2
    while(z.abs()<R && iter<1000){
        z = z*z + c
        iter+=1
    }
    return iter
}





fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

class Complex(var Re: Float = 0f, var Im: Float = 0f){
    fun abs():Float{
        return sqrt(this.Re*this.Re+this.Im*this.Im)
    }
    operator fun plus(c: Complex): Complex{
        return Complex(this.Re+c.Re, this.Im+c.Im)
    }
    operator fun times(c: Complex): Complex{
        var z = Complex()
        z.Re = this.Re*c.Re - this.Im*c.Im
        z.Im = this.Re*c.Im + this.Im*c.Re
        return z
    }

}
class Window(var w: Float, var h: Float,
             var xMin: Double=-2.5, var xMax: Double=1.0,
             var yMin: Double=-1.0, var yMax: Double=1.0){

}
class Decart(var x: Double=0.0, var y: Double=0.0){
    fun DecToScr(w: Window):Screen{
        var s_x = (this.x - w.xMin)*(w.w/(w.xMax-w.xMin))
        var s_y = (w.yMax-this.y)*(w.h/(w.yMax-w.yMin))
        var s = Screen(s_x.toFloat(), s_y.toFloat())
        return s
    }
}
class Screen(var x: Float=0f, var y: Float=0f){
    fun ScrToDec(w: Window):Decart{
        var d_x = this.x*(w.xMax-w.xMin)/w.w + w.xMin
        var d_y = w.yMax - this.y*(w.yMax-w.yMin)/w.h
        var d = Decart(d_x, d_y)
        return d
    }
}


