package ru.practicum.android.diploma.common.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.custom_view.model.ButtonWithSelectedValuesLocationState
import ru.practicum.android.diploma.common.custom_view.model.ButtonWithSelectedValuesTextState
import ru.practicum.android.diploma.databinding.ViewButtonWithSelectedValuesBinding

class ButtonWithSelectedValues @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewButtonWithSelectedValuesBinding.inflate(
        LayoutInflater.from(context), this
    )

    fun renderTextState(state: ButtonWithSelectedValuesTextState) {
        when (state) {
            is ButtonWithSelectedValuesTextState.Content -> {
                showContent(state.hint, state.text)
            }

            is ButtonWithSelectedValuesTextState.Empty -> {
                showEmpty(state.hint)
            }
        }
    }

    fun renderLocationState(state: ButtonWithSelectedValuesLocationState) {
        when (state) {
            ButtonWithSelectedValuesLocationState.Gone -> showLocationGone()
            ButtonWithSelectedValuesLocationState.LocationEmpty -> showLocationEmpty()
            ButtonWithSelectedValuesLocationState.LocationLoading -> showLocationLoading()
            ButtonWithSelectedValuesLocationState.LocationSuccess -> showLocationSuccess()
        }
    }

    fun onIconButtonClick(click: () -> Unit) {
        binding.icon.setOnClickListener {
            click.invoke()
        }
    }

    fun onLocationButtonClick(click: () -> Unit) {
        binding.locationIcon.setOnClickListener {
            click.invoke()
        }
    }

    private fun showContent(hint: String, text: String) {
        binding.hintBig.text = hint
        binding.hintSmall.text = hint
        binding.text.text = text
        binding.text.isVisible = true
        binding.hintSmall.isVisible = true
        binding.hintBig.isVisible = false
        binding.icon.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources, R.drawable.ic_cross, null
            )
        )
    }

    private fun showEmpty(hint: String) {
        binding.hintBig.text = hint
        binding.hintSmall.text = hint
        binding.text.isVisible = false
        binding.hintSmall.isVisible = false
        binding.hintBig.isVisible = true
        binding.icon.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources, R.drawable.ic_chevron_right, null
            )
        )
    }

    private fun showLocationGone() {
        binding.locationIcon.isVisible = false
        binding.locationProgressBar.isVisible = false
    }

    private fun showLocationEmpty() {
        binding.locationIcon.isVisible = true
        binding.locationProgressBar.isVisible = false
        binding.locationIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources, R.drawable.ic_location_unknown, null
            )
        )
    }

    private fun showLocationLoading() {
        binding.locationIcon.isVisible = false
        binding.locationProgressBar.isVisible = true

    }

    private fun showLocationSuccess() {
        binding.locationProgressBar.isVisible = false
        binding.locationIcon.isVisible = true
        binding.locationIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources, R.drawable.ic_location_found, null
            )
        )
    }
}
