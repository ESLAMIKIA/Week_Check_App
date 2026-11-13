
package com.example.week_check

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.week_check.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isEvenWeek = true

    private val preferences by lazy {
        getSharedPreferences("EvenWeekPrefs", MODE_PRIVATE)
    }

    private val oddColors = intArrayOf(
        Color.rgb(30, 25, 65),
        Color.rgb(75, 45, 125)
    )

    private val oddButtonColors = intArrayOf(
        Color.rgb(167, 139, 250),
        Color.rgb(79, 70, 229)
    )

    private val evenColors = intArrayOf(
        Color.rgb(20, 35, 65),
        Color.rgb(35, 75, 115)
    )

    private val evenButtonColors = intArrayOf(
        Color.rgb(90, 220, 220),
        Color.rgb(40, 145, 180)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isEvenWeek = preferences.getBoolean("isEvenWeek", true)

        WeekChangeReceiver.scheduleNextSaturday(this)

        updateUI(false)

        binding.tvWeek.setOnClickListener {

            isEvenWeek = !isEvenWeek

            preferences.edit {
                putBoolean("isEvenWeek", isEvenWeek)
            }

            updateUI(true)

            val widgetManager = AppWidgetManager.getInstance(this)

            val widgetComponent = ComponentName(
                this,
                WeekWidget::class.java
            )

            val widgetIds = widgetManager.getAppWidgetIds(widgetComponent)

            val widgetIntent = Intent(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE
            ).apply {
                component = widgetComponent
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
            }

            sendBroadcast(widgetIntent)
        }

        // Add Widget Button
        binding.btnAddWidget.setOnClickListener {
            addWidgetToHomeScreen()
        }
    }

    private fun addWidgetToHomeScreen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val widgetManager = AppWidgetManager.getInstance(this)

            val widgetComponent = ComponentName(
                this,
                WeekWidget::class.java
            )

            if (widgetManager.isRequestPinAppWidgetSupported) {

                val requested = widgetManager.requestPinAppWidget(
                    widgetComponent,
                    null,
                    null
                )

                if (!requested) {
                    Toast.makeText(
                        this,
                        "لانچر گوشی اجازه افزودن مستقیم ویجت را نمی‌دهد.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                Toast.makeText(
                    this,
                    "لانچر گوشی از این قابلیت پشتیبانی نمی‌کند.",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            Toast.makeText(
                this,
                "این قابلیت به اندروید 8 یا بالاتر نیاز دارد.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateUI(animate: Boolean) {

        val backgroundColors =
            if (isEvenWeek) evenColors else oddColors

        val buttonColors =
            if (isEvenWeek) evenButtonColors else oddButtonColors

        val textColor =
            if (isEvenWeek) Color.rgb(167, 243, 208)
            else Color.rgb(221, 214, 254)

        binding.tvWeek.text =
            if (isEvenWeek) "زوج" else "فرد"

        binding.tvWeekLabel.text =
            if (isEvenWeek) "EVEN WEEK" else "ODD WEEK"

        binding.tvHint.text =
            if (isEvenWeek) "برای تغییر به هفته فرد لمس کنید"
            else "برای تغییر به هفته زوج لمس کنید"

        binding.tvSubtitle.setTextColor(textColor)
        binding.tvWeekLabel.setTextColor(textColor)
        binding.tvHint.setTextColor(textColor)

        val background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            backgroundColors
        )

        if (animate) {

            val oldColors =
                if (isEvenWeek) oddColors else evenColors

            val oldButtonColors =
                if (isEvenWeek) oddButtonColors else evenButtonColors

            val animator = ValueAnimator.ofFloat(0f, 1f)

            animator.duration = 650

            animator.addUpdateListener { animation ->

                val fraction = animation.animatedValue as Float

                val evaluator = ArgbEvaluator()

                val color1 = evaluator.evaluate(
                    fraction,
                    oldColors[0],
                    backgroundColors[0]
                ) as Int

                val color2 = evaluator.evaluate(
                    fraction,
                    oldColors[1],
                    backgroundColors[1]
                ) as Int

                val newBackground = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(color1, color2)
                )

                binding.rootLayout.background = newBackground

                val buttonColor1 = evaluator.evaluate(
                    fraction,
                    oldButtonColors[0],
                    buttonColors[0]
                ) as Int

                val buttonColor2 = evaluator.evaluate(
                    fraction,
                    oldButtonColors[1],
                    buttonColors[1]
                ) as Int

                val buttonBackground = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(buttonColor1, buttonColor2)
                ).apply {
                    shape = GradientDrawable.OVAL
                    setStroke(3, Color.argb(128, 255, 255, 255))
                }

                binding.tvWeek.background = buttonBackground
            }

            animator.start()

            binding.tvWeek.animate()
                .scaleX(0.88f)
                .scaleY(0.88f)
                .setDuration(120)
                .withEndAction {
                    binding.tvWeek.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(350)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
                .start()

        } else {

            binding.rootLayout.background = background

            binding.tvWeek.background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                buttonColors
            ).apply {
                shape = GradientDrawable.OVAL
                setStroke(3, Color.argb(128, 255, 255, 255))
            }
        }
    }
}
