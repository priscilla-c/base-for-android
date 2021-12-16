package com.jie.tungcheung.utils.enshrine

import io.objectbox.query.PropertyQueryCondition
import io.objectbox.query.QueryBuilder
import io.objectbox.query.QueryCondition

//Created by Priscilla Cheung 2021年12月16日10:56:08.
object DataBaseStore {

    /**
     * 单条录入
     * 默认情况下,新对象的id由ObjectBox分配,当一个新对象被放置时,它将被分配到下一个最高的可用ID~
     * 例如,如果在一个框中有一个ID为1的对象和另一个ID为100的对象,则放置的下一个新对象将分配ID 101~
     * 如果您尝试自己分配一个新ID并放置对象,ObjectBox将抛出一个错误~
     */
    inline fun <reified T> put(bean: T) {
        try {
            Store.box.boxFor(T::class.java).put(bean)
        } catch (e: IllegalArgumentException) {
            toast("IllegalArgumentException")
            e.printStackTrace()
        }
    }

    /**
     * 多条录入
     */
    inline fun <reified T> put(bean: List<T>) =
        Store.box.boxFor(T::class.java).put(bean)

    /**
     * 通过Id单条获取
     * @param id 查询的Id
     * @return 返回一个实体类
     */
    inline fun <reified T> get(id: Long): T = Store.box.boxFor(T::class.java).get(id)

    /**
     * 获取全部
     * @return 返回一个实体类集合
     */
    inline fun <reified T> getAll(): List<T> {
        return try {
            Store.box.boxFor(T::class.java).all
        }catch (e:RuntimeException){
            arrayListOf()
        }catch (e:java.lang.IllegalArgumentException){
            arrayListOf()
        }
    }

    /**
     * 通过Id删除单条
     */
    inline fun <reified T> remove(id: Long) {
        try {
            Store.box.boxFor(T::class.java).remove(id)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 通过Id删除多条
     */
    inline fun <reified T> remove(id: List<Long>) {
        try {
            Store.box.boxFor(T::class.java).removeByIds(id)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 删除全部
     */
    inline fun <reified T> removeAll() {
        try {
            Store.box.boxFor(T::class.java).removeAll()

        }catch (e:RuntimeException){
            e.printStackTrace()
        }catch (e:java.lang.IllegalArgumentException){
            e.printStackTrace()
        }
    }


    /**
     * 条件查询
     * Store.query<UserBean>(UserBean_.code.equal(MyApp.isLogin))
     */
    inline fun <reified T> query(propertyQueryCondition: PropertyQueryCondition<T>): List<T> {
        var info: ArrayList<T> = ArrayList<T>()
        Store.box.boxFor(T::class.java).query(propertyQueryCondition).build().apply {
            info.addAll(find())
            close()
        }
        return info
    }

    /**
     * 复杂筛选
     * Store.queryCreate<AcceptanceMaterialsBean>()
     *.order(AcceptanceMaterialsBean_.recordDate)
     *.build().start()
     */
    inline fun <reified T> expertQuery(
        propertyQueryCondition: QueryCondition<T>? = null,
        extend:QueryBuilder<T>.()->Unit
    ): List<T> {
        val queryBuilder =  if(propertyQueryCondition != null) Store.box.boxFor(T::class.java).query(propertyQueryCondition)
        else Store.box.boxFor(T::class.java).query()
        extend.invoke(queryBuilder)
        var info: ArrayList<T> = ArrayList<T>()
        queryBuilder.build().apply {
            info.addAll(find())
            close()
        }
        return info
    }

    /**
     * 获取数量
     */
    inline fun <reified T> count(): Long =
        Store.box.boxFor(T::class.java).count()
}