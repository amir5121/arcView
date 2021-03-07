[![](https://jitpack.io/v/amir5121/arcView.svg)](https://jitpack.io/#amir5121/arcView)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-ArcView-green.svg?style=flat )]( https://android-arsenal.com/details/1/8130 )

# arcView

ArcView is an android library for showing list like an arc

## Installation
 - Gradle
Add it in your root `build.gradle` at the end of repositories:
    ```
    allprojects {
    	repositories {
    		...
    		maven { url 'https://jitpack.io' }
    	}
    }
    ```
    add the dependency
    ```
    dependencies {
	        implementation 'com.github.amir5121:arcView:TAG'
	}
    ```
 - [More ways to add it](https://jitpack.io/#amir5121/arcView/)

## Usage

![gif](https://media.giphy.com/media/jrzFqaxhIKyjTzIA4B/giphy.gif)

`layout.xml`
```
<?xml version="1.0" encoding="utf-8"?>
<com.amir.arcview.VerticalArcContainer xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/include_buttons_scroll_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true">

    <!--used to swap views in and out-->
    <com.amir.arcview.ArcScrollView
        android:id="@+id/include_arc_buttons_temp_arc"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:background="@color/move_button_background"
        android:elevation="3dp"
        android:visibility="gone"
        app:findBestWidth="true"
        app:radius="550dp"
        app:stroke_width="50dp" />

    <com.amir.arcview.ArcScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:background="#4499ff"
        android:elevation="5dp"
        app:findBestWidth="true"
        app:radius="400dp"
        app:stroke_width="50dp">

        <com.amir.arcview.ArcLinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            app:itemsOffset="7dp"
            app:useMinPadding="true">

            <ImageView
                android:id="@+id/include_buttons_stroke"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_stroke" />

            <!--add a bunch of image-->

        </com.amir.arcview.ArcLinearLayout>

    </com.amir.arcview.ArcScrollView>

    <com.amir.arcview.ArcScrollView
        android:id="@+id/activity_edit_image_move_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/move_button_background"
        android:elevation="6dp"
        app:findBestWidth="true"
        app:radius="300dp"
        app:stroke_width="40dp">

        <com.amir.arcview.ArcLinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            app:itemsOffset="7dp"
            app:useMinPadding="true">

            <!--you can put an empty view on each end if they won't reach the arc-->
            <View
                android:layout_width="25dp"
                android:layout_height="0dp" />

            <ImageView
                android:id="@+id/activity_edit_image_move_align_top"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_align_top" />

            <!--add a bunch of image-->

        </com.amir.arcview.ArcLinearLayout>

    </com.amir.arcview.ArcScrollView>

    <com.amir.arcview.ArcScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:background="#0088ff"
        android:elevation="9dp"
        app:findBestWidth="true"
        app:radius="100dp"
        app:stroke_width="50dp">

        <com.amir.arcview.ArcLinearLayout
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            app:itemsOffset="7dp"
            app:useMinPadding="true">

            <TextView
                android:id="@+id/include_buttons_text_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:clickable="true"
                android:focusable="true"
                android:padding="5dp"
                android:text="@string/text"
                android:textColor="#fff"
                android:textSize="20sp" />

        </com.amir.arcview.ArcLinearLayout>

    </com.amir.arcview.ArcScrollView>

</com.amir.arcview.VerticalArcContainer>
```
`MainActivity.kt`
```
package com.amir.arclistview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amir.arcview.ArcLinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_arc_button.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG: String = "MainActivity"
    private lateinit var strokeArc: ArcLinearLayout
    private lateinit var shadowArc: ArcLinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kick_me.setOnClickListener(this)
        kick_swapped.setOnClickListener(this)
        include_buttons_stroke.setOnClickListener(this)
        include_buttons_shadow.setOnClickListener(this)
        strokeArc =
            layoutInflater.inflate(
                R.layout.stroke_arc_linear_layout,
                include_arc_buttons_temp_arc,
                false
            ) as ArcLinearLayout
        shadowArc =
            layoutInflater.inflate(
                R.layout.shadow_arc_linear_layout,
                include_arc_buttons_temp_arc,
                false
            ) as ArcLinearLayout
    }

    override fun onClick(v: View?) {
        when (v) {
            kick_me -> {
                if (include_buttons_scroll_view.isKnockedIn) {
                    include_buttons_scroll_view.knockout()
                } else {
                    include_buttons_scroll_view.knockIn()
                }
            }
            kick_swapped -> {
                Log.wtf(TAG, "onClick: swapped")
                include_arc_buttons_temp_arc.swapView(null)
            }
            include_buttons_shadow -> {
                include_arc_buttons_temp_arc.swapView(shadowArc)
            }
            include_buttons_stroke -> {
                include_arc_buttons_temp_arc.swapView(strokeArc)
            }
        }
    }
}
```
![gif](https://media.giphy.com/media/j1hUT1JJkpjDsizDgK/giphy.gif)
```
<?xml version="1.0" encoding="utf-8"?>
<com.amir.arcview.ArcScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_alignParentBottom="true"
    android:layout_centerHorizontal="true"
    android:background="#b71c1c"
    android:elevation="5dp"
    app:radius="250dp"
    app:stroke_width="200dp">

    <com.amir.arcview.ArcLinearLayout
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        app:itemsOffset="7dp"
        app:useMinPadding="true">

        <ImageView
            android:id="@+id/include_buttons_stroke"
            style="@style/EditActivityImageView"
            android:contentDescription="@string/app_name"
            android:src="@drawable/ic_stroke" />

        <ImageView
            android:id="@+id/include_buttons_shadow"
            style="@style/EditActivityImageView"
            android:contentDescription="@string/app_name"
            android:src="@drawable/ic_shadow" />

        <!--add a bunch of image-->

    </com.amir.arcview.ArcLinearLayout>

</com.amir.arcview.ArcScrollView>

```

## API
### styleables
    ArcScrollView
        radius: dimension = defines the radius of the drawn circle
        stroke_width: dimension = defines how much of the circle should be visible
        findBestWidth: boolean = let the ArcScrollView decide the width

    ArcLinearLayout
        useMinPadding: boolean = add an extra padding to top and bottom of items if set to true won't add any extra padding
        itemsOffset: dimension = how much the items in ArcLinearLayout should be offset from top of the arc

## What need to be implemented?
Currently it doesn't support screen rotation. if this feature is asked for i will work on it.


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
