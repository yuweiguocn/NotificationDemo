title: Android基础——Notification
date: 2016-09-16 09:01:55
tags: [android,basic,notification]
---

本文介绍了通知的使用，包括大文本、大图片


<!-- more -->

### 简单的通知
一个通知必须要设置的属性：
- 小图标，由setSmallIcon设置
- 标题，由setContentTitle设置
- 内容，由setContentText设置

从Android 5.0之后，通知可以显示在锁定屏幕上。

这里我们先抽出两个公用方法，generateBuilder()方法用于生成Builder对象，notify()方法用于设置PendingIntent并发出通知：

```
private NotificationCompat.Builder generateBuilder(String title, String content) {
   return new NotificationCompat.Builder(ct)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL);
}

private void notify(int notifyID, NotificationCompat.Builder mBuilder) {
    // Creates an explicit intent for an Activity in your app
    Intent resultIntent = new Intent(ct, AboutActivity.class);
    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
    // Adds the back stack for the Intent (but not the Intent itself)
    stackBuilder.addParentStack(AboutActivity.class);
    // Adds the Intent that starts the Activity to the top of the stack
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(resultPendingIntent);
    NotificationManager mNotificationManager =
            (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
    // mId allows you to update the notification later on.
    mNotificationManager.notify(notifyID, mBuilder.build());
}
```
然后通过调用上面两个方法创建一个简单的通知：
```
public void simpleNotification(View view) {
    String title = "Simple Notification";
    String content = "Hello World!";
    NotificationCompat.Builder mBuilder = generateBuilder(title, content);
    notify(UUID.randomUUID().hashCode(), mBuilder);
}
```

![](/art/notification-simple-1.png)
Android 5.1 & Android 4.3


从Android 5.0后，通知会显示在锁定屏幕上，锁屏效果图：
![](/art/notification-simple-2.png)
Android 5.1 & Android 4.3

### 常规的Activity PendingIntent
从上面的例子，当用户点击通知进入详情页面，从详情页面返回时进入应用主页面，首先我们需要修改清单文件，`<meta-data .../>`元素是为支持Android 4.0.3及更低的版本，属性android:parentActivityName是为了支持Android 4.1及更高的版本：
```
<activity android:name=".ui.main.MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>

        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
</activity>

<activity
    android:name=".ui.about.AboutActivity"
    android:label="@string/about"
    android:parentActivityName=".ui.main.MainActivity">
    <meta-data
        android:name="android.support.PARENT_ACTIVITY"
        android:value=".ui.main.MainActivity"/>
</activity>
```
通过TaskStackBuilder类设置任务栈：
```
// Creates an explicit intent for an Activity in your app
Intent resultIntent = new Intent(ct, AboutActivity.class);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
// Adds the back stack for the Intent (but not the Intent itself)
stackBuilder.addParentStack(AboutActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
stackBuilder.addNextIntent(resultIntent);
PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
mBuilder.setContentIntent(resultPendingIntent);
```
![](/art/notification-pending-intent-normal.gif)

>如果按照上面代码配置后没有作用时请将应用缷载再重新安装即可，这可能是由于Android Studio的即时运行导致的。


### 特殊的Activity PendingIntent
当一个Activity只能从通知进入时，配置清单文件，excludeFromRecents属性是为了从最近任务列表中移除，taskAffinity属性和在代码中设置FLAG_ACTIVITY_NEW_TASK标志是为了确保Activity不会进入应用的默认任务栈：
```
<activity android:name=".ui.special.SpecialActivity"
          android:launchMode="singleTask"
          android:taskAffinity=".special"
          android:label="@string/special"
          android:excludeFromRecents="true"/>
```
通过设置FLAG_ACTIVITY_NEW_TASK 和 FLAG_ACTIVITY_CLEAR_TASK将Intent设置为在新的空任务栈中启动：
```
public void specialActivityNotification(View view) {
        String title = "Special Activity Notification";
        String content = "Android 7.0 Nougat is here!";

    NotificationCompat.Builder mBuilder = generateBuilder(title, content);

    // Creates an Intent for the Activity
    Intent notifyIntent =
            new Intent(ct, SpecialActivity.class);
    // Sets the Activity to start in a new, empty task
    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    // Creates the PendingIntent
    PendingIntent notifyPendingIntent =
            PendingIntent.getActivity(ct, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    // Puts the PendingIntent into the notification builder
    mBuilder.setContentIntent(notifyPendingIntent);
    // Notifications are issued by sending them to the
    // NotificationManager system service.
    NotificationManager mNotificationManager =
            (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
    // Builds an anonymous Notification object from the builder, and
    // passes it to the NotificationManager
    mNotificationManager.notify(UUID.randomUUID().hashCode(), mBuilder.build());

}
```
![](/art/notification-pending-intent-special.gif)


### 扩展通知
扩展通知在4.1之前不可用，扩展通知包括4种样式：
- BigTextStyle 大文本样式
- InboxStyle 收件箱样式
- BigPictureStyle 大图片样式
- MediaStyle 媒体样式

#### 大文本样式的通知

我们需要使用NotificationCompat.BigTextStyle类设置大文本样式：

```
public void bigTextStyleNotification(View view) {
    String title = "Big Text Style Notification";
    String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
    NotificationCompat.Builder mBuilder = generateBuilder(title, content);

    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
    bigTextStyle.setBigContentTitle("Big Text Style Title");
    bigTextStyle.bigText("Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.");
    // Moves the expanded layout object into the notification object.
    mBuilder.setStyle(bigTextStyle);
    // Issue the notification here.
    notify(UUID.randomUUID().hashCode(), mBuilder);
}
```
![](/art/notification-big-text.gif)
Android 5.1 & Android 4.3

#### 收件箱样式的通知

```
public void inboxStyleNotification(View view) {
    String title = "Inbox Style Notification";
    String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
    NotificationCompat.Builder mBuilder = generateBuilder(title, content);

    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    // Sets a title for the Inbox in expanded layout
    inboxStyle.setBigContentTitle("Inbox Style Title");
    // Moves events into the expanded layout
    inboxStyle.setSummaryText("Inbox Style Summary Text");
    inboxStyle.addLine("Inbox Style Line 1");
    inboxStyle.addLine("Inbox Style Line 2");
    inboxStyle.addLine("Inbox Style Line 3");
    // Moves the expanded layout object into the notification object.
    mBuilder.setStyle(inboxStyle);
    // Issue the notification here.
    notify(UUID.randomUUID().hashCode(), mBuilder);
}
```
![](/art/notification-inbox.png)
Android 5.1 & Android 4.3
#### 大图片样式的通知

```
public void bigPictureStyleNotification(View view) {
    String title = "Big Picture Style Notification";
    String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
    NotificationCompat.Builder mBuilder = generateBuilder(title, content);

    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
    bigPictureStyle.setBigContentTitle("Big Picture");
    bigPictureStyle.setSummaryText("Big Picture Summary Text");
    bigPictureStyle.bigPicture(BitmapFactory.decodeResource(ct.getResources(), R.drawable.big_image_android));
    bigPictureStyle.bigLargeIcon(BitmapFactory.decodeResource(ct.getResources(), R.mipmap.ic_launcher));
    // Moves the expanded layout object into the notification object.
    mBuilder.setStyle(bigPictureStyle);
    // Issue the notification here.
    notify(UUID.randomUUID().hashCode(), mBuilder);
}
```
![](/art/notification-big-picture.png)
Android 5.1 & Android 4.3

#### 媒体样式通知
从Android 5.0之后，可以在锁定屏幕上控制媒体播放：

```
public void mediaStyleNotification(View view) {

    // Creates an explicit intent for an Activity in your app
    Intent resultIntent = new Intent(ct, AboutActivity.class);
    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
    // Adds the back stack for the Intent (but not the Intent itself)
    stackBuilder.addParentStack(AboutActivity.class);
    // Adds the Intent that starts the Activity to the top of the stack
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent intent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    String title = "Wonderful music";
    String content = "My Awesome Band";

    NotificationCompat.Builder mBuilder = generateBuilder(title, content);
    mBuilder.setSmallIcon(R.drawable.music);
    mBuilder.addAction(R.drawable.pre, "Previous", intent); // #0
    mBuilder.addAction(R.drawable.pause, "Pause", intent);  // #1
    mBuilder.addAction(R.drawable.next, "Next", intent);     // #2
    mBuilder.setStyle(new android.support.v7.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1));
    mBuilder.setLargeIcon(BitmapFactory.decodeResource(ct.getResources(), R.drawable.record));
    mBuilder.setAutoCancel(false);
    mBuilder.build();

    notify(9, mBuilder);
}
```

![](/art/notification-media-style.png)
Android 5.1 & Android 4.3

### 更新通知
只要我们使用相同notifyID就可以实现通知的更新：
```
public void updateNotification(View view) {
    // Start of a loop that processes data and then notifies the user
    ++numMessages;
    mBuilder.setContentText("You've received "+numMessages+" messages.")
            .setNumber(numMessages);
    // Because the ID remains unchanged, the existing notification is
    // updated.
    // mId allows you to update the notification later on.
    notify(notifyID, mBuilder);
}
```

![](/art/notification-update.png)
Android 5.1 & Android 4.3


### 删除通知
- 通过setAutoCancel()方法设置通知自动取消
- 通过cancel()删除指定ID的通知
- 通过cancelAll()删除所有发出的通知


### 进度条通知

对于Android 4.0之前，要在通知是显示进度指示器，必须创建包含ProgressBar的自定义通知布局。从Android 4.0之后，调用 setProgress()方法通知就可以显示进度指示器。

显示进度指示器的通知有两种样式：一是知道明确的进度值，二是不知道明确的进度值。

为了模拟进度指示器的通知，我们开启一个后台线程，在for循环中更新进度值：
```
public void progressNotification(View view) {

    final NotificationManager mNotifyManager =
            (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);

    final String title = "Picture Download";
    String content = "Download in progress";
    final NotificationCompat.Builder mBuilder = generateBuilder(title, content);
    mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);

    // Start a lengthy operation in a background thread
    new Thread(
            new Runnable() {
                @Override
                public void run() {
                    int incr;
                    // Do the "lengthy" operation 20 times
                    for (incr = 0; incr <= 100; incr += 5) {
                        // Sets the progress indicator to a max value, the
                        // current completion percentage, and "determinate"
                        // state
                        mBuilder.setProgress(100, incr, false);
                        // Displays the progress bar for the first time.
                        mNotifyManager.notify(0, mBuilder.build());
                        // Sleeps the thread, simulating an operation
                        // that takes time
                        try {
                            // Sleep for 1 seconds
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // When the loop is finished, updates the notification
                    mBuilder.setContentText("Download complete")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
                    mNotifyManager.notify(0, mBuilder.build());
                }
            }
            // Starts the thread by calling the run() method in its Runnable
    ).start();

}
```
![](/art/notification-progress.png)
Android 5.1 & Android 4.3


当我们不知道明确的进度值时，我们只需修改上述代码中的这一行
```
mBuilder.setProgress(100, incr, false);
```
为下面的代码即可：
```
mBuilder.setProgress(0, 0, true);
```
![](/art/notification-progress-indederminate.png)
Android 5.1 & Android 4.3



### 浮动通知
从Android 5.0开始支持浮动通知，下列条件可以触发浮动通知：
- 用户的 Activity 处于全屏模式中
- 通知具有较高的优先级并使用铃声或振动

设置Intent时应该调用setFullScreenIntent方法，当设置了FullScreenIntent在Android 5.0以下版本运行时，发出通知会直接打开相应Activity。

```
public void fullScreenNotification(View view) {
    String title = "Floating Notification by Full Screen Intent";
    String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
    NotificationCompat.Builder mBuilder = generateBuilder(title, content);
    // Creates an explicit intent for an Activity in your app
    Intent resultIntent = new Intent(ct, AboutActivity.class);
    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
    // Adds the back stack for the Intent (but not the Intent itself)
    stackBuilder.addParentStack(AboutActivity.class);
    // Adds the Intent that starts the Activity to the top of the stack
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent fullScreenIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setFullScreenIntent(fullScreenIntent, true);
    // Issue the notification here.
    notify(UUID.randomUUID().hashCode(), mBuilder);
}
```

![](/art/notification-floating-full-screen.gif)
Android 5.1 & Android 4.3




当设处于优先模式时，通过设置setCategory告诉系统如何处理应用的通知；通知默认优先级为0（PRIORITY_DEFAULT），当优先级设置为PRIORITY_MAX 或 PRIORITY_HIGH并使用铃声或振动时会触发浮动通知：
```
public void priorityNotification(View view) {

    // Creates an explicit intent for an Activity in your app
    Intent resultIntent = new Intent(ct, AboutActivity.class);
    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
    // Adds the back stack for the Intent (but not the Intent itself)
    stackBuilder.addParentStack(AboutActivity.class);
    // Adds the Intent that starts the Activity to the top of the stack
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent intent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


    String title = "Floating Notification by priority";
    String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
    NotificationCompat.Builder mBuilder = generateBuilder(title, content);
    mBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(content))
            .addAction (R.drawable.dismiss,
                    "Dismiss", intent)
            .addAction (R.drawable.snooze,
                    "Snooze", intent);
    notify(UUID.randomUUID().hashCode(), mBuilder);
}
```
![](/art/notification-floating-priority.png)
Android 5.1 & Android 4.3


### 自定义通知

我们可以使用RemoteViews创建自定义通知，有兴趣的童鞋可以自行了解下。

### Android 7.0新特性
可以在通知上进行消息的快速回复（Replying to notifications）

![](/art/notification-inline-reply.png)
图片来自Android官方文档

![](/art/notification-inline-type-reply.png)
图片来自Android官方文档


折叠通知（Bundling notifications），如果有超过三条来自同一应用的通知，系统会强制进行折叠
![](/art/notification-bundles.png)
图片来自Android官方文档

更强大的自定义View（Custom Views）

MessagingStyle的支持
```
Notification notification = new Notification.Builder()
             .setStyle(new Notification.MessagingStyle("Me")
             .setConversationTitle("Team lunch")
             .addMessage("Hi", timestamp1, null) // Pass in null for user.
             .addMessage("What's up?", timestamp2, "Coworker")
             .addMessage("Not much", timestamp3, null)
             .addMessage("How about lunch?", timestamp4, "Coworker"))
             .build();
```


>关于新特性的功能，且听下回分解。
