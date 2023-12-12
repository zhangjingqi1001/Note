<template>
  <li>
    <label>
      <!-- <input
        type="checkbox"
        :checked="todo.done"
        @click="handleCheck(todo.id)"
      /> -->

      <!-- :checked="todo.done"是想实现初始时是勾线的还是非勾选的 -->
      <!-- @click="handleCheck(todo.id)是想实现之后（更新）是勾线的还是非勾选的 -->
      <!-- 我们可以将其合成一个，合二为一 -->
      <!-- 因为我们的input类型是checkbox并且v-model绑定的是一个布尔类型的数据,这个布尔值就能决定复选框能勾选还是不能勾选 -->
      <!-- v-model双向绑定，归根到底此处改变的是App组件中的内容 -->
      <input type="checkbox" v-model="todo.done" />
      <span>{{ todo.title }}</span>
    </label>
    <button class="btn btn-danger" style="display: none">删除</button>
  </li>
</template>

<script>
export default {
  name: "MyItem",
  props: ["todo", "checkTodo"],
  methods: {
    //响应勾选的函数
    handleCheck(id) {
      console.log("复选框id:" + id);
      // 通知App组将将对应的todo对象的done值取反
      this.checkTodo(id);
    },
  },
};
</script>

<style scoped>
/*item*/
li {
  list-style: none;
  height: 36px;
  line-height: 36px;
  padding: 0 5px;
  border-bottom: 1px solid #ddd;
}

li label {
  float: left;
  cursor: pointer;
}

li label li input {
  vertical-align: middle;
  margin-right: 6px;
  position: relative;
  top: -1px;
}

li button {
  float: right;
  display: none;
  margin-top: 3px;
}

li:before {
  content: initial;
}

li:last-child {
  border-bottom: none;
}
</style>