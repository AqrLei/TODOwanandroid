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
   
   
