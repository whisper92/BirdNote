BirdNote
========

超级记事本

数据表notes结构
```
create table notes
(_id integer primary key autoincrement,
level integer default 0,
title text default \note\,
content blob,
thumbnail blob)
```

一个Note包含4个象限笔记，每创建一个Note都要创建它的一个象限笔记的表,表的命名规则为child_父类的id，比如：child_05,就是_id为05的note对应的子表，表结构如下：
```
create table child_id
(
quadrant integer,
textlines text,  
content blob,
thumbnail blob  
)
```

Textlines直接以json数组的方式存储,tqua表示所在的象限，tx表示横坐标，ty表示纵坐标，tcontent表示内容
示例：
```
{
  "textlines":[
  {"tqua":"0","tx":"0","ty":"0","tcontext":"hello"},
  {"tqua":"1","tx":"100","ty":"100","tcontext":"world"}  
  ]
}
```
笔记等级：
0：蓝
1：绿
2：黄
3：红
