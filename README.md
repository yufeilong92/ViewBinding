# ViewBinding
View Binding 的优点

Null 安全：由于视图绑定会创建对视图的直接引用，因此不存在因视图 ID 无效而引发 Null 指针异常的风险。此外，如果视图仅出现在布局的某些配置中，则绑定类中包含其引用的字段会使用 @Nullable 标记。
类型安全：每个绑定类中的字段均具有与它们在 XML 文件中引用的视图相匹配的类型。这意味着不存在发生类转换异常的风险。

```
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected lateinit var viewBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pt = javaClass.genericSuperclass as ParameterizedType
        val cla = pt.actualTypeArguments[0] as Class<*>
        try {
            val declaredMethod = cla.getDeclaredMethod("inflate", LayoutInflater::class.java)
            viewBinding = declaredMethod.invoke(null, layoutInflater) as T
            setContentView(viewBinding.root)
            initCreate()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    abstract fun initCreate()
}
```

```

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    protected lateinit var viewBinding: T
    abstract fun initCreateView(savedInstanceState: Bundle?)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            val parameterizedType = javaClass.genericSuperclass as ParameterizedType
            val clazz = parameterizedType.actualTypeArguments[0] as Class<*>
            val declaredMethod = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
            viewBinding = declaredMethod.invoke(null, inflater,container,false) as T
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCreateView(savedInstanceState)
    }

}
```