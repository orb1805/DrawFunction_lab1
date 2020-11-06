package e.roman.lab10

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scaleMatrix
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt


class MainActivity : AppCompatActivity(), Drawer {

    private lateinit var imView: ImageView
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private lateinit var paintBlack: Paint
    private lateinit var paintGrey: Paint
    private lateinit var paintLightGrey: Paint
    private lateinit var drawBtn: Button
    private lateinit var ATxt: EditText
    private lateinit var BTxt: EditText
    private lateinit var aсTxt: EditText
    private lateinit var edgesTV: TextView
    private lateinit var pointsX: MutableList<Float>
    private lateinit var pointsY: MutableList<Float>
    private lateinit var squares: MutableList<Float>
    private var scale = 1f
    private var multer = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawBtn = findViewById(R.id.draw)
        ATxt = findViewById(R.id.A)
        BTxt = findViewById(R.id.B)
        aсTxt = findViewById(R.id.ac)
        edgesTV = findViewById(R.id.txt_edges)
        imView = findViewById(R.id.im_view)
        paintBlack = Paint()
        paintGrey = Paint()
        paintLightGrey = Paint()
        paintBlack.color = Color.BLACK
        paintGrey.textSize = 25f
        paintBlack.textSize = 25f
        paintGrey.color = Color.parseColor("#A0A0A0")
        paintLightGrey.color = Color.parseColor("#D0D0D0")
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        imView.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)
        val mScaleDetector = ScaleGestureDetector(this, MyPinchListener(this))
        imView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                mScaleDetector.onTouchEvent(event)
                return true
            }
        })

        drawBtn.setOnClickListener {
            edgesTV.setTextColor(Color.GRAY)
            val A = ATxt.text.toString().toFloat()
            val B = BTxt.text.toString().toFloat()
            val ac = aсTxt.text.toString().toFloat()
            var x = A
            val d = (abs(A) + abs(B)) / 50
            var b = max(abs(A), abs(B)).toInt()
            scale = 1f
            while (b > 1){
                b /= 10
                scale *= 10
            }
            if (-ac < A && A <= B && B < ac) {
                pointsX = mutableListOf()
                pointsY = mutableListOf()
                while (x < B + d) {
                    if (x > 0f && x - d < 0f) {
                        pointsY.add(y(0.0f, ac))
                        pointsX.add(0.0f)
                    } else {
                        pointsY.add(y(x, ac))
                        pointsX.add(x)
                    }
                    x += d
                }
            }else
                edgesTV.setTextColor(Color.RED)
            squares = mutableListOf()
            x = -500f
            while (x < 1500f){
                squares.add(x)
                x += 25
            }
            val multerX = max(abs(pointsX.last()), abs(pointsX[0]))
            val multerY = maxPoint(pointsY)
            multer = max(multerX, multerY)
            this.draw(1f)
        }
    }

    private fun y(x: Float, a: Float) : Float{
        return sqrt(x * x * (a - x) / (a + x))
    }

    private fun maxPoint(a: MutableList<Float>): Float {
        var max = 0f
        for (i in a)
            if (i > max)
                max = i
        return max
    }

    override fun draw(size: Float){
        canvas.drawColor(Color.WHITE)
        edgesTV.text = edgesTV.text
        val transler = 500 * (1 - size)
        var count = 0;
        //draw squares
        for (i in squares){
            canvas.drawLine(i * size + transler, 0f, i * size + transler, 1000f, paintLightGrey)
            canvas.drawLine(0f,  i * size + transler, 1000f,  i* size + transler, paintLightGrey)
            /*if (count == 10) {
                canvas.drawText((i - 500).toString(), i * size + transler, 500f, paintBlack)
                count = 0
            }
            else
                count++*/
        }
        //draw axis
        //x
        canvas.drawLine(0f, 500f, 1000f, 500f, paintGrey)
        //y
        canvas.drawLine(500f, 0f, 500f, 1000f, paintGrey)
        //xArrows
        canvas.drawLine(970f, 470f, 1000f, 500f, paintGrey)
        canvas.drawLine(970f, 530f, 1000f, 500f, paintGrey)
        //yArrows
        canvas.drawLine(500f, 0f, 530f, 30f, paintGrey)
        canvas.drawLine(500f, 0f, 470f, 30f, paintGrey)
        var diff = abs(pointsX[0] - scale)
        var point = 0f
        //draw function
        for (i in 0 until pointsX.lastIndex){
            canvas.drawLine(
                (pointsX[i] * 400 / multer + 500) * size + transler,
                (pointsY[i] * 400 / multer + 500) * size + transler,
                (pointsX[i + 1] * 400 / multer + 500) * size + transler,
                (pointsY[i + 1] * 400 / multer + 500) * size + transler,
                paintBlack
            )
            canvas.drawLine(
                (pointsX[i] * 400 / multer + 500) * size + transler,
                (-pointsY[i] * 400 / multer + 500) * size + transler,
                (pointsX[i + 1] * 400 / multer + 500) * size + transler,
                (-pointsY[i + 1] * 400 / multer + 500) * size + transler,
                paintBlack
            )
            if (abs(pointsX[i] - scale) < diff) {
                diff = abs(pointsX[i] - scale)
                point = pointsX[i]
            }
        }
        //draw scale
        canvas.drawText(
            (-scale).toString(),
            (-point * 400 / multer + 500) * size + transler,
            500f,
            paintGrey
        )
        canvas.drawText(
            (scale).toString(),
            (point * 400 / multer + 500) * size + transler,
            500f,
            paintGrey
        )
        canvas.drawText(
            (scale).toString(),
            500f,
            (-point * 400 / multer + 500) * size + transler,
            paintGrey
        )
        canvas.drawText(
            (-scale).toString(),
            500f,
            (point * 400 / multer + 500) * size + transler,
            paintGrey
        )
        canvas.drawText(
            (-scale / 2).toString(),
            (-point * 200 / multer + 500) * size + transler,
            500f,
            paintGrey
        )
        canvas.drawText(
            (scale / 2).toString(),
            (point * 200 / multer + 500) * size + transler,
            500f,
            paintGrey
        )
        canvas.drawText(
            (scale / 2).toString(),
            500f,
            (-point * 200 / multer + 500) * size + transler,
            paintGrey
        )
        canvas.drawText(
            (-scale / 2).toString(),
            500f,
            (point * 200 / multer + 500) * size + transler,
            paintGrey
        )
        canvas.drawText(
            (-2 * scale).toString(),
            (-point * 800 / multer + 500) * size + transler,
            500f,
            paintGrey
        )
        canvas.drawText(
            (scale * 2).toString(),
            (point * 800 / multer + 500) * size + transler,
            500f,
            paintGrey
        )
        canvas.drawText(
            (scale * 2).toString(),
            500f,
            (-point * 800 / multer + 500) * size + transler,
            paintGrey
        )
        canvas.drawText(
            (-scale * 2).toString(),
            500f,
            (point * 800 / multer + 500) * size + transler,
            paintGrey
        )
        //draw x, y
        canvas.drawText(
            "X",
            900f,
            480f,
            paintBlack
        )
        canvas.drawText(
            "Y",
            520f,
            80f,
            paintBlack
        )
        //imView.animateTransform(scaleMatrix(size, size))
    }
}