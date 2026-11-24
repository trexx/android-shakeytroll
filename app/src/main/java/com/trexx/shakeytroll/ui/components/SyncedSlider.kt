package com.trexx.shakeytroll.ui.components

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * A value-driven Slider on Material3's [SliderState] overload, behaving exactly like the
 * `Slider(value, onValueChange, …)` overload that Material3 1.5 deprecated.
 *
 * Our sliders are value-driven on purpose: the caller owns the value, so the device's channel-2
 * sync can move the thumb between drags, and the command is only sent from
 * [onValueChangeFinished] on release. Material3's suggested replacement,
 * `rememberSliderState(value, …)`, would break that: it is a rememberSaveable read once, so later
 * values from the device would never reach the thumb, and it isn't recreated when the range or
 * step count changes. So this does what the deprecated overload did internally: keep a
 * [SliderState] per (steps, valueRange) and write the caller's value into it on every
 * composition. [onValueChange] still receives each drag position (already snapped to [steps])
 * and decides the value; the state itself is never moved by the gesture.
 */
@Composable
fun SyncedSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  onValueChangeFinished: () -> Unit,
  valueRange: ClosedFloatingPointRange<Float>,
  enabled: Boolean,
  modifier: Modifier = Modifier,
  steps: Int = 0,
) {
  val state = remember(steps, valueRange) { SliderState(value, steps, valueRange) }
  state.value = value
  Slider(
    state = state,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    onValueChangeFinished = onValueChangeFinished,
  )
}
