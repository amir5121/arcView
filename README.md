# arcView

ArcView is an android library for showing list

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
	        implementation 'com.github.amir5121:arcView:1.0.0'
	}
    ```
 - [More ways to add it](https://jitpack.io/#amir5121/arcView/)

## Usage

```
<?xml version="1.0" encoding="utf-8"?>
<com.amir.arcview.VerticalArcContainer xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/include_buttons_scroll_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true">

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

            <View
                android:layout_width="25dp"
                android:layout_height="0dp" />

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

            <ImageView
                android:id="@+id/include_buttons_size_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_size" />

            <ImageView
                android:id="@+id/include_buttons_font_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_font" />

            <ImageView
                android:id="@+id/include_buttons_text_color"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_color" />

            <ImageView
                android:id="@+id/include_buttons_tilt_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_rotate_right" />

            <ImageView
                android:id="@+id/include_buttons_text_background"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_background_color" />

            <ImageView
                android:id="@+id/include_buttons_duplicate"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_copy" />

            <ImageView
                android:id="@+id/include_buttons_remove"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_close_white" />

            <View
                android:layout_width="25dp"
                android:layout_height="0dp" />

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

            <View
                android:layout_width="25dp"
                android:layout_height="0dp" />

            <ImageView
                android:id="@+id/activity_edit_image_move_align_top"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_align_top" />

            <ImageView
                android:id="@+id/activity_edit_image_move_align_left"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_align_left" />

            <ImageView
                android:id="@+id/activity_edit_image_move_center_horizontal"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_center_horizontal" />

            <ImageView
                android:id="@+id/activity_edit_image_move_up_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_up_arrow" />

            <ImageView
                android:id="@+id/activity_edit_image_move_down_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_down_arrow" />

            <ImageView
                android:id="@+id/activity_edit_image_move_left_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_left_arrow" />


            <ImageView
                android:id="@+id/activity_edit_image_move_right_button"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_right_arrow" />


            <ImageView
                android:id="@+id/activity_edit_image_move_center_vertical"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_center_vertical" />

            <ImageView
                android:id="@+id/activity_edit_image_move_align_right"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_align_right" />

            <ImageView
                android:id="@+id/activity_edit_image_move_align_bottom"
                style="@style/EditActivityImageView"
                android:contentDescription="@string/app_name"
                android:src="@drawable/ic_align_bottom" />

            <View
                android:layout_width="25dp"
                android:layout_height="0dp" />

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

![gif](https://media.giphy.com/media/igVoMAtVWycI7W8bUf/giphy.gif)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[GNU](https://choosealicense.com/licenses/gpl-3.0/)