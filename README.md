
# EasyTextViewKotlin 

一个基于 Kotlin 开发的轻量级 TextView 扩展


---

## 安装方法  

本库已托管在 [JitPack](https://jitpack.io)。按照以下步骤集成到项目中：  

### 第一步：添加 JitPack 仓库  
在项目的 `build.gradle`（项目根目录）中添加：  
```groovy  
allprojects {  
    repositories {  
        maven { url 'https://jitpack.io' }  
    }  
}  
```

### 第二步：添加依赖  
在模块的 `build.gradle` 中添加以下依赖：  

```groovy  
dependencies {  
    implementation 'com.github.TaylorsZ:EasyTextViewKotlin:1.0.5'  
}  
```  

如果你使用的是 Kotlin DSL（`build.gradle.kts`），请添加以下代码：  

```kotlin  
dependencies {  
    implementation("com.github.TaylorsZ:EasyTextViewKotlin:1.0.5")  
}  
```  


---

## 许可证  
本项目使用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。  

---

## 问题反馈  
如果你在使用过程中遇到问题或有功能需求，欢迎在 GitHub 仓库提交 Issue。  
