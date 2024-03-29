package com.wajahatkarim3.mediumclap_android

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.viewanimator.ViewAnimator
import com.wajahatkarim3.clapfab.DotsView
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : AppCompatActivity() {

    var clapCount = 0
    var isCirlceAvailable = false

    var hideAnimator: ViewAnimator? = null
    var textMoveUpAnimator: ViewAnimator? = null

    // Animations
    var fabScaleAnimation_1: ViewAnimator? = null
    var circleShowMoveUpAnimation_2: ViewAnimator? = null
    var circleScaleAnimation_3: ViewAnimator? = null
    var circleHideMoveAnimation_4: ViewAnimator? = null

    lateinit var dotsView: DotsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        initDots()

        fabDemoClap.setOnClickListener {

            clapCount++
            txtCountCircle.text = "+$clapCount"
            if (clapCount > 0)
            {
                fabDemoClap.setImageResource(R.drawable.ic_clap_hands_filled)
            }

            playActualFabAnim()
        }
    }

    fun initDots()
    {
        dotsView = findViewById(R.id.dotsView)
        dotsView.setColors(resources.getColor(R.color.colorPrimaryLight), resources.getColor(R.color.colorAccent))
        dotsView.currentProgress = 0f
        dotsView.setSize(400, 400)
    }

    fun playActualFabAnim()
    {
        isHideAnimStopped = true

        playDotsAnimation()

        // 1. Scale Up FAB Button On Each Click
        fabScaleUpAnimation()

        // 2. Show and move count text circle from button
        // If circle is not shown on top, then move up and show circle
        if (!isCirlceAvailable)
        {
            circleShowMoveUpAnimation()
        }
        // Else, scale up the counter text
        else
        {
            circleScaleAnimation()
        }

    }

    fun playDotsAnimation()
    {
        dotsView.currentProgress = 0f

        var dotsAnimator = ObjectAnimator.ofFloat(dotsView, DotsView.DOTS_PROGRESS, 0f, 1f)
        dotsAnimator.duration = 500
        dotsAnimator.interpolator = AccelerateDecelerateInterpolator()
        dotsAnimator.start()
    }


    fun fabScaleUpAnimation()
    {
        fabScaleAnimation_1 = ViewAnimator
                .animate(fabDemoClap)
                    .scale(1f, 1.2f)
                    .duration(70)
                .thenAnimate(fabDemoClap)
                    .scale(1.2f, 1.0f)
                    .duration(70)
                .start()
                .onStop {
                    fabScaleAnimation_1 = null
                }
    }

    fun circleShowMoveUpAnimation()
    {
        txtCountCircle.visibility = View.VISIBLE
        txtCountCircle.y = fabDemoClap.y + fabDemoClap.height/2
        txtCountCircle.alpha = 0f
        circleShowMoveUpAnimation_2 = ViewAnimator
                .animate(txtCountCircle)
                .dp().translationY(0f, -70f)
                .interpolator(DecelerateInterpolator())
                .alpha(0f, 1f)
                .duration(500)
                .onStop {
                    isCirlceAvailable = true
                    //circleShowMoveUpAnimation_2 = null
                    //circleHideMoveAnimation()
                    isHideAnimStopped = false
                    hideAnimTimer.start()
                }
                .start()
    }

    fun circleScaleAnimation()
    {
        circleScaleAnimation_3 = ViewAnimator
                .animate(txtCountCircle)
                .scale(1f, 1.2f)
                .duration(70)
                .thenAnimate(txtCountCircle)
                .scale(1.2f, 1.0f)
                .duration(70)
                .onStop {
                    // Hide Circle Anim
                    //circleScaleAnimation_3 = null
                    //circleHideMoveAnimation()
                    isHideAnimStopped = false
                    hideAnimTimer.start()
                }
                .start()
    }

    var hideAnimRunnable = Runnable {
        runOnUiThread {
            circleHideMoveAnimation()
        }
    }

    var isHideAnimStopped = false
    var hideAnimTimer = object : CountDownTimer(800, 50){
        override fun onTick(millisUntilFinished: Long) {
            if (isHideAnimStopped)
                cancel()
        }

        override fun onFinish() {
            runOnUiThread {
                circleHideMoveAnimation()
            }
        }
    }

    fun circleHideMoveAnimation()
    {
        circleHideMoveAnimation_4?.cancel()
        circleHideMoveAnimation_4 = ViewAnimator
                .animate(txtCountCircle)
                .alpha(1f, 0f)
                .dp().translationY( -70f, -140f)
                .duration(400)
                //.startDelay(1500)
                .onStop {
                    isCirlceAvailable = false
                    //circleShowMoveUpAnimation_2 = null
                    //circleHideMoveAnimation_4 = null
                }
                .start()
    }

    /*
    fun animateClapFab()
    {
        if (hideAnimator != null)
        {
            hideAnimator?.cancel()
            isCirlceAvailable = true
        }
        else
        {
            isCirlceAvailable = false
            hideAnimator?.cancel()
        }

        if (textMoveUpAnimator != null)
            return

        // Update clap icon depending on clap count
        txtCountCircle.text = "+$clapCount"
        if (clapCount > 0)
        {
            fabDemoClap.setImageResource(R.drawable.ic_clap_hands_filled)
        }

        // 1. Scale Up FAB Button On Each Click
        ViewAnimator
                .animate(fabDemoClap)
                    .scale(1f, 1.2f)
                    .duration(70)
                    //.interpolator(AccelerateDecelerateInterpolator())
                .thenAnimate(fabDemoClap)
                    .scale(1.2f, 1.0f)
                    .duration(70)
                    //.interpolator(AccelerateDecelerateInterpolator())
                .start()

        // 2. Show and move count text circle from button

        // Reset Circle Text State
        txtCountCircle.visibility = View.VISIBLE
        txtCountCircle.y = fabDemoClap.y + fabDemoClap.height/2
        txtCountCircle.alpha = 0f

        if (!isCirlceAvailable)
        {
            // Animate Text up
            textMoveUpAnimator = ViewAnimator
                    .animate(txtCountCircle)
                    .dp().translationY(0f, -70f)
                    .interpolator(DecelerateInterpolator())
                    .alpha(0f, 1f)
                    .duration(500)
                    .onStop {
                        Log.w("ANIM", "Circle stopped!")
                        isCirlceAvailable = true
                        textMoveUpAnimator = null
                    }
                    .start()

            // Hide Circle Anim
            hideAnimator = ViewAnimator.animate(txtCountCircle)
                    .alpha(1f, 0f)
                    .dp().translationY( -70f, -140f)
                    .duration(500)
                    .startDelay(500)
                    .onStop {
                        Log.e("ANIM", "Circle hidden")
                        //isCirlceAvailable = false
                    }
                    .start()
        }
        else
        {
            // Scale up Circle Text
            txtCountCircle.alpha = 1f
            ViewAnimator
                    .animate(txtCountCircle)
                        .scale(1f, 1.2f)
                        .duration(70)
                    .thenAnimate(txtCountCircle)
                        .scale(1.2f, 1.0f)
                        .duration(70)
                    .onStop {

                        // Hide Circle Anim
                        hideAnimator = ViewAnimator.animate(txtCountCircle)
                                .alpha(1f, 0f)
                                .dp().translationY( -70f, -140f)
                                .duration(500)
                                .startDelay(500)
                                .onStop {
                                    Log.e("ANIM", "Circle hidden")
                                    //isCirlceAvailable = false
                                }
                                .start()
                    }
                    .start()


        }



    }
    */

}
