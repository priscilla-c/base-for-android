package com.jie.tungcheung.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class BaseDao {
    @Id
    var id :Long = 0
}