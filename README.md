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
thumbnail blob,
star integer default 0
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
笔记等级：
0：蓝
1：绿
2：黄
3：红

象限分布示意图：
```
0|1
---
2|3
```

每行插入的空格数以及行数都要从values下根据分辨率获取。

注意EditText自动换行时并未插入换行符，只是显示到下一行了。

撤销和重做的思路：
每绘制一笔，都将其保存到 mSaveUndoPath 中。如果要撤销一笔，将 mSaveUndoPath 栈顶的一笔移出，并压入 mSaveRedoPath 中，同时请求重绘。
如果要重做一笔，都将 mSaveRedoPath 栈顶的一笔移出，并压入 mSaveUndoPath 中，同时请求重绘。
