# WanTodo

- notes
   1. 使用MediatorLiveData的addSource，其本身必须也是被Observe的。也就是说必须有个
   *lifecycleOwner* 或者 *observeForever*
   2. Kotlin + dataBinding 要 在*module*的 build.gradle中加上： <br>
   ` apply plugin: 'kotlin-kapt' `