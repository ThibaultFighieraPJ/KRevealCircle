# KRevealCircle Android

Start any [fragment](https://developer.android.com/guide/components/fragments.html), 
[activity](https://developer.android.com/guide/components/activities/index.html) or [view](https://developer.android.com/reference/android/view/View.html) with a circular reveal effect.

<p align="center">
<img src="http://developers.pagesjaunes.fr/content/images/2017/12/reveal_final.gif"/>
</p>

Visit our blog post to see how it works deeply <a href="developers.pagesjaunes.fr/reveal-your-app-reveal-your-app-part1-3">Part 1 Fragment & Views</a> and <a href="developers.pagesjaunes.fr/reveal-your-app-reveal-your-app-part2">Part 2 Activities</a>

# How to use

The main class allowing to achieve circular reveal animation is achieved with [RevealCircleAnimatorHelper](https://github.com/ThibaultFighieraPJ/KRevealCircle/blob/master/app/src/main/java/com/tfighiera/revealactivity/RevealCircleAnimatorHelper.kt). It is essentialy based on <a href="https://developer.android.com/reference/android/view/ViewAnimationUtils.html">ViewAnimationUtils</a>
<a href="https://developer.android.com/reference/android/view/ViewAnimationUtils.html#createCircularReveal(android.view.View, int, int, float, float)">#createCircularReveal</a>.

## View

Use it with a targeted [view](https://developer.android.com/reference/android/view/View.html).

```kotlin
public fun getCircularAnimator(targetView: View, sourceX: Int, sourceY: Int, speed: Long): Animator {
        val finalRadius = Math.hypot(targetView.width.toDouble(), targetView.height.toDouble()).toFloat()
        return ViewAnimationUtils.createCircularReveal(targetView, sourceX, sourceY, 0f, finalRadius).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = speed
        }
    }
```

<p align="center">
<img src="http://developers.pagesjaunes.fr/content/images/2017/12/reveal_view-2.gif">
</p>

## Fragment

Assuming you are using [StarterPattern](https://hackernoon.com/object-oriented-tricks-4-starter-pattern-android-edition-1844e1a8522d) just call [RevealCircleAnimatorHelper#addBundleValues(Bundle, View)](https://github.com/ThibaultFighieraPJ/KRevealCircle/blob/master/app/src/main/java/com/tfighiera/revealactivity/RevealCircleAnimatorHelper.kt#L157) in builder function and start animation in `onCreateView`.

```kotlin
class RevealFragment : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.dummy_fragment, container, false)
        RevealCircleAnimatorHelper.create(this, container)
                                  .start(root)
        return root
    }

    ...
    
    companion object {
        fun newInstance(sourceView: View? = null) = RevealFragment().apply {
            arguments = Bundle()
            RevealCircleAnimatorHelper.addBundleValues(arguments, sourceView)
        }
    }
}
```

## Activity

Firstly make your Activity use the following attributes in your theme.

```xml
    <style name="AppTheme.Circled" parent="AppTheme.NoActionBar">
        <item name="android:windowIsTranslucent">true</item>
        <item name="colorPrimary">@color/colorAccent</item>
        <item name="android:windowBackground">@android:color/transparent</item>
    </style>
```

Assuming you are using [StarterPattern](https://hackernoon.com/object-oriented-tricks-4-starter-pattern-android-edition-1844e1a8522d) just call [RevealCircleAnimatorHelper#addBundleValues(Bundle, View)](https://github.com/ThibaultFighieraPJ/KRevealCircle/blob/master/app/src/main/java/com/tfighiera/revealactivity/RevealCircleAnimatorHelper.kt#L157) in builder function and start animation in `onCreate`.

```kotlin
class RevealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circled)
        RevealCircleAnimatorHelper
                .create(this)
                .start(root, getThemeColor(R.attr.colorAccent), getColor(R.color.background_default, theme))
    }
    
    companion object {
    
        fun newIntent(context: Context, sourceView: View? = null): Intent {
            return Intent(context, RevealActivity::class.java).also {
                RevealCircleAnimatorHelper.addBundleValues(it, sourceView)
            }
        }
    }
```

# License

```
Copyright 2017 PagesJaunes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
