TODOwanandroid
======
[Flutter版本(未完成)](https://github.com/AqrLei/todo_flutter)  

介绍
------
   - 项目使用MVVM-DataBinding架构，采用Retrofit2网络请求框架，基于玩android的Todo Api,纯Kotlin编程。

NOTES
-----
   1. 使用MediatorLiveData的addSource，其本身必须也是被Observe的。也就是说必须有个
   *lifecycleOwner* 或者 *observeForever*，即必须有个Active 的Observe
   2. Kotlin + dataBinding 要 在*module*的 build.gradle中加上： <br>
   ` apply plugin: 'kotlin-kapt' `
   
   
性能优化
----

  1. 布局优化 
     - 父控件有背景颜色且与子控件背景颜色相同，则在子控件不必重复添加
     - 所有子控件的背景颜色可以完全覆盖父控件，则不必在父控件添加背景颜色
     - 简单布局尽量用LinearLayout,FrameLayout
     - 尽量减少不必要的布局嵌套(复杂的布局可以使用**ConstraintLayout**)
     - include,merge增加服用，减少层级
     - ViewStub按需加载
  2. 绘制优化
     - 在自定义View时，不要在onDraw()中执行耗时任务，也不要创建对象
  3. 内存优化   
     - 集合类内存泄漏
     - 单例造成的~
     - 非静态内部类/匿名类~
     -  静态变量（如静态的集合对象）~
     - 其它(I/O操作, 动态注册的广播, Service, ListView的Item, 静态的成员变量)