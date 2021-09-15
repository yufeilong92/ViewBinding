# ViewBinding
View Binding 的优点

Null 安全：由于视图绑定会创建对视图的直接引用，因此不存在因视图 ID 无效而引发 Null 指针异常的风险。此外，如果视图仅出现在布局的某些配置中，则绑定类中包含其引用的字段会使用 @Nullable 标记。
类型安全：每个绑定类中的字段均具有与它们在 XML 文件中引用的视图相匹配的类型。这意味着不存在发生类转换异常的风险。

```
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected lateinit var viewBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  ParameterizedType 表示参数化类型，带有类型参数的类型，即常说的泛型，如：List<T>、Map<Integer, String>、List<? extends Number>。
        val pt = javaClass.genericSuperclass as ParameterizedType
       
        val cla = pt.actualTypeArguments[0] as Class<*>
        try {
           //通过反射加载类的inflate方法
            val declaredMethod = cla.getDeclaredMethod("inflate", LayoutInflater::class.java)
           //通过调用对应方法并强转成T
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
            //  ParameterizedType 表示参数化类型，带有类型参数的类型，即常说的泛型，如：List<T>、Map<Integer, String>、List<? extends Number>。
            val parameterizedType = javaClass.genericSuperclass as ParameterizedType
            val clazz = parameterizedType.actualTypeArguments[0] as Class<*>
            //通过反射加载类的inflate方法
            val declaredMethod = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
            //通过调用对应方法并强转成T
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
ParameterizedType 表示参数化类型，带有类型参数的类型，即常说的泛型
Type[]    getActualTypeArguments()  返回表示此类型实际类型参数的 Type 对象的数组。【重要】
                   简单来说就是：获得参数化类型中<>里的类型参数的类型。
                   因为可能有多个类型参数，例如Map<K, V>，所以返回的是一个Type[]数组。
                   注意：无论<>中有几层<>嵌套，这个方法仅仅脱去最外层的<>，之后剩下的内容就作为这个方法的返回值，所以其返回值类型不一定。
Type    getOwnerType()   返回 Type 对象，表示此类型是其成员之一的类型。
                   如果此类型为顶层类型，则返回 null（大多数情况都是这样）。
Type    getRawType()  返回 Type 对象，表示声明此类型的类或接口。
                   简单来说就是：返回最外层<>前面那个类型，例如Map<K ,V>，返回的是Map类型。
