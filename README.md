# PinnedExpandableListView
必须设置pinnedGroupViewId
```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#1ad71a"
    tools:context=".MainActivity">

    <com.dhy.phel.PinnedExpandableListView
        android:id="@+id/expandableListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:cacheColorHint="@null"
        android:childDivider="@drawable/child_bg"
        android:childIndicatorLeft="0dp"
        android:dividerHeight="0dp"
        android:groupIndicator="@null"
        app:pinnedGroupViewId="@id/floatGroup"
        tools:listitem="@layout/group" />

    <include
        android:id="@+id/floatGroup"
        layout="@layout/group" />
</FrameLayout>
```
