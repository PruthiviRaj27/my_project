package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var posX = 0f
    private var posY = 0f


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.image_view)
        val scaleGesture = ScaleGesture(imageView)
        scaleGestureDetector = ScaleGestureDetector(this, scaleGesture)

        window.decorView.setOnTouchListener { _, event ->
            scaleGestureDetector?.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.rawX - lastTouchX
                        val deltaY = event.rawY - lastTouchY

                        // Calculate the new positions
                        val newPosX = posX + deltaX
                        val newPosY = posY + deltaY

                        // Calculate the maximum allowed translations
                        val maxPosX = (imageView.width * scaleGesture.scaleFactor - findViewById<FrameLayout>(R.id.frame_layout).width) / 2
                        val maxPosY = (imageView.height * scaleGesture.scaleFactor - findViewById<FrameLayout>(R.id.frame_layout).height) / 2
                        val minPosX = -maxPosX
                        val minPosY = -maxPosY

                        // Apply boundary checks
                        posX = newPosX.coerceIn(minPosX, maxPosX)
                        posY = newPosY.coerceIn(minPosY, maxPosY)

                        // Apply translations
                        imageView.translationX = posX
                        imageView.translationY = posY

                        lastTouchX = event.rawX
                        lastTouchY = event.rawY
                        Log.d("TAG", "onCreate1: $posX $posY")
                        Log.d("TAG", "onCreate2: ${event.x} ${event.y}")
                }
            }
            true
        }

    }

}

private class ScaleGesture(val imageView: ImageView) : SimpleOnScaleGestureListener() {
    var scaleFactor = 1.0f

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 6.0f))
        imageView.scaleX = scaleFactor
        imageView.scaleY = scaleFactor

        return true
    }
}