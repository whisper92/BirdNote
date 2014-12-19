BirdNote
========

超级记事本

数据表notes结构
```
create table notes
(_id integer primary key autoincrement,
level integer default 0,
title text,
textcontent text,
qua0 blob,
qua1 blob,
qua2 blob,
qua3 blob,
background integer,
star integer default 0,
create_time text,
update_time text
)
```

Textlines直接以json数组的方式存储,tqua表示所在的象限,tcontent表示内容
示例：
```
{
  "textcontents":[
  {"qua":"0","quatcontent":"hello"},
  {"qua":"1","quatcontent":"world"},
  {"qua":"2","quatcontent":"hello"},
  {"qua":"3","quatcontent":"world"}
  ]
}
```

 - 画布的大小要从values下根据分辨率获取。

 - 注意EditText自动换行时并未插入换行符，只是显示到下一行了。

 - 撤销和重做的思路：
        每绘制一笔，都将其保存到 mSaveUndoPath 中。如果要撤销一笔，将 mSaveUndoPath 栈顶的一笔移出，并压入 mSaveRedoPath 中，同时请求重绘。
        如果要重做一笔，都将 mSaveRedoPath 栈顶的一笔移出，并压入 mSaveUndoPath 中，同时请求重绘。

- - -
更新历史：

- version_1.1：基本功能全部实现
- version_1.2：完善全屏输入文字；修复bug
- version_1.3：完善功能；修复bug
- [version_2.0.2](http://whisperlog.qiniudn.com/BirdNote_2.0.2.apk)：改变界面风格

- - -

效果图：

![BirdNoteShow.png](/Media/BirdNoteShow.png)

![BirdNoteEdit1.png](/Media/BirdNoteEdit1.png)

![BirdNoteEdit2.png](/Media/BirdNoteEdit2.png)

橡皮擦功能、撤销和重做功能

![birdnote.gif](/Media/birdnote.gif)

- - -

THE END
